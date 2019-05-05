package com.gm.ex;

import com.gm.model.response.JsonResult;

/**
 * 携带Json响应结果对象的异常
 *
 * @author Jason
 */
public class JsonBusinessException extends BusinessException {

    private JsonResult jsonResult;

    /**
     * Gets json result.
     *
     * @return the json result
     */
    public JsonResult getJsonResult() {
        return jsonResult;
    }

    /**
     * Sets json result.
     *
     * @param jsonResult the json result
     */
    public void setJsonResult(JsonResult jsonResult) {
        this.jsonResult = jsonResult;
    }

    /**
     * Instantiates a new Json business exception.
     *
     * @param obj the obj
     */
    public JsonBusinessException(Object obj) {
        super(obj instanceof JsonResult ? ((JsonResult) obj).getMsg() : obj.toString());
        if (obj instanceof JsonResult) {
            this.jsonResult = (JsonResult) obj;
        }
    }

    /**
     * Instantiates a new Json business exception.
     *
     * @param message the message
     */
    public JsonBusinessException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Json business exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public JsonBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Json business exception.
     *
     * @param cause the cause
     */
    public JsonBusinessException(Throwable cause) {
        super(cause);
    }
}
