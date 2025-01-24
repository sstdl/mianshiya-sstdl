package com.sstdl.mianshiya.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量删除
 */
@Data
public class QuestionBatchDeleteRequest implements Serializable {
    /**
     * 题目 Id 列表
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}
