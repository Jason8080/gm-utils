package com.gm.model.response;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;

/**
 * Json响应对象
 * <p>
 * Http状态码
 *
 * @param <T> 响应对象泛型
 * @author Jason
 */
public class JsonResult<T> extends BaseRes implements Serializable {
    /**
     * 操作成功 {请求已成功，请求所希望的响应头或数据体将随此响应返回。}
     */
    public static final Integer SUCCESS = 200;
    /**
     * 操作失败 {一言难尽的异常}
     */
    public static final Integer UNSUCCESSFUL = 999;
    /**
     * 请求失败 {
     * 1、语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求。
     * 2、请求参数有误。 简而言之无法进入正常的业务处理
     * }
     */
    public static final Integer REQUEST_FAIL = 400;
    /**
     * 方法错误 {
     * 请求行中指定的请求方法不能被用于请求相应的资源。
     * 该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
     * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
     * }
     */
    public static final Integer METHOD_ERROR = 405;
    /**
     * 服务器异常 {服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。}
     */
    public static final Integer SERVERS_EXCEPTION = 500;
    /**
     * 数据库数据错误 {}
     */
    public static final Integer DATA_EXCEPTION = 602;
    /**
     * 数据状态异常 {}
     */
    public static final Integer STATUS_EXCEPTION = 603;
    /**
     * 网络异常
     */
    public static final Integer NETWORK_EXCEPTION = 604;
    /**
     * 业务断言退出 {}
     */
    public static final Integer ASSERT_EXIT = 605;
    /**
     * 不存在的 {}
     */
    public static final Integer NON_EXISTENT = 606;
    /**
     * 登陆超时或未登录 {}
     */
    public static final Integer NOT_LOGIN = 607;
    /**
     * 用户名或密码错误 {}
     */
    public static final Integer PASS_ERROR = 608;

    /**
     * 成功.
     */
    public static JsonResult<Object> SUCCESS_ = new JsonResult();
    /**
     * 总之就是失败了.
     */
    public static JsonResult<Object> FAIL_ = new JsonResult(UNSUCCESSFUL);
    /**
     * 数据异常.
     */
    public static JsonResult<Object> DATA_EXCEPTION_ = new JsonResult(DATA_EXCEPTION, "数据异常");
    /**
     * 状态异常.
     */
    public static JsonResult<Object> STATUS_EXCEPTION_ = new JsonResult(STATUS_EXCEPTION, "状态异常");
    /**
     * 网络异常.
     */
    public static JsonResult<Object> NETWORK_EXCEPTION_ = new JsonResult(NETWORK_EXCEPTION, "网络异常");
    /**
     * 服务器异常.
     */
    public static JsonResult<Object> SERVERS_EXCEPTION_ = new JsonResult(NETWORK_EXCEPTION, "服务器异常");
    /**
     * 断言退出.
     */
    public static JsonResult<Object> ASSERT_EXIT_ = new JsonResult(ASSERT_EXIT, "断言退出");
    /**
     * 登陆超时.
     */
    public static JsonResult<Object> NOT_LOGIN_ = new JsonResult(NOT_LOGIN, "登录超时或未登录");

    /**
     * 用户名或密码错误.
     */
    public static JsonResult<Object> PASS_ERROR_ = new JsonResult(PASS_ERROR, "用户名或密码错误");

    /**
     * 操作失败.
     */
    public static final String UNSUCCESSFUL_DESC = "操作失败";
    /**
     * 操作成功.
     */
    public static final String SUCCESS_DESC = "操作成功";
    private final Integer code;
    private final String msg;
    private PageRes page;
    private final T data;

    /**
     * 判断真假返回结果.
     *
     * @param b the b
     * @return the json result
     */
    public static JsonResult as(Boolean b) {
        return b != null && b ? JsonResult.SUCCESS_ : JsonResult.FAIL_;
    }


    /**
     * 实例化.
     *
     * @param <T>  the type parameter
     * @param code the code
     * @param t    the t
     * @param msg  the msg
     * @return the json result
     */
    public static <T> JsonResult<T> as(Integer code, T t, String msg) {
        return new JsonResult(code, t, msg);
    }

