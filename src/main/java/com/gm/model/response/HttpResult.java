package com.gm.model.response;

import com.gm.utils.base.Bool;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.protocol.HTTP;

import java.io.Serializable;
import java.util.Map;

/**
 * The type Http result.
 *
 * @author Jason
 */
@Data
public class HttpResult implements Serializable {

    /**
     * The constant CHARSET.
     */
    public static final String CHARSET = "charset";

    private String url;
    private String method;
    private long innerMillis;
    private Map<String, String> reqHeaders;
    private Map<String, Object> params;
    private Header[] resHeaders;
    private Integer status;
    private byte[] result;
    private String err;
    private long outerMillis;
    private long millis;

    /**
     * 获取请求头
     *
     * @param name the name
     * @return header
     */
    public String getHeader(String name) {
        Header header = header(name);
        if (!Bool.isNull(header)) {
            return header.getValue();
        }
        return null;
    }

    /**
     * 是否json响应
     *
     * @return boolean
     */
    public boolean isJson() {
        String header = getHeader(HTTP.CONTENT_TYPE);
        return Bool.isNull(header) ? false : header.contains("json");
    }

    /**
     * 获取
     *
     * @return
     */
    private Header header(String name) {
        if (!Bool.isNull(name)) {
            for (Header header : resHeaders) {
                if (name.equalsIgnoreCase(header.getName())) {
                    return header;
                }
            }
        }
        return null;
    }
}
