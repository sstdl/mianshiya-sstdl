package com.sstdl.mianshiya.model.dto.questionBankMapping;

import lombok.Data;

import java.io.Serializable;

/**
 * 移除映射请求
 */
@Data
public class QuestionBankMappingRemoveRequest implements Serializable {

    /**
     * 题目 Id
     */
    private Long questionId;

    /**
     * 题库 Id
     */
    private Long questionBankId;

    private static final long serialVersionUID = 1L;
}
