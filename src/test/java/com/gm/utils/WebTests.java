package com.gm.utils;

import com.gm.utils.base.Logger;
import com.gm.utils.ext.Web;

public class WebTests {
    public static void main(String[] args) {
        String domain = Web.getDomain("http://127.0.0.1:8080/demo/one");
        int port = Web.getPort("http://127.0.0.1:8090/demo/one");
        Logger.debug(port);
    }
}
