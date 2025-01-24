package com.sstdl.mianshiya.model.dto.questionBank;

import com.sstdl.mianshiya.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询题库请求
 */
@Data
public class QuestionBankQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 不包含此 Id
     */
    private Long notId;

    /**
     * 标题
     */
    private String title;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 是否要关联查询题目列表
     */
    private boolean needQueryQuestionList;

    private static final long serialVersionUID = 1L;
}