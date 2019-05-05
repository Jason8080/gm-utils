package com.gm.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求通用实体
 *
 * @author Jason
 */
@Data
public class PageReq implements Serializable {
    /**
     * 起始页
     */
    Integer pageNo = 1;
    /**
     * 页容量
     */
    Integer pageSize = 10;
}
