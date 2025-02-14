package com.sstdl.mianshiya.common;

import com.sstdl.mianshiya.constant.CommonConstant;
import lombok.Data;

/**
 * @author SSTDL
 * @description 分页请求
 */
@Data
public class PageRequest {
    /**
     * 当前页码
     */
    private int currentPage = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 默认升序排序
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

}
