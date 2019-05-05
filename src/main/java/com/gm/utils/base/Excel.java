package com.gm.utils.base;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

/**
 * 导出Excel文档工具类
 *
 * @author Jason
 */
public class Excel {
    /**
     * 创建工作表.
     *
     * @param rows   整表 (单列数据)
     * @return the workbook 工作表
     */
    public static Workbook news(List<Object> rows) {
        // 创建工作簿
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        // 创建表
        CellStyle nameStyle = wb.createCellStyle();
        CellStyle valStyle = wb.createCellStyle();
        // 设置列名和列值样式
        style(wb, nameStyle, valStyle);
        if (!Bool.isNull(rows)) {
            for (int i = 0; i < rows.size(); i++) {
                // 获取每行数据
                Object column = rows.get(i);
                Row row = sheet.createRow(i);
                int count = 0;
                sheet.setColumnWidth(0, (short) (35.7 * 150));
                Cell cell = row.createCell(count);
                cell.setCellValue(column != null ? column.toString() : null);
                cell.setCellStyle(nameStyle);
            }
        }
        return wb;
    }

    /**
     * 创建工作表.
     *
     * @param sheet   整表 (多列数据)
     * @param columns the columns 列名
     * @return the workbook 工作表
     */
    public static Workbook create(List<List<Object>> sheet, String... columns) {
        Map<String, List<List<Object>>> map = new HashMap(1);
        map.put("Sheet1", sheet);
        return create(map, columns);
    }

    /**
     * 创建工作簿.
     *
     * @param book    the book 整簿纯数据
     * @param columns the columns 列名
     * @return the workbook 工作簿
     */
    public static Workbook create(Map<String, List<List<Object>>> book, String... columns) {
        // 创建工作簿
        Workbook wb = new HSSFWorkbook();
        if (!Bool.isNull(book)) {
            Iterator<Map.Entry<String, List<List<Object>>>> it = book.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<List<Object>>> next = it.next();
                // 创建表
                Sheet sheet = wb.createSheet(next.getKey());
                CellStyle nameStyle = wb.createCellStyle();
                CellStyle valStyle = wb.createCellStyle();
                // 设置列名和列值样式
                style(wb, nameStyle, valStyle);
                int def = 0;
                if (!Bool.isNull(columns)) {
                    Row top = sheet.createRow(def++);
                    for (int i = 0; i < columns.length; i++) {
                        sheet.setColumnWidth(i, (short) (35.7 * 150));
                        Cell cell = top.createCell(i);
                        cell.setCellValue(columns[i]);
                        cell.setCellStyle(nameStyle);
                    }
                }
                // 获取整页数据
                List<List<Object>> data = next.getValue();
                if (!Bool.isNull(data)) {
                    for (int i = 0; i < data.size(); i++) {
                        // 获取每行数据
                        List<Object> list = data.get(i);
                        Row row = sheet.createRow(i + def);
                        int count = 0;
                        for (Object value : list) {
                            Cell cell = row.createCell(count);
                            cell.setCellValue(value != null ? value.toString() : null);
                            cell.setCellStyle(valStyle);
                            ++count;
                        }
                    }
                }
            }
        }
        return wb;
    }

    /**
     * 创建工作簿.
     *
     * @param book 整簿数据(注意: 列名不同将导致列值无法对齐[除非传递LinkedHashMap])
     * @return the 工作簿
     */
    public static Workbook create(Map<String, List<LinkedHashMap<String, Object>>> book) {
        // 创建工作簿
        Workbook wb = new HSSFWorkbook();
        if (!Bool.isNull(book)) {
            Iterator<Map.Entry<String, List<LinkedHashMap<String, Object>>>> it = book.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<LinkedHashMap<String, Object>>> next = it.next();
                // 创建表
                Sheet sheet = wb.createSheet(next.getKey());
                CellStyle nameStyle = wb.createCellStyle();
                CellStyle valStyle = wb.createCellStyle();
                // 设置列名和列值样式
                style(wb, nameStyle, valStyle);
                // 获取整页数据
                List<LinkedHashMap<String, Object>> data = next.getValue();
                if (!Bool.isNull(data)) {
                    for (int i = 0; i < data.size(); i++) {
                        // 获取每行数据
                        Map<String, Object> map = data.get(i);
                        if (i == 0) {
                            Row row = sheet.createRow(0);
                            int count = 0;
                            for (String key : map.keySet()) {
                                sheet.setColumnWidth(count, (short) (35.7 * 150));
                                Cell cell = row.createCell(count);
                                cell.setCellValue(key);
                                cell.setCellStyle(nameStyle);
                                ++count;
                            }
                        }
                        Row row = sheet.createRow(i + 1);
                        int count = 0;
                        for (Object value : map.values()) {
                            Cell cell = row.createCell(count);
                            cell.setCellValue(value != null ? value.toString() : null);
                            cell.setCellStyle(valStyle);
                            ++count;
                        }
                    }
                }
            }
        }
        return wb;
    }

    private static void style(Workbook wb, CellStyle cs, CellStyle cs2) {
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 创建第三种字体样式（未使用）
        Font f3 = wb.createFont();
        f3.setFontHeightInPoints((short) 10);
        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
    }
}
