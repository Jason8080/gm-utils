package com.gm.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用请求实体
 *
 * @author Jason
 */
@Data
public class BaseReq implements Serializable {
    /**
     * The Request time.
     */
    Long requestTime = System.currentTimeMillis();
}
