package com.gm.ex;

import com.gm.model.response.JsonResult;

/**
 * 登陆异常
 *
 * @author Jason
 */
public class LoginException extends JsonBusinessException {

    /**
     * 登陆超时或未登录异常.
     */
    public LoginException() {
        super(JsonResult.NOT_LOGIN_);
    }

    /**
     * 登陆失败异常.
     *
     * @param obj the obj 异常代码
     */
    public LoginException(JsonResult obj) {
        super(obj);
    }
}
