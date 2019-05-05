package com.gm.utils.third;

import com.gm.help.base.Quick;
import com.gm.model.response.HttpResult;
import com.gm.model.response.JsonResult;
import com.gm.utils.Utils;
import com.gm.utils.base.*;
import com.gm.utils.base.Collection;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Web;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;

/**
 * The type Netty utils.
 *
 * @author Jason
 */
public class Netty implements Utils {

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

    /**
     * route.
     *
     * @param req      the req
     * @param url      the url
     * @param callback the callback
     */
    public static void route(HttpRequest req, String url, Callback callback) {

        Assert.Null(url);
        String base = Web.getDomain(url) + Web.getPort(url);
        // 设置服务器地址
        req.headers().add(HttpHeaderNames.HOST, base);
        // 设置URI地址
        String[] split = url.split(base);
        if (split.length > 1) {
            req.setUri(split[1]);
        } else {
            req.setUri("/");
        }
        Channel channel = getChannelFuture(req, null, null, callback);
        // 发起请求
        start(req, channel);
    }


    /**
     * Get.
     *
     * @param <T>      the type parameter
     * @param url      the url
     * @param header   the header
     * @param params   the params
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static <T> void doGet(String url, Map<String, String> header, Map<String, Object> params, Callback<T> callback, Cookie... cookies) {
        // 设置请求参数
        url = Web.addParam(url, params);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, url);
        // 设置请求头
        addHeader(header, request, cookies);
        // 建立通道
        Channel channel = getChannelFuture(request, header, params, callback);
        // 发起请求
        start(request, channel);
    }


    /**
     * Post.
     *
     * @param <T>      the type parameter
     * @param url      the url
     * @param header   the header
     * @param params   the params
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static <T> void doPost(String url, Map<String, String> header, Map<String, Object> params, Callback<T> callback, Cookie... cookies) {
        // 设置请求参数
        DefaultHttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, url);
        // 设置请求头
        addHeader(header, request, cookies);
        // 设置请求参数
        HttpPostRequestEncoder encoder = Quick.exec(x -> addParameter(params, request));
        // 建立通道
        Channel channel = getChannelFuture(request, header, params, callback);
        // 发起请求
        start(request, encoder, channel);
    }

    private static void start(HttpRequest request, HttpPostRequestEncoder encoder, Channel channel) {

        channel.write(request);
        if (encoder.isChunked()) {
            channel.write(encoder).awaitUninterruptibly();
        }
        channel.flush();
        encoder.cleanFiles();
        channel.closeFuture().awaitUninterruptibly();
    }

    private static void start(HttpRequest request, Channel channel) {

        channel.writeAndFlush(request);
        channel.closeFuture().awaitUninterruptibly();
    }

    /**
     * Get.
     *
     * @param <T>      the type parameter
     * @param url      the url
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static <T> void doGet(String url, Callback<T> callback, Cookie... cookies) {

        doGet(url, null, null, callback, cookies);
    }

    /**
     * Post.
     *
     * @param <T>      the type parameter
     * @param url      the url
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static <T> void doPost(String url, Callback<T> callback, Cookie... cookies) {

        doPost(url, null, null, callback, cookies);
    }

    private static void addHeader(Map<String, String> header, HttpRequest request, Cookie[] cookies) {

        if (!Bool.isNull(header)) {
            Iterator<Map.Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> next = it.next();
                request.headers().add(next.getKey(), next.getValue());
            }
        }
        if (!Bool.isNull(cookies)) {
            List<String> collect = Arrays.stream(cookies).map(
                    c -> c.getName() + "=" + c.getValue()).collect(Collectors.toList()
            );
            request.headers().add(COOKIE, Collection.toArrString(collect, ";"));
        }
    }


    private static HttpPostRequestEncoder addParameter(Map<String, Object> params, HttpRequest request) throws Exception {
        // FIXME true时的多媒体类型参数无法传递
        HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(request, true);
        if (!Bool.isNull(params)) {
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> next = it.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (value instanceof String[]) {
                    for (String val : (String[]) value) {
                        encoder.addBodyAttribute(key, val);
                    }
                } else if (value instanceof File) {
                    encoder.addBodyFileUpload(key, (File) value, "application/x-zip-compressed", false);
                }
            }
        }
        encoder.finalizeRequest();
        return encoder;
    }

    /**
     * Get cookies cookie [ ].
     *
     * @param req the req
     * @return the cookie [ ]
     */
    public static Cookie[] getCookies(HttpRequest req) {

        List<Cookie> cookies = new ArrayList();
        String val = req.headers().get(COOKIE);
        String[] split = Convert.toEmpty(val).split(";");
        for (String nv : split) {
            String[] obj = nv.split("=");
            if (!Bool.isNull(obj) && obj.length > 1) {
                cookies.add(new Cookie(obj[0].trim(), obj[1].trim()));
            }
        }
        return cookies.toArray(new Cookie[]{});
    }

    private static Channel getChannelFuture(HttpRequest req, Map<String, String> header, Map<String, Object> params, Callback callback) {

        try {
            String domain = Web.getDomain(req.uri());
            int port = Web.getPort(req.uri());
            // 连接HTTP服务器
            ChannelFuture sync = bootstrap.connect(domain, port).sync();
            Channel channel = sync.awaitUninterruptibly().channel();
            channel.pipeline().addLast(new HttpClientInboundHandler(req.uri()
                    , req.method().name()
                    , header
                    , params
                    , Assert.Null(callback)));
            return channel;
        } catch (InterruptedException e) {
            return ExceptionUtils.cast(e);
        }
    }


