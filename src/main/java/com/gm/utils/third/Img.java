package com.gm.utils.third;

import com.gm.help.base.Quick;
import com.gm.utils.Utils;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;

/**
 * The type Img.
 *
 * @author Jason
 */
public class Img implements Utils {

    /**
     * 临时文件夹
     */
    public static final String tmpdir = System.getProperty("java.io.tmpdir");

    /**
     * 默认0.3倍量化图片.
     *
     * @param file the file
     */
    public static void quantify(File file) {
        quantify(0.3, file);
    }

    /**
     * 自定义(0-1]倍量化图片.
     *
     * @param multiple the multiple
     * @param file     the file
     */
    public static void quantify(double multiple, File file) {
        Thumbnails.Builder<File> builder = Thumbnails.of(file).scale(1f).outputQuality(multiple);
        Quick.run(x -> builder.toFile(file));
    }

    /**
     * 默认0.3倍缩放图片.
     *
     * @param file the file
     */
    public static void scale(File file) {
        scale(0.3, file);
    }

    /**
     * 自定义(0-1]倍缩放图片.
     *
     * @param multiple the multiple
     * @param file     the file
     */
    public static void scale(double multiple, File file) {
        Thumbnails.Builder<File> builder = Thumbnails.of(file).scale(multiple).outputQuality(1f);
        Quick.run(x -> builder.toFile(file));
    }
}
