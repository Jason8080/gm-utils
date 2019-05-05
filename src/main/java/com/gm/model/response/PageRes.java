package com.gm.model.response;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页响应实体
 *
 * @author Jason
 */
@Data
public class PageRes implements Serializable {
    /**
     * 起始页
     */
    Number pageNo = 1;
    /**
     * 页容量
     */
    Number pageSize = 10;
    /**
     * 总记录数
     */
    Number total = 0;

    public PageRes() {
    }

    public PageRes(Number pageNo, Number pageSize, Number total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }

    public static PageRes as(Page page){
        return new PageRes(page.getPageNum(), page.getPageSize(), page.getTotal());
    }

    public static PageRes as(PageInfo page){
        return new PageRes(page.getPageNum(), page.getPageSize(), page.getTotal());
    }

    public static PageRes as(Number pageNo, Number pageSize, Number total){
        return new PageRes(pageNo, pageSize, total);
    }


}
