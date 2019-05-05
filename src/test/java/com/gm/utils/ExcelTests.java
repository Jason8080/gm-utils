package com.gm.utils;

import com.gm.utils.base.Excel;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelTests {

    public static void main(String[] args) throws IOException {
        Map sheet = new HashMap();
        List data = new ArrayList();
        Map row1 = new HashMap();
        row1.put("a","1");
        row1.put("b","12");
        row1.put("c","123");
        row1.put("d","1234");
        row1.put("e","12345");
        Map row2 = new LinkedHashMap();
        row2.put("一","1");
        row2.put("二","12");
        row2.put("三","123");
        row2.put("四","1234");
        row2.put("五","12345");
        Map row3 = new HashMap();
        row3.put("a","1");
        row3.put("b","12");
        row3.put("c","123");
        row3.put("d","1234");
        row3.put("e","12345");
        data.add(row1);
        data.add(row2);
        data.add(row3);
//        List row1 = new ArrayList();
//        row1.add("1");
//        row1.add("12");
//        row1.add("123");
//        row1.add("1234");
//        row1.add("12345");
//        data.add(row1);
//        data.add(row1);
//        data.add(row1);
        sheet.put("one", data);
        Workbook wb = Excel.create(sheet);
        FileOutputStream stream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.xls");
        wb.write(stream);
    }
}
