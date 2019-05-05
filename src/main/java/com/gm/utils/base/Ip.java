package com.gm.utils.base;

import com.gm.help.base.Quick;

import java.net.InetAddress;

/**
 * @author Jason
 */
public class Ip {
    /**
     * 获取本机IP(!127.0.0.1).
     *
     * @return the ip
     */
    public static String getIp() {
        return Quick.exec(x -> {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        });
    }
}
