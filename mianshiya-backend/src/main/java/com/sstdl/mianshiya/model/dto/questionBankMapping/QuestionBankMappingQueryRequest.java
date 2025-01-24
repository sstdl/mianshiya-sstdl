package com.sstdl.mianshiya.model.dto.questionBankMapping;

import com.sstdl.mianshiya.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询映射请求
 */
@Data
public class QuestionBankMappingQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 题目 Id
     */
    private Long questionId;

    /**
     * 题库 Id
     */
    private Long questionBankId;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
