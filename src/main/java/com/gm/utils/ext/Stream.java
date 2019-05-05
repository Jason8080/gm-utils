package com.gm.utils.ext;

import com.gm.help.base.Quick;
import com.gm.utils.Utils;
import com.gm.utils.base.Bool;

import java.io.*;
import java.net.URL;

/**
 * 字节流工具.
 *
 * @author Jason
 */
public class Stream implements Utils {

    /* 流控制部分 */

    /**
     * 转换为可复制流.
     *
     * @param in the in
     * @return the byte array output stream
     * @throws IOException the io exception
     */
    public static ByteArrayOutputStream toStream(InputStream in) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > -1) {
            stream.write(buffer, 0, len);
        }
        stream.flush();
        return stream;
    }

    /**
     * 将字节输出流转换为输入流.
     *
     * @param out the out
     * @return the input stream
     */
    public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * 将流转换为字符.
     *
     * @param in      the in
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String toString(InputStream in, String... charset) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            if (Bool.isNull(charset) || Bool.isNull(charset[0])) {
                out.append(new String(b, 0, n));
            } else {
                out.append(new String(b, 0, n, charset[0]));
            }
        }
        return out.toString();
    }

    /**
     * 将流转换为字符.
     *
     * @param out     the out
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String toString(ByteArrayOutputStream out, String... charset) throws IOException {
        ByteArrayInputStream in = toStream(out);
        return toString(in, charset);
    }

    /* 文件流部分 */

    public static InputStream in(String filename) {
        return Stream.class.getClassLoader().getResourceAsStream(filename);
    }

    public static File get(String filename) {
        URL url = Stream.class.getClassLoader().getResource(filename);
        return Quick.exec(x -> new File(url.toURI()));
    }

}
