package com.gm.kit.buffer;

import java.nio.ByteBuffer;

/**
 * 缓冲区工具.
 *
 * @author ：Jason.lee
 * @version : 1.0
 */
public class Buffer {
    /**
     * 合并缓冲区.
     * <p>
     *     该合并工具是从position开始合并
     *     不会合并已读数据
     *     若希望合并已读数据请在合并前调用flip()
     * </p>
     *
     * @param buffers 多个缓冲区
     * @return 合并的缓冲区
     */
    public static ByteBuffer merge(ByteBuffer... buffers) {
        // 合并后缓冲区
        ByteBuffer all = ByteBuffer.allocate(0);
        // 逐个合并
        for (ByteBuffer buffer : buffers){
            // 重新定义大小
            all = ByteBuffer.allocate(all.limit() + buffer.limit());
            all.put(buffer);
        }
        // 合并后准备读
        all.flip();
        return all;
    }

    /**
     * 获取剩余数据.
     *
     * @param buffer the buffer
     * @return the byte buffer
     */
    public static ByteBuffer slice(ByteBuffer buffer) {
        // 切割剩余部分
        // 剩余部分维护了原数组
        // 影响.array()调用
        ByteBuffer slice = buffer.slice();
        // 重新构建缓冲区
        ByteBuffer excess = ByteBuffer.allocate(slice.limit());
        // 添加剩余数据
        excess.put(slice);
        // 准备读取
        excess.flip();
        return excess;
    }
}
