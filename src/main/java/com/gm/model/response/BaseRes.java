package com.gm.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用响应实体
 *
 * @author Jason
 */
@Data
public class BaseRes implements Serializable {

    /**
     * The Response time.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date responseTime = new Date();
}
