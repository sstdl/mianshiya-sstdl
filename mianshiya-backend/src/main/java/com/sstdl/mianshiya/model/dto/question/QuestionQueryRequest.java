package com.sstdl.mianshiya.model.dto.question;

import com.sstdl.mianshiya.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询题目请求
 */
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * Id
     */
    private Long id;

    /**
     * 不包含此 Id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 推荐答案
     */
    private String answer;

    /**
     * 题库 Id
     */
    private Long questionBankId;

    /**
     * 创建用户 Id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}