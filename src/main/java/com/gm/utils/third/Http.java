package com.gm.utils.third;

import com.gm.model.response.HttpResult;
import com.gm.utils.Utils;
import com.gm.utils.base.Bool;
import com.gm.utils.base.Convert;
import com.gm.utils.base.ExceptionUtils;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Resource;
import com.gm.utils.ext.Web;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 模拟Http请求工具类
 *
 * @author Jason
 */
public class Http implements Utils {

    /**
     * 响应成功
     * 200~300
     */
    private static int SUCCESS_MIN = 200;

    private static int SUCCESS_MAX = 300;

    public static final String HTTPS = "https";
    public static final String HTTP_ = "http";

    /**
     * 加载自由配置.
     */
    public static final RequestConfig CONFIG = RequestConfig.custom()
            //连接的超时时间
            .setConnectTimeout(Resource.getInt("gm.http.utils.connect.timeout", 5000))
            //从连接池获取连接的超时时间
            .setConnectionRequestTimeout(Resource.getInt("gm.http.utils.connect.request.timeout",
                    1000))
            //请求数据的超时时间
            .setSocketTimeout(Resource.getInt("gm.http.utils.connect.socket.timeout", 5000))
            .build();

    /**
     * ssl 连接配置.
     */
    public static final SSLConnectionSocketFactory SSL_CONTEXT = ssl();

    /**
     * Get http result.
     *
     * @param response the response
     * @return the http result
     */
    public static HttpResult getHttpResult(HttpResponse response) {
        HttpResult result = new HttpResult();
        result.setStatus(response.getStatusLine().getStatusCode());
        result.setResHeaders(response.getAllHeaders());
        HttpEntity entity = response.getEntity();
        try {
            result.setResult(Bool.isNull(entity) ? null : EntityUtils.toByteArray(entity));
        } finally {
            return result;
        }
    }

    /**
     * Get http result.
     *
     * @param e the e
     * @return the http result
     */
    public static HttpResult getHttpResult(Exception e) {
        HttpResult result = new HttpResult();
        result.setErr(e.getMessage());
        result.setResHeaders(new Header[0]);
        result.setStatus(500);
        result.setResult(Convert.toEmpty(ExceptionUtils.getMsg(e)).getBytes());
        result.setResult(ExceptionUtils.getMsg(e).getBytes());
        return result;
    }


    /**
     * asyncRoute.
     *
     * @param req      the req
     * @param url      the url
     * @param callback the callback
     */
    public static void asyncRoute(HttpServletRequest req, String url,
                                  FutureCallback<HttpResponse> callback) {
        // 1. 获取方法
        String method = req.getMethod();
        // 2. 封装请求头
        Map<String, String> reqHeaders = Web.getHeaderMap(req);
        // 3. 获取请求参数
        Map<String, Object> parameterMap = Web.getParameterMap(req);
        // 4. 调用服务接口
        way(url, method, reqHeaders, parameterMap, callback, req.getCookies());
    }


    /**
     * asyncRoute.
     *
     * @param method       the method
     * @param headerMap    the header map
     * @param parameterMap the parameter map
     * @param url          the url
     * @param callback     the callback
     * @param cookies      the cookies
     */
    public static void asyncRoute(String method, Map<String, String> headerMap, Map<String,
            Object> parameterMap, String url, FutureCallback<HttpResponse> callback, Cookie...
                                          cookies) {
        way(url, method, headerMap, parameterMap, callback, cookies);
    }


    /**
     * Route.
     *
     * @param req the req
     * @param res the res
     * @param url the url
     * @return the http result
     * @throws IOException the io exception
     */
    public static HttpResult route(HttpServletRequest req, HttpServletResponse res, String url)
            throws IOException {
        long start = System.currentTimeMillis();
        // 1. 获取方法
        String method = req.getMethod();
        // 2. 封装请求头
        Map<String, String> reqHeaders = Web.getHeaderMap(req);
        // 3. 获取请求参数
        Map<String, Object> parameterMap = Web.getParameterMap(req);
        // 4. 调用服务接口
        long outerStart = System.currentTimeMillis();
        HttpResult result = way(url, method, reqHeaders, parameterMap, req.getCookies());
        long outerEnd = System.currentTimeMillis();
        result.setReqHeaders(reqHeaders);
        result.setParams(parameterMap);
        // 5. 封装响应头
        Header[] resHeaders = result.getResHeaders();
        for (Header header : resHeaders) {
            res.setHeader(header.getName(), header.getValue());
        }
        res.setStatus(result.getStatus());
        // 6. 写入相应内容
        ServletOutputStream out = res.getOutputStream();
        out.write(result.getResult());
        out.flush();
        out.close();
        // 7. 记录时间消耗
        long end = System.currentTimeMillis();
        result.setMillis(end - start);
        result.setOuterMillis(outerEnd - outerStart);
        result.setInnerMillis(result.getMillis() - result.getOuterMillis());
        return result;
    }

