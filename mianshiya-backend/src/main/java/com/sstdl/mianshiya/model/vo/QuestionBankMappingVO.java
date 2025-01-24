package com.sstdl.mianshiya.model.vo;

import com.sstdl.mianshiya.model.entity.QuestionBankMapping;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目题库映射视图
 */
@Data
public class QuestionBankMappingVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO userVO;

    /**
     * 封装类转对象
     *
     * @param questionBankQuestionVO
     * @return
     */
    public static QuestionBankMapping voToObj(QuestionBankMappingVO questionBankQuestionVO) {
        if (questionBankQuestionVO == null) {
            return null;
        }
        QuestionBankMapping questionBankQuestion = new QuestionBankMapping();
        BeanUtils.copyProperties(questionBankQuestionVO, questionBankQuestion);
        return questionBankQuestion;
    }

    /**
     * 对象转封装类
     *
     * @param questionBankQuestion
     * @return
     */
    public static QuestionBankMappingVO objToVo(QuestionBankMapping questionBankQuestion) {
        if (questionBankQuestion == null) {
            return null;
        }
        QuestionBankMappingVO questionBankQuestionVO = new QuestionBankMappingVO();
        BeanUtils.copyProperties(questionBankQuestion, questionBankQuestionVO);
        return questionBankQuestionVO;
    }
}