    /**
     * 实例化.
     *
     * @param <T>  the type parameter
     * @param code the code
     * @param t    the t
     * @return the json result
     */
    public static <T> JsonResult<T> as(Integer code, T t) {
        return new JsonResult(code, t, null);
    }


    /**
     * Successful 返回消息.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the json result
     */
    public static <T> JsonResult<T> as(T t) {
        return new JsonResult(t);
    }

    /**
     * Successful 返回消息.
     *
     * @param <T>  the type parameter
     * @param page the page
     * @param t    the t
     * @return the json result
     */
    public static <T> JsonResult<T> as(PageRes page, T t) {
        return new JsonResult(page, t);
    }

    /**
     * As json result.
     *
     * @param <T>  the type parameter
     * @param page the page
     * @return the json result
     */
    public static <T> JsonResult<T> as(Page<T> page) {
        return new JsonResult(page);
    }

    /**
     * As json result.
     *
     * @param <T>      the type parameter
     * @param pageInfo the page info
     * @return the json result
     */
    public static <T> JsonResult<T> as(PageInfo pageInfo) {
        return new JsonResult(pageInfo);
    }

    /**
     * Successful 返回消息.
     *
     * @param msg the msg
     * @return the json result
     */
    public static JsonResult successful(String msg) {
        return new JsonResult(SUCCESS, msg);
    }

    /**
     * Unsuccessful 返回消息.
     *
     * @param msg the msg
     * @return the json result
     */
    public static JsonResult unsuccessful(String msg) {
        return new JsonResult(msg);
    }

    /**
     * 操作成功
     */
    public JsonResult() {
        this(SUCCESS, null, SUCCESS_DESC);
    }

    /**
     * 操作成功
     *
     * @param page the page
     * @param t    the t
     */
    public JsonResult(PageRes page, T t) {
        this(page.getPageNo(), page.getPageSize(), page.getTotal(), t);
    }


    /**
     * 操作成功
     *
     * @param page the page
     */
    public JsonResult(Page page) {
        this(page.getPageNum(), page.getPageSize(), page.getTotal(), (T) page.getResult());
    }

    /**
     * 操作成功
     *
     * @param page the page
     */
    public JsonResult(PageInfo page) {
        this(page.getPageNum(), page.getPageSize(), page.getTotal(), (T) page.getList());
    }

    /**
     * 操作失败
     *
     * @param code the code
     */
    public JsonResult(Integer code) {
        this(code, null, UNSUCCESSFUL_DESC);
    }

    /**
     * 操作成功
     *
     * @param data the data
     */
    public JsonResult(T data) {
        this(SUCCESS, data, SUCCESS_DESC);
    }

    /**
     * 操作成功
     *
     * @param data the data
     * @param msg  the code desc
     */
    public JsonResult(T data, String msg) {
        this(SUCCESS, data, msg);
    }

    /**
     * 500异常
     *
     * @param msg the code desc
     */
    public JsonResult(String msg) {
        this(UNSUCCESSFUL, null, msg);
    }

    /**
     * 自定义响应
     *
     * @param code the code
     * @param msg  the code desc
     */
    public JsonResult(Integer code, String msg) {
        this(code, null, msg);
    }

    /**
     * 全量定义
     *
     * @param code the code
     * @param data the data
     * @param msg  the code desc
     */
    public JsonResult(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }


    /**
     * 全量定义(成功)
     *
     * @param pageNo   the page no
     * @param pageSize the page size
     * @param total    the total
     * @param t        the t
     */
    public JsonResult(Number pageNo, Number pageSize, Number total, T t) {
        this.code = SUCCESS;
        this.data = t;
        this.msg = SUCCESS_DESC;
        this.page = new PageRes(pageNo, pageSize, total);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Gets code desc.
     *
     * @return the code desc
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Gets page.
     *
     * @return the page
     */
    public PageRes getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page
     */
    public void setPage(PageRes page) {
        this.page = page;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "responseTime=" + super.responseTime +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", page=" + page +
                ", data=" + data +
                '}';
    }
}