    /**
     * 发起指定方式的请求
     *
     * @param url          the url
     * @param method       the method
     * @param reqHeaders   the req headers
     * @param parameterMap the parameter map
     * @param cookies      the cookies
     * @return http result
     */
    private static HttpResult way(String url, String method, Map<String, String> reqHeaders,
                                  Map<String, Object> parameterMap, Cookie... cookies) {
        switch (method) {
            case "GET":
                return doGet(url, reqHeaders, parameterMap, cookies);
            case "POST":
                return doPost(url, reqHeaders, parameterMap, cookies);
            case "PUT":
                return unity(new HttpPut(url), reqHeaders, parameterMap, cookies);
            case "HEAD":
                return unity(new HttpHead(url), reqHeaders, parameterMap, cookies);
            case "PATCH":
                return unity(new HttpPatch(url), reqHeaders, parameterMap, cookies);
            case "TRACE":
                return unity(new HttpTrace(url), reqHeaders, parameterMap, cookies);
            case "DELETE":
                return unity(new HttpDelete(url), reqHeaders, parameterMap, cookies);
            case "OPTIONS":
                return unity(new HttpOptions(url), reqHeaders, parameterMap, cookies);
            default:
                return doGet(url, reqHeaders, parameterMap, cookies);
        }
    }

    /**
     * 发起指定方式的请求
     *
     * @param url          the url
     * @param method       the method
     * @param reqHeaders   the req headers
     * @param parameterMap the parameter map
     * @param callback     the callback
     * @param cookies      the cookies
     */
    private static void way(String url, String method, Map<String, String> reqHeaders,
                            Map<String, Object> parameterMap, FutureCallback<HttpResponse>
                                    callback, Cookie... cookies) {
        switch (method) {
            case "GET":
                asyncGet(url, reqHeaders, parameterMap, callback, cookies);
                break;
            case "POST":
                asyncPost(url, reqHeaders, parameterMap, callback, cookies);
                break;
            case "PUT":
                unity(new HttpPut(url), reqHeaders, parameterMap, callback, cookies);
                break;
            case "HEAD":
                unity(new HttpHead(url), reqHeaders, parameterMap, callback, cookies);
                break;
            case "PATCH":
                unity(new HttpPatch(url), reqHeaders, parameterMap, callback, cookies);
                break;
            case "TRACE":
                unity(new HttpTrace(url), reqHeaders, parameterMap, callback, cookies);
                break;
            case "DELETE":
                unity(new HttpDelete(url), reqHeaders, parameterMap, callback, cookies);
                break;
            case "OPTIONS":
                unity(new HttpOptions(url), reqHeaders, parameterMap, callback, cookies);
                break;
            default:
                asyncGet(url, reqHeaders, parameterMap, callback, cookies);
        }
    }

    private static HttpResult unity(HttpRequestBase request, Map<String, String> reqHeaders,
                                    Map<String, Object> parameterMap, Cookie... cookies) {
        //加载配置
        request.setConfig(CONFIG);
        //添加请求头
        addHeader(reqHeaders, request);
        //设置请求内容
        addParameter(parameterMap, request);
        //调用请求
        return execute(request, cookies);
    }

    private static void unity(HttpRequestBase request, Map<String, String> reqHeaders,
                              Map<String, Object> parameterMap, FutureCallback<HttpResponse>
                                      callback, Cookie... cookies) {
        //加载配置
        request.setConfig(CONFIG);
        //添加请求头
        addHeader(reqHeaders, request);
        //设置请求内容
        addParameter(parameterMap, request);
        //发起异步请求
        execute(request, callback, cookies);
    }

