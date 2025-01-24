package com.sstdl.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sstdl.mianshiya.model.dto.questionBank.QuestionBankQueryRequest;
import com.sstdl.mianshiya.model.entity.QuestionBank;
import com.sstdl.mianshiya.model.vo.QuestionBankVO;

/**
* @author SSTDL
* @description 针对表【question_bank(题库)】的数据库操作Service
* @createDate 2024-12-17 20:45:06
*/
public interface QuestionBankService extends IService<QuestionBank> {

    // 验证题库
    void validQuestionBank(QuestionBank questionBank, boolean b);

    // 获取题库VO
    QuestionBankVO getQuestionBankVO(QuestionBank questionBank);

    // 获取查询包装器
    Wrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest);

    // 获取题库VO分页
    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage);
}
