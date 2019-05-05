package com.gm.utils.base;

import java.math.BigInteger;

/**
 * 进制转换工具.
 *
 * @author Jason
 */
public class Hex {
    /**
     * 1Byte=8bit
     */
    public static final int bits = 8;
    /**
     * int=32Bit
     */
    public static final int decimal = 32;

    /**
     * 字节 转 二进制 字符串.
     *
     * @param b the b
     * @return the string
     */
    public static String byte2binary(byte b) {
        StringBuilder sb = new StringBuilder();
        for (int i = bits - 1; i >= 0; i--) {
            sb.append(b >> i & 0x01);
        }
        return sb.toString();
    }

    /**
     * 字节数组 转 二进制组 字符串.
     *
     * @param bs the bs
     * @return the string
     */
    public static String bytes2binaries(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            sb.append(byte2binary(b));
        }
        return sb.toString();
    }

    /**
     * 二进制 转 十进制 数字.
     *
     * @param i the
     * @return the int
     */
    public static int binary2decimal(int i) {
        int decimal = 0;
        int p = 0;
        while (i != 0) {
            decimal += i % 10 * Math.pow(2, p++);
            i = i / 10;
        }
        return decimal;
    }

    /**
     * 二进制 转 十进制 数字.
     *
     * @param binary the binary
     * @return the int
     */
    public static int binary2decimal(String binary) {
        //转换为BigInteger类型
        BigInteger big = new BigInteger(binary, 2);
        //转换成十进制
        return Integer.parseInt(big.toString());
    }

    /**
     * 十进制 转 4个字节.
     *
     * @param n the n
     * @return the string
     */
    public static byte[] decimal2bytes(int n) {
        byte[] bytes = new byte[4];
        // 最低位
        bytes[0] = (byte) (n & 0xff);
        // 次低位
        bytes[1] = (byte) ((n >> 8) & 0xff);
        // 次高位
        bytes[2] = (byte) ((n >> 16) & 0xff);
        // 最高位,无符号右移。
        bytes[3] = (byte) (n >>> 24);
        return bytes;
    }

    /**
     * 十进制 转 二进制 字符串.
     *
     * @param n the n
     * @return the string
     */
    public static String decimal2binary(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = decimal - 1; i >= 0; i--) {
            sb.append(n >>> i & 1);
        }
        return sb.toString();
    }

    /**
     * 十进制 转 二进制 字符串.
     * <p>
     * 取指定长度位
     *
     * @param n   the n
     * @param len the len
     * @return the string
     */
    public static String decimal2binary(int n, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = decimal - 1; i >= 0; i--) {
            sb.append(n >>> i & 1);
        }
        String binary = sb.toString();
        return binary.substring(binary.length() - len, binary.length());
    }
}