    /**
     * 调用异步GET请求.
     *
     * @param url      the url
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static void asyncGet(String url, FutureCallback<HttpResponse> callback, Cookie...
            cookies) {
        //创建GET请求
        HttpGet get = new HttpGet(url);
        //发起异步请求
        execute(get, callback, cookies);
    }


    /**
     * 调用Get请求.
     *
     * @param url      the url
     * @param header   the header
     * @param params   the params
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static void asyncGet(String url, Map<String, String> header, Map<String, Object>
            params, FutureCallback<HttpResponse> callback, Cookie... cookies) {
        url = Web.addParam(url, params);
        //创建GET请求
        HttpGet get = new HttpGet(url);
        //加载配置
        get.setConfig(CONFIG);
        //设置请求头
        addHeader(header, get);
        //发起异步请求
        execute(get, callback, cookies);
    }

    /**
     * 调用GET请求.
     *
     * @param url     the url
     * @param cookies the cookies
     * @return the t
     */
    public static HttpResult doGet(String url, Cookie... cookies) {
        //创建GET请求
        HttpGet get = new HttpGet(url);
        return execute(get, cookies);
    }


    /**
     * 调用Get请求.
     *
     * @param url     the url
     * @param header  the header
     * @param params  the params
     * @param cookies the cookies
     * @return the http result
     */
    public static HttpResult doGet(String url, Map<String, String> header, Map<String, Object>
            params, Cookie... cookies) {
        url = Web.addParam(url, params);
        //创建GET请求
        HttpGet get = new HttpGet(url);
        //加载配置
        get.setConfig(CONFIG);
        //设置请求头
        addHeader(header, get);
        return execute(get, cookies);
    }

    /**
     * 调用GET请求
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param tClass the t class
     * @return t t
     */
    public static <T> T get(String url, Class<T> tClass) {
        //创建GET请求
        HttpGet get = new HttpGet(url);
        return execute(tClass, get);
    }

    /**
     * 调用GET请求
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param params the params
     * @param tClass the t class
     * @return t t
     */
    public static <T> T get(String url, Map<String, Object> params, Class<T> tClass) {
        url = Web.addParam(url, params);
        //创建GET请求
        HttpGet get = new HttpGet(url);
        return execute(tClass, get);
    }

    /**
     * Get t.
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param header the header
     * @param params the params
     * @param tClass the t class
     * @return the t
     */
    public static <T> T get(String url, Map<String, String> header, Map<String, Object> params,
                            Class<T> tClass) {
        url = Web.addParam(url, params);
        //创建GET请求
        HttpGet get = new HttpGet(url);
        addHeader(header, get);
        return execute(tClass, get);

    }

