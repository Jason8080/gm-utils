package com.gm.utils.ext;

import com.gm.utils.Utils;
import com.gm.utils.base.*;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Collection;
import java.util.regex.Pattern;


/**
 * Web工具类.
 *
 * @author Jason
 */
public class Web implements Utils {


    /**
     * ip正则.
     */
    public static Pattern ipPattern = Pattern.compile("(\\d{1,3}\\.)+\\d{1,3}");

    /**
     * url正则.
     */
    public static Pattern urlPattern = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\" +
            ".[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#\\[\\]]*[\\w\\-\\@?^=%&amp;/~\\+#])?");


    private static final String unknown = "unknown";


    /**
     * GET url 传参标识符
     */
    public static final String PARAM_SPLIT_CODE = "\\?";

    /**
     * The constant PARAM_START_CODE.
     */
    public static final String PARAM_START_CODE = "?";

    /**
     * URL参数连接符.
     */
    public static final String PARAM_SPLICE_CODE = "&";

    /**
     * URL参数赋值符.
     */
    public static final String PARAM_WITH_CODE = "=";


    /**
     * 获取请求字符集.
     *
     * @param req the req
     * @return the charset
     */
    public static String getCharset(HttpServletRequest req) {
        return req.getCharacterEncoding();
    }

    /**
     * 获取请求字符集.
     *
     * @param req the req
     * @return the charset
     */
    public static String getCharset(HttpRequestBase req) {
        Header header = req.getFirstHeader(HTTP.CONTENT_TYPE);
        if (!Bool.isNull(header)) {
            HeaderElement[] elements = header.getElements();
            for (HeaderElement e : elements) {
                NameValuePair[] pairs = e.getParameters();
                for (NameValuePair p : pairs) {
                    if ("charset".equalsIgnoreCase(p.getName())) {
                        return p.getValue();
                    }
                }
            }
        }
        return "UTF-8";
    }


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param req the req
     * @return ip ip
     */
    public static String getIp(HttpServletRequest req) {
        String ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求者端口.
     *
     * @param req the req
     * @return the port
     */
    public static int port(HttpServletRequest req) {
        //这个是请求者的端口号并非请求端口号
        return req.getRemotePort();
    }


    /**
     * Gets 请求端口.
     *
     * @param req the req
     * @return the req port
     */
    public static int getPort(HttpServletRequest req) {
        return req.getServerPort();
    }

    /**
     * Gets 请求的全路径.
     *
     * @param req the req
     * @return the path
     */
    public static String getPath(HttpServletRequest req) {
        return req.getRequestURL().toString();
    }

    /**
     * Gets 根全路径.
     *
     * @param req the req
     * @return the root path
     */
    public static String getRootPath(HttpServletRequest req) {
        return getBase(req) + getDomain(req) + ":" + getPort(req) + getRoot(req);
    }


    /**
     * Gets 相对路径.
     *
     * @param req the req
     * @return the rel path
     */
    public static String getRelPath(HttpServletRequest req) {
        return req.getServletPath();
    }

    /**
     * 获取请求主机.
     *
     * @param req the req
     * @return the host
     */
    public static String getHost(HttpServletRequest req) {
        return req.getRemoteHost();
    }

    /**
     * 获取项目根路径(/项目名).
     *
     * @param req the req
     * @return the host
     */
    public static String getRoot(HttpServletRequest req) {
        return req.getContextPath();
    }


    /**
     * 获取访问根路径(包含域名).
     *
     * @param request the request
     * @return the base
     */
    public static String getBase(HttpServletRequest request) {
        String base;
        String proto = request.getHeader("x-forwarded-orign-proto");
        if (proto == null) {
            proto = request.getHeader("x-forwarded-proto");
        }
        String host = request.getHeader("x-forwarded-host");
        String prefix = request.getHeader("x-forwarded-prefix");
        if (Bool.isNull(proto, host, prefix)) {
            String url = request.getRequestURL().toString();
            String[] split = url.split("/");
            base = split[0] + "//" + split[1];
        } else {
            base = proto + "://" + host + prefix;
        }
        return base;
    }


    /**
     * 获取域名.
     *
     * @param request the request
     * @return the domain
     */
    public static String getDomain(HttpServletRequest request) {
        String host = request.getHeader("x-forwarded-host");
        if (Bool.isNull(host)) {
            host = request.getServerName();
        }
        return host;
    }

    /**
     * 根据路径获取域名.
     *
     * @param url the url
     * @return the domain
     */
    public static String getDomain(String url) {
        Assert.Null(url, "地址是空");
        String[] split = url.split("/");
        if (split.length > 2) {
            url = split[2];
        }
        split = url.split(":");
        if (split.length > 0) {
            return split[0];
        }
        return url;
    }

    /**
     * 根据路径获取域名.
     *
     * @param url the url
     * @return the domain
     */
    public static int getPort(String url) {
        Assert.Null(url, "地址是空");
        String[] split = url.split("/");
        if (split.length > 2) {
            url = split[2];
        }
        split = url.split(":");
        if (split.length > 1) {
            return Integer.valueOf(split[1]);
        } else {
            return 80;
        }
    }

    /**
     * 获取根域名.
     *
     * @param request the request
     * @return the root domain
     */
    public static String getRootDomain(HttpServletRequest request) {
        String host = request.getHeader("x-forwarded-host");
        if (Bool.isNull(host)) {
            host = request.getServerName();
        }
        return getRootDomain(host);
    }

    /**
     * 获取根域名.
     * (注意这里没有考虑.com.cn)
     *
     * @param url the url
     * @return the root domain
     */
    public static String getRootDomain(String url) {
        String domain = getDomain(url);
        if (!Bool.isNull(domain) && !ipPattern.matcher(domain).matches() && !"localhost".equals
                (domain)) {
            String[] split = domain.split("\\.");
            int len = split.length;
            return split[len - 2] + "." + split[len - 1];
        } else {
            return domain;
        }
    }

    /**
     * 获取层级路径 (依赖最后1个/).
     *
     * @param url the url
     * @return the layer
     */
    public static String getLayer(String url) {
        int index = url.lastIndexOf("/");
        String domain = getDomain(url);
        if (domain.length() <= index) {
            return url.substring(0, index);
        }
        return url;
    }

    /**
     * 获取指定cookie
     *
     * @param name    名称
     * @param request the request
     * @return cookie值 cookie
     */
    public static String getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


    /**
     * 获取URL指定参数
     *
     * @param url 请求
     * @return 参数集 params
     */
    public static Map<String, Object> getParams(String url) {
        Map<String, Object> map = new HashMap(0);
        if (!Bool.isNull(url)) {
            String[] split = url.split(PARAM_SPLIT_CODE);
            if (split.length > 1) {
                String[] params = split[1].split(PARAM_SPLICE_CODE);
                for (String str : params) {
                    String[] kv = str.split(PARAM_WITH_CODE);
                    Object val = kv.length > 1 ? kv[1] : "";
                    if (map.containsKey(kv[0])) {
                        Object values = map.get(kv[0]);
                        if (values instanceof Collection) {
                            ((Collection) values).add(val);
                        } else if (Bool.isNull(values)) {
                            map.put(kv[0], Arrays.asList(val));
                        }
                    } else {
                        map.put(kv[0], val);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取URL指定参数
     *
     * @param url   请求
     * @param param 参数名
     * @return 参数值 value
     */
    public static Object getParam(String url, String param) {
        Map<String, Object> params = getParams(url);
        return params.get(param);
    }

    /**
     * 获取指定参数(优先取用请求头)
     *
     * @param param   参数名
     * @param request 请求
     * @return 参数值 param
     */
    public static String getParam(String param, HttpServletRequest request) {
        String value = request.getHeader(param);
        if (Bool.isNull(value)) {
            value = request.getParameter(param);
        }
        return value;
    }


    /**
     * 获取指定参数(包括cookie)
     *
     * @param param   参数名
     * @param request 请求
     * @return 参数值 parameter
     */
    public static String getParameter(String param, HttpServletRequest request) {
        String value = getParam(param, request);
        if (Bool.isNull(value)) {
            value = getCookie(param, request);
        }
        return value;
    }

    /**
     * 获取无参url.
     *
     * @param url the url
     * @return the string
     */
    public static String nonArgs(String url) {
        return url.split(PARAM_SPLIT_CODE)[0];
    }


    /**
     * 获取url参数
     *
     * @param url
     * @return
     */
    private static String args(String url) {
        String replace = url.replace(nonArgs(url), "");
        if (replace.endsWith(PARAM_START_CODE)) {
            return replace.substring(0, replace.length() - 1);
        }
        return replace;
    }

    /**
     * 删除参数.
     *
     * @param url  the url
     * @param keys the keys
     * @return the string
     */
    public static String delParam(String url, String... keys) {
        Map<String, Object> all = getParams(url);
        com.gm.utils.base.Collection.removeKeys(all, keys);
        url = nonArgs(url);
        return addParam(url, all);
    }

    /**
     * 替换1个参数.
     *
     * @param url   the url
     * @param key   the key
     * @param value the value
     * @return the string
     */
    public static String replace(String url, String key, Object value) {
        Map<String, Object> all = getParams(url);
        all.put(key, value);
        url = nonArgs(url);
        return addParam(url, all);
    }

    /**
     * 替换1批参数.
     *
     * @param url the url
     * @param map the map
     * @return the string
     */
    public static String replace(String url, Map<String, Object> map) {
        Map<String, Object> all = getParams(url);
        all.putAll(map);
        url = nonArgs(url);
        return addParam(url, all);
    }

    /**
     * 添加1批参数.
     *
     * @param url    the url
     * @param params the params
     * @return 全路径
     */
    public static String addParam(String url, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(Convert.toEmpty(url));
        if (!Bool.haveNull(url, params)) {
            sb.append(url.contains(PARAM_START_CODE) ? PARAM_SPLICE_CODE : PARAM_START_CODE);
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> next = it.next();
                String key = next.getKey();
                Object value = next.getValue();
                String str = addParam(sb.toString(), key, value.toString());
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 添加1个多值参数.
     *
     * @param url   the url
     * @param key   the key
     * @param value the value
     * @return 全路径
     */
    public static String addParam(String url, String key, Object value) {
        StringBuilder sb = new StringBuilder(url);
        if (!Bool.haveNull(key, value)) {
            if (value instanceof Object[]) {
                for (Object val : (Object[]) value) {
                    sb.append(addParam(sb.toString(), key, val.toString()));
                }
            } else if (value instanceof Collection) {
                for (Object val : (Collection) value) {
                    sb.append(addParam(sb.toString(), key, val.toString()));
                }
            } else {
                sb.append(addParam(sb.toString(), key, value.toString()));
            }
        }
        return sb.toString();
    }

    /**
     * 添加1个参数.
     *
     * @param url   the url
     * @param key   the key
     * @param value the value
     * @return 参数部分
     */
    public static String addParam(String url, String key, String value) {
        url = Convert.toEmpty(url);
        StringBuilder sb = new StringBuilder();
        if (!Bool.haveNull(key, value)) {
            if (url.endsWith(PARAM_START_CODE)) {
                sb.append(key);
                sb.append(PARAM_WITH_CODE);
                sb.append(value);
            } else if (url.contains(PARAM_START_CODE)) {
                sb.append(PARAM_SPLICE_CODE);
                sb.append(key);
                sb.append(PARAM_WITH_CODE);
                sb.append(value);
            } else {
                sb.append(PARAM_START_CODE);
                sb.append(key);
                sb.append(PARAM_WITH_CODE);
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 获取multipart/form-data数据
     *
     * @param req the req
     * @return files files
     */
    public static Map<String, File> getFiles(HttpServletRequest req) {
        if (req instanceof MultipartHttpServletRequest) {
            return getFiles((MultipartHttpServletRequest) req);
        } else {
            Map<String, File> map = new HashMap(0);
            String path = req.getServletContext().getRealPath(File.separator);
            try {
                Collection<Part> parts = req.getParts();
                for (Part p : parts) {
                    File file = getFile(path, p);
                    if (!Bool.isNull(file)) {
                        map.put(p.getName(), file);
                    }
                }
            } catch (Exception e) {
                Logger.error(e);
            }
            return map;
        }
    }

    private static Map<String, File> getFiles(MultipartHttpServletRequest req) {
        Map<String, File> map = new HashMap(0);
        String path = req.getServletContext().getRealPath(File.separator);
        Map<String, MultipartFile> fileMap = req.getFileMap();
        Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
        ok:
        while (it.hasNext()) {
            Map.Entry<String, MultipartFile> next = it.next();
            MultipartFile multipartFile = next.getValue();
            File file = new File(path + File.separator + Generator.uuid() + multipartFile
                    .getOriginalFilename());
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                break ok;
            }
            if (file.isFile()) {
                map.put(next.getKey(), file);
            }
        }
        return map;
    }


    /**
     * 取得上传文件名
     */
    private static File getFile(String path, Part part) {
        if (!Bool.isNull(part.getSubmittedFileName())) {
            String header = part.getHeader("Content-Disposition");
            String name = header.substring(header.indexOf("filename=\"") + 10,
                    header.lastIndexOf("\""));
            File file = new File(path + File.separator + name);
            if (file.isFile()) {
                return file;
            }
        }
        return null;
    }


    /**
     * 获取请求头.
     *
     * @param req the req
     * @return the header map
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest req) {
        Map<String, String> reqHeaders = new HashMap(0);
        Enumeration<String> names = req.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            reqHeaders.put(name, req.getHeader(name));
        }
        return reqHeaders;
    }


    /**
     * 获取参数集合.
     *
     * @param req the req
     * @return the parameter map
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest req) {
        Map<String, Object> params = new HashMap(req.getParameterMap());
        Enumeration<String> headers = req.getHeaders(HTTP.CONTENT_TYPE);
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (Bool.isNull(header) && !header.contains("json")) {
                return params;
            }
        }
        String json;
        try {
            ServletInputStream in = req.getInputStream();
            json = Stream.toString(in);
        } catch (IOException e) {
            return ExceptionUtils.cast(e);
        }
        if (!Bool.isNull(json)) {
            Map<String, Object> map = Json.toObject(json, Map.class);
            if (!Bool.isNull(map)) {
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> next = it.next();
                    Object value = next.getValue();
                    if (!Bool.isNull(value)) {
                        params.put(next.getKey(), new String[]{value.toString()});
                    }
                }
            }
        }
        params.putAll(Web.getFiles(req));
        return params;
    }
}
