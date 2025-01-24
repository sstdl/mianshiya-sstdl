package com.sstdl.mianshiya.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sstdl.mianshiya.model.entity.QuestionBank;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题库视图
 */
@Data
public class QuestionBankVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO userVO;

    /**
     * 题库中的题目列表（分页展示）
     */
    private Page<QuestionVO> questionVOPage;

    /**
     * 封装类 -> 实体对象
     * @param questionBankVo
     * @return
     */
    public static QuestionBank voToObj(QuestionBankVO questionBankVo) {
        if (questionBankVo == null) return null;
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(questionBankVo, questionBank);
        return questionBank;
    }

    /**
     * 实体对象 -> 封装类
     * @param questionBank
     * @return
     */
    public static QuestionBankVO objToVo(QuestionBank questionBank) {
        if (questionBank == null) return null;
        QuestionBankVO questionBankVo = new QuestionBankVO();
        BeanUtils.copyProperties(questionBank, questionBankVo);
        return questionBankVo;
    }
}