    /**
     * 执行并封装响应实体
     *
     * @param <T>
     * @param tClass
     * @param request
     * @return
     */
    private static <T> T execute(Class<T> tClass, HttpRequestBase request) {
        //创建请求工具
        CloseableHttpClient client = getClient(request);
        try {
            CloseableHttpResponse response = client.execute(request);
            HttpEntity responseEntity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            //关闭响应通道
            String json = EntityUtils.toString(responseEntity);
            response.close();
            if (status >= SUCCESS_MIN && status < SUCCESS_MAX) {
                return Json.toObject(json, tClass);
            }
            Logger.debug("调用HTTP请求{}失败;响应是{}" + status + ":", request.getURI().toString(), json);
            return Json.toObject(json, tClass);
        } catch (Exception e) {
            Logger.debug("调用同步请求{}失败", request.getURI().toString(), e);
        } finally {
            try {//关闭请求通道
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 执行并封装响应实体
     *
     * @param request
     * @return
     */
    private static HttpResult execute(HttpRequestBase request, Cookie... cookies) {
        //创建请求工具
        CloseableHttpClient client = getClient(request, cookies);
        HttpResult result = new HttpResult();
        result.setUrl(request.getURI().toString());
        result.setMethod(request.getMethod());
        try {
            CloseableHttpResponse response = client.execute(request);
            result.setStatus(response.getStatusLine().getStatusCode());
            result.setResHeaders(response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            result.setResult(Bool.isNull(entity) ? null : EntityUtils.toByteArray(entity));
            response.close();
            return result;
        } catch (Exception e) {
            result.setErr(e.getMessage());
            result.setResHeaders(new Header[0]);
            result.setStatus(500);
            result.setResult(Convert.toEmpty(ExceptionUtils.getMsg(e)).getBytes());
            Logger.debug("调用同步请求{}失败", request.getURI().toString(), e);
        } finally {
            try {//关闭请求通道
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发起异步请求
     *
     * @param request
     */
    private static void execute(HttpRequestBase request, FutureCallback<HttpResponse> callback,
                                Cookie... cookies) {
        //创建请求工具
        CloseableHttpAsyncClient client = getAsyncClient(request, cookies);
        client.start();
        client.execute(request, callback);
    }

    private static CookieStore getStore(HttpRequestBase req, Cookie[] cookies) {
        BasicCookieStore store = new BasicCookieStore();
        if (!Bool.isNull(cookies)) {
            for (Cookie c : cookies) {
                if (!Bool.isNull(c.getName(), c.getValue())) {
                    BasicClientCookie cookie = new BasicClientCookie(c.getName(), c.getValue());
                    cookie.setVersion(0);
                    cookie.setDomain(req.getURI().getHost());
                    cookie.setPath("/");
                    store.addCookie(cookie);
                }
            }
        }
        return store;
    }

    /**
     * 调用POST请求
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param params the params
     * @param tClass the t class
     * @return t t
     */
    public static <T> T post(String url, Object params, Class<T> tClass) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //设置内容类型
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        //设置请求内容
        setEntity(params, post);
        return execute(tClass, post);
    }

    private static void setEntity(Object params, HttpEntityEnclosingRequestBase post) {
        if (params instanceof Map) {
            Iterator<Map.Entry<String, Object>> it = ((Map<String, Object>) params).entrySet()
                    .iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> next = it.next();
                Object value = next.getValue();
                if (value instanceof String[]) {
                    StringBuilder sb = new StringBuilder();
                    String[] array = (String[]) value;
                    for (int i = 0; i < array.length; i++) {
                        sb.append(array[i]);
                        if (i != array.length - 1) {
                            sb.append(",");
                        }
                    }
                    ((Map) params).put(next.getKey(), sb.toString());
                }
            }
        }
        //设置参数编码
        StringEntity requestEntity = new StringEntity(Json.toJson(params), Web.getCharset(post));
        requestEntity.setContentEncoding(Web.getCharset(post));
        //设置请求内容
        post.setEntity(requestEntity);
    }

    /**
     * Post t.
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param header the header
     * @param params the params
     * @param tClass the t class
     * @return the t
     */
    public static <T> T post(String url, Map<String, String> header, Object params, Class<T>
            tClass) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //设置内容类型
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        //添加请求头
        addHeader(header, post);
        //设置请求内容
        setEntity(params, post);
        return execute(tClass, post);
    }

    /**
     * 原生post请求工具
     *
     * @param url     the url
     * @param header  the header
     * @param params  the params
     * @param cookies the cookies
     * @return the http result
     */
    public static HttpResult doPost(String url, Map<String, String> header, Map<String, Object>
            params, Cookie... cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //加载配置
        post.setConfig(CONFIG);
        //添加请求头
        addHeader(header, post);
        //设置请求内容
        addParameter(params, post);
        //调用请求
        return execute(post, cookies);
    }

    /**
     * 异步POST请求工具
     *
     * @param url      the url
     * @param header   the header
     * @param params   the params
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static void asyncPost(String url, Map<String, String> header, Map<String, Object>
            params, FutureCallback<HttpResponse> callback, Cookie... cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //加载配置
        post.setConfig(CONFIG);
        //添加请求头
        addHeader(header, post);
        //设置请求内容
        addParameter(params, post);
        //发起异步请求
        execute(post, callback, cookies);
    }

    /**
     * 原生post请求工具
     *
     * @param url     the url
     * @param params  the params
     * @param cookies the cookies
     * @return http result
     */
    public static HttpResult doPost(String url, Map<String, Object> params, Cookie... cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //设置请求内容
        addParameter(params, post);
        //调用请求
        return execute(post, cookies);
    }


    /**
     * 异步POST请求工具
     *
     * @param url      the url
     * @param params   the params
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static void asyncPost(String url, Map<String, Object> params,
                                 FutureCallback<HttpResponse> callback, Cookie... cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //设置请求内容
        addParameter(params, post);
        //发起异步请求
        execute(post, callback, cookies);
    }

    private static void addParameter(Map<String, Object> params, HttpRequestBase post) {
        Header header = post.getFirstHeader(HTTP.CONTENT_TYPE);
        if (!(post instanceof HttpEntityEnclosingRequestBase)) {
            return;
        } else if (!Bool.isNull(header) && header.getValue().contains("json")) {
            setEntity(params, (HttpEntityEnclosingRequestBase) post);
        } else if (!Bool.isNull(header) && header.getValue().contains("form-data")) {
            post.removeHeader(header);
            setForm(params, (HttpEntityEnclosingRequestBase) post);
        } else {
            addParams(params, (HttpEntityEnclosingRequestBase) post);
        }
    }

    /**
     * 原生post请求工具
     *
     * @param url     the url
     * @param cookies the cookies
     * @return http result
     */
    public static HttpResult doPost(String url, Cookie... cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //调用请求
        return execute(post, cookies);
    }

    /**
     * 异步POST请求工具
     *
     * @param url      the url
     * @param callback the callback
     * @param cookies  the cookies
     */
    public static void asyncPost(String url, FutureCallback<HttpResponse> callback, Cookie...
            cookies) {
        //创建POST请求
        HttpPost post = new HttpPost(url);
        //发起异步请求
        execute(post, callback, cookies);
    }

    private static void setForm(Map<String, Object> params, HttpEntityEnclosingRequestBase post) {
        if (!Bool.isNull(params)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName(Web.getCharset(post)));
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> next = it.next();
                Object value = next.getValue();
                if (value instanceof String[]) {
                    for (String val : (String[]) value) {
                        builder.addTextBody(next.getKey(), val);
                    }
                } else if (value instanceof File) {
                    builder.addBinaryBody(next.getKey(), (File) value);
                }
            }
            post.setEntity(builder.build());
        }
    }


    private static void addParams(Map<String, Object> params, HttpEntityEnclosingRequestBase post) {
        if (!Bool.isNull(params)) {
            List<BasicNameValuePair> pairs = new ArrayList();
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> next = it.next();
                Object value = next.getValue();
                if (value instanceof String[]) {
                    for (String val : (String[]) value) {
                        pairs.add(new BasicNameValuePair(next.getKey(), val));
                    }
                }
            }
            try {
                UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(pairs);
                //设置参数编码
                requestEntity.setContentEncoding("UTF-8");
                //设置请求内容
                post.setEntity(requestEntity);
            } catch (Exception e) {
                ExceptionUtils.cast(e);
            }
        }
    }

    private static void addHeader(Map<String, String> header, HttpRequestBase http) {
        if (!Bool.isNull(header)) {
            Iterator<Map.Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> next = it.next();
                String key = next.getKey();
                if (HTTP.CONTENT_LEN.equalsIgnoreCase(key)) {
                    continue;
                }
                String value = next.getValue();
                http.addHeader(key, value);
            }
        }
    }


