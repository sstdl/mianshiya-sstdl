package com.sstdl.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingQueryRequest;
import com.sstdl.mianshiya.model.entity.QuestionBankMapping;
import com.sstdl.mianshiya.model.vo.QuestionBankMappingVO;

/**
* @author SSTDL
* @description 针对表【question_bank_mapping(题库题目)】的数据库操作Service
* @createDate 2024-12-17 20:45:06
*/
public interface QuestionBankMappingService extends IService<QuestionBankMapping> {

    // 验证题库映射
    void validQuestionBankMapping(QuestionBankMapping questionBankMapping, boolean b);

    // 获取题库映射VO
    QuestionBankMappingVO getQuestionBankMappingVO(QuestionBankMapping questionBankMapping);

    // 获取查询包装器
    Wrapper<QuestionBankMapping> getQueryWrapper(QuestionBankMappingQueryRequest questionBankMappingQueryRequest);

    // 获取题库映射VO分页
    Page<QuestionBankMappingVO> getQuestionBankMappingVOPage(Page<QuestionBankMapping> questionBankMappingPage);
}
