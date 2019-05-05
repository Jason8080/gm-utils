package com.gm.utils;

import com.gm.help.base.Quick;
import com.gm.model.response.HttpResult;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Web;
import com.gm.utils.third.Netty3;
import com.gm.utils.third.Netty;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NettyTests {
    @Test
    public void netty4Tests() {
        String url = "http://192.168.1.223:8787/demo/seven";
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo", new String[]{"2"});
        map.put("file", new File("C:\\Users\\Administrator\\Desktop\\common-utils\\pom.xml"));
        Netty.doPost(url,null, map, (HttpResult result)->Logger.info(new String(result.getResult())));
        Quick.run(x->Thread.sleep(1000000));
    }
    @Test
    public void netty3Tests() {
        String url = "http://192.168.1.223:8787/demo/seven";
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo", new String[]{"2"});
        map.put("file", new File("C:\\Users\\Administrator\\Desktop\\common-utils\\pom.xml"));
        Netty3.doPost(url,null, map, (HttpResult result)->Logger.info(new String(result.getResult())));
        Quick.run(x->Thread.sleep(1000000));
    }

    @Test
    public void doGetTests() {
        String url = "http://localhost:8787/demo/one?msg=666";
        Netty.doGet(url, (HttpResult result)->Logger.debug(result));
    }

    /**
     * 客户端程序
     */
    private static Bootstrap bootstrap = new Bootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                }
            });

    @Test
    public void test() throws Exception {
        String url = "http://127.0.0.1:9090/one?msg=666";
        String host = Web.getDomain(url);
        int port = Web.getPort(url);
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, url);
        request.headers().add("host",host+":"+port);
        HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(request, true);
//        encoder.addBodyFileUpload(
//                "file"
//                , new File("C:\\Users\\Administrator\\Desktop\\common-utils\\pom.xml")
//                , "application/x-zip-compressed"
//                , false);
//        request = encoder.finalizeRequest();
        // 连接HTTP服务器
        ChannelFuture sync = bootstrap.connect(host, port).sync();
        sync.channel().writeAndFlush(request);
        Quick.run(x->Thread.sleep(10000));
    }
}