    private static CloseableHttpClient getClient(HttpRequestBase req, Cookie... cookies) {
        HttpClientBuilder custom = HttpClients.custom();
        String url = req.getURI().toString();
        if (!Bool.isNull(url) && url.startsWith(HTTPS)) {
            custom.setSSLSocketFactory(SSL_CONTEXT);
        }
        if (!Bool.isNull(cookies)) {
            custom.setDefaultCookieStore(getStore(req, cookies));
        }
        return custom.build();
    }


    private static CloseableHttpAsyncClient getAsyncClient(HttpRequestBase req, Cookie... cookies) {
        HttpAsyncClientBuilder custom = HttpAsyncClients.custom();
        String url = req.getURI().toString();
        if (!Bool.isNull(url) && url.startsWith(HTTPS)) {
            custom.setConnectionManager(getPoolingNHttpClientConnectionManager());
        }
        if (!Bool.isNull(cookies)) {
            custom.setDefaultCookieStore(getStore(req, cookies));
        }
        return custom.build();
    }

    private static SSLConnectionSocketFactory ssl() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null
                    , new TrustStrategy() {

                        // 信任所有
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) {
                            return true;
                        }
                    }
            );
            return new SSLConnectionSocketFactory(builder.build()
                    , new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}
                    , null
                    , NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            Logger.debug("SSL路由信任失败!", e);
            return null;
        }
    }

    private static SSLContext getSSLContext() {
        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType)
                -> true;
        try {
            final SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            sslContext.getServerSessionContext().setSessionCacheSize(1000);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            Logger.debug("SSL路由信任失败!", e);
            return null;
        }
    }

    private static Registry<SchemeIOSessionStrategy> getSSLRegistryAsync() {
        return RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(
                        getSSLContext()
                        , null
                        , null
                        , SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
                .build();
    }

    private static PoolingNHttpClientConnectionManager getPoolingNHttpClientConnectionManager() {
        try {
            final PoolingNHttpClientConnectionManager connectionManager =
                    new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor
                            (IOReactorConfig.DEFAULT), getSSLRegistryAsync());
            return connectionManager;
        } catch (IOReactorException e) {
            return null;
        }
    }
}