    // ================================== 3 8 分 割 线 =================================

    /**
     * 需要被继承
     *
     * @param <T> the type parameter
     */
    public interface Callback<T> {

        /**
         * 回调响应处理器
         *
         * @param result the result
         */
        void callback(T result);
    }

    /**
     * Netty internal handler.
     *
     * @param <T> the type parameter
     */
    public static class HttpClientInboundHandler<T> extends ChannelInboundHandlerAdapter {

        private final String url;

        private final String method;

        private final Map<String, String> header;

        private final Map<String, Object> params;

        private final Callback<T> callback;

        /**
         * Instantiates a new Http client inbound handler.
         *
         * @param url      the url
         * @param method   the method
         * @param header   the header
         * @param params   the params
         * @param callback the callback
         */
        public HttpClientInboundHandler(String url, String method, Map<String, String> header, Map<String, Object> params, Callback<T> callback) {

            this.url = url;
            this.method = method;
            this.header = header;
            this.params = params;
            this.callback = callback;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

            HttpResult result = getHttpResult(msg);
            result.setUrl(url);
            result.setMethod(method);
            result.setReqHeaders(header);
            result.setParams(params);
            Class aClass = ClassUtils.getGenericInterfaces(callback);
            if (msg instanceof HttpResponse && HttpResponse.class.equals(aClass)) {
                callback.callback((T) msg);
            } else if (HttpResult.class.equals(aClass)) {
                callback.callback((T) result);
            } else {
                Quick.run(x -> callback.callback((T) result));
            }
        }
    }

    /**
     * get netty async result.
     *
     * @param msg the Obj
     * @return the http result
     */
    public static HttpResult getHttpResult(Object msg) {

        HttpResult result = new HttpResult();
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            result.setStatus(response.status().code());
            HttpHeaders headers = response.headers();
            Header[] array = new Header[headers.size()];

            int count = 0;
            for (String name : headers.names()) {
                String value = headers.get(name);
                array[count++] = new BasicHeader(name, value);
            }


            result.setResHeaders(array);
            if (response instanceof FullHttpResponse) {
                FullHttpResponse full = (FullHttpResponse) response;
                Quick.run(x -> {
                    ByteBuf content = full.content();
                    result.setResult(content.toString(Charset.defaultCharset()).getBytes());
                });
            }
            return result;
        } else if (msg instanceof HttpContent) {
            result.setStatus(JsonResult.SUCCESS);
            HttpContent hc = (HttpContent) msg;
            Quick.run(x -> {
                ByteBuf content = hc.content();
                result.setResult(content.toString(Charset.defaultCharset()).getBytes());
            });
            return result;
        } else {
            result.setStatus(JsonResult.REQUEST_FAIL);
            return result;
        }
    }

    /**
     * 获取 NETTY 请求头.
     *
     * @param req the req
     * @return the header map
     */
    public static Map<String, String> getHeaderMap(HttpRequest req) {

        Map<String, String> map = new HashMap(0);
        for (Map.Entry<String, String> entry : req.headers()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 解析 NETTY 请求参数
     *
     * @param request the request
     * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
     */
    public static Map<String, Object> getParameterMap(HttpRequest request) {

        Map<String, Object> map = new HashMap(0);
        if (request instanceof HttpRequest) {
            if (HttpMethod.GET.equals(request.method())) {
                // 是GET请求
                QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
                decoderQuery.parameters().entrySet().forEach(entry -> {
                    map.put(entry.getKey(), entry.getValue().toArray(new String[0]));
                });
            }
            // 处理POST请求
            if (HttpMethod.POST.equals(request.method())) {
                String contentType = request.headers().get(HTTP.CONTENT_TYPE);
                if (!Bool.isNull(contentType) && contentType.contains("json")) {
                    ByteBuf jsonBuf = ((FullHttpRequest) request).content();
                    Map<String, Object> toMap = Json.toMap(jsonBuf.toString(CharsetUtil.UTF_8));
                    Iterator<Map.Entry<String, Object>> it = toMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, Object> next = it.next();
                        if (!Bool.haveNull(next.getKey(), next.getValue())) {
                            map.put(next.getKey(), new String[]{next.getValue().toString()});
                        }
                    }
                } else {
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
                    for (InterfaceHttpData data : postData) {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            Quick.run(x -> map.put(attribute.getName(), new String[]{attribute.getValue()}));
                        } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                            FileUpload attribute = (FileUpload) data;
                            if (attribute.isCompleted()) {
                                File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "download" + File.separator);
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File dest = new File(file, attribute.getFilename());
                                try {
                                    attribute.renameTo(dest);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                map.put(attribute.getName(), dest);
                            }
                        }
                    }
                }
            }
        } else if (request instanceof HttpContent) {
            HttpContent chunk = (HttpContent) request;
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            decoder.offer(chunk);
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        /**
                         * HttpDataType有三种类型
                         * Attribute, FileUpload, InternalAttribute
                         */
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            map.put(attribute.getName(), new String[]{attribute.getValue()});
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        data.release();
                    }
                }
            }
        }
        return map;
    }
}
