package com.sstdl.mianshiya.model.dto.questionBankMapping;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量添加映射请求
 */
@Data
public class QuestionBankMappingBatchAddRequest implements Serializable {

    /**
     * 题目 Id
     */
    private List<Long> questionIdList;

    /**
     * 题库 Id
     */
    private Long questionBankId;

    private static final long serialVersionUID = 1L;
}
