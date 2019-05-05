package com.gm.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求实体
 *
 * @author Jason
 */
@Data
public class BasePageReq extends BaseReq implements Serializable {
    /**
     * 起始页
     */
    Integer pageNo = 1;
    /**
     * 页容量
     */
    Integer pageSize = 10;
}
