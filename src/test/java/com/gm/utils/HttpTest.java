package com.gm.utils;

import com.gm.model.response.HttpResult;
import com.gm.utils.base.Logger;
import com.gm.utils.third.Http;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HttpTest {
    @Test
    public void asyncDemoHttpsTest() throws InterruptedException, NoSuchAlgorithmException, KeyManagementException, IOException {
        SimpleHttpAsyncClientDemo.send("https://192.168.1.223:8787/demo/one?msg=666", null, "UTF-8", new SimpleHttpAsyncClientDemo.IHandler() {
            @Override
            public Object failed(Exception e) {
                return null;
            }

            @Override
            public Object completed(String respBody) {
                return null;
            }

            @Override
            public Object cancelled() {
                return null;
            }
        });
        Thread.sleep(3000);
    }
    @Test
    public void asyncHttpsTest() throws InterruptedException {
        Http.asyncGet("https://192.168.1.223:8787/demo/one?msg=666", new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse response) {
                Logger.info(new String(Http.getHttpResult(response).getResult()));
            }

            @Override
            public void failed(Exception e) {
                Logger.info(new String(Http.getHttpResult(e).getResult()));
            }

            @Override
            public void cancelled() {
                Logger.info("取消了~");
            }
        });
        Thread.sleep(3000);
    }
    @Test
    public void httpsTest() {
//        HttpResult result = Http.doGet("https://www.baidu.com");
        HttpResult result = Http.doGet("https://192.168.1.223:8989/foo/demo/demo/one?msg=666");
//        HttpResult result = Http.doGet("https://192.168.1.223:8989/foo/demo/demo/one");
        Logger.info(new String(result.getResult()));
    }
}
