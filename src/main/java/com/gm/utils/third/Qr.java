package com.gm.utils.third;

import com.gm.utils.base.Logger;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具
 *
 * @author Jason
 */
public class Qr extends LuminanceSource {
    /**
     * 二维码颜色
     */
    private static final int BLACK = 0xFF000000;
    /**
     * 二维码颜色
     */
    private static final int WHITE = 0xFFFFFFFF;

    private final BufferedImage image;
    private final int left;
    private final int top;

    /**
     * Instantiates a new Qr.
     *
     * @param image the image
     */
    public Qr(BufferedImage image) {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Instantiates a new Qr.
     *
     * @param image  the image
     * @param left   the left
     * @param top    the top
     * @param width  the width
     * @param height the height
     */
    public Qr(BufferedImage image, int left, int top, int width, int height) {
        super(width, height);
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        if (left + width > sourceWidth || top + height > sourceHeight) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }
        for (int y = top; y < top + height; y++) {
            for (int x = left; x < left + width; x++) {
                if ((image.getRGB(x, y) & 0xFF000000) == 0) {
                    // = white
                    image.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
        this.image.getGraphics().drawImage(image, 0, 0, null);
        this.left = left;
        this.top = top;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        image.getRaster().getDataElements(left, top + y, width, 1, row);
        return row;
    }

    @Override
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();
        int area = width * height;
        byte[] matrix = new byte[area];
        image.getRaster().getDataElements(left, top, width, height, matrix);
        return matrix;
    }

    @Override
    public boolean isCropSupported() {
        return true;
    }

    @Override
    public LuminanceSource crop(int left, int top, int width, int height) {
        return new Qr(image, this.left + left, this.top + top, width, height);
    }

    @Override
    public boolean isRotateSupported() {
        return true;
    }

    @Override
    public LuminanceSource rotateCounterClockwise() {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);
        BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
        int width = getWidth();
        return new Qr(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
    }

    /**
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 生成二维码图片
     *
     * @param matrix the matrix
     * @param format the format
     * @param file   the file
     * @throws IOException the io exception
     */
    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    /**
     * 生成二维码图片流
     *
     * @param matrix the matrix
     * @param format the format
     * @param stream the stream
     * @throws IOException the io exception
     */
    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 生成二维码图片流
     *
     * @param matrix the matrix
     * @param format the format
     * @param stream the stream
     * @throws IOException the io exception
     */
    public static void writeToStream(BitMatrix matrix, String format, ByteArrayOutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 根据内容，生成指定宽高、指定格式的二维码图片
     *
     * @param text     内容
     * @param width    宽
     * @param height   高
     * @param format   图片格式
     * @param pathname the pathname
     * @return 生成的二维码图片路径 string
     * @throws Exception the exception
     */
    public static String generateQRCode(String text, int width, int height, String format, String pathname)
            throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 白边大小，取值范围0~4
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File(pathname);
        writeToFile(bitMatrix, format, outputFile);
        return pathname;
    }

    /**
     * 输出二维码图片流
     *
     * @param text     二维码内容
     * @param width    二维码宽
     * @param height   二维码高
     * @param format   图片格式: png, jpg, gif
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    public static void generateQRCode(String text, int width, int height, String format, HttpServletResponse response)
            throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 白边大小，取值范围0~4
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        writeToStream(bitMatrix, format, response.getOutputStream());
    }

    /**
     * 输出二维码图片流
     *
     * @param text   二维码内容
     * @param width  二维码宽
     * @param height 二维码高
     * @param format 图片格式: png, jpg, gif
     * @return the byte array output stream
     * @throws Exception the exception
     */
    public static ByteArrayOutputStream generateQRCode(String text, int width, int height, String format) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 白边大小，取值范围0~4
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        writeToStream(bitMatrix, format, out);
        return out;
    }

    /**
     * 解析指定路径下的二维码图片
     *
     * @param filePath 二维码图片路径
     * @return string string
     */
    public static String parseQRCode(String filePath) {
        String content = "";
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new Qr(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap(1);
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            MultiFormatReader formatReader = new MultiFormatReader();
            Result result = formatReader.decode(binaryBitmap, hints);

            Logger.info("result 为：" + result.toString());
            Logger.info("resultFormat 为：" + result.getBarcodeFormat());
            Logger.info("resultText 为：" + result.getText());
            // 设置返回值
            content = result.getText();
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return content;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // 随机生成验证码
        String text = "http://baidu.com/";
        System.out.println("随机码： " + text);
        // 二维码图片的宽
        int width = 400;
        // 二维码图片的高
        int height = 400;
        // 二维码图片的格式
        String format = "png";

        try {
            // 生成二维码图片，并返回图片路径
            String pathName = generateQRCode(text, width, height, format, "C:\\Users\\Administrator\\Desktop\\new.png");
            System.out.println("生成二维码的图片路径： " + pathName);

            String content = parseQRCode(pathName);
            System.out.println("解析出二维码的图片的内容为： " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
