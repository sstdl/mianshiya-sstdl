package com.sstdl.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sstdl.mianshiya.model.dto.question.QuestionQueryRequest;
import com.sstdl.mianshiya.model.entity.Question;
import com.sstdl.mianshiya.model.vo.QuestionVO;

/**
* @author SSTDL
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-12-17 20:45:06
*/
public interface QuestionService extends IService<Question> {

    // 根据题目和布尔值验证题目
    void validQuestion(Question question, boolean b);

    // 根据题目获取题目视图对象
    QuestionVO getQuestionVO(Question question);

    // 根据查询请求分页获取题目
    Page<Question> listQuestionByPage(QuestionQueryRequest queryRequest);

    // 根据查询请求获取题目查询包装器
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    // 根据题目分页获取题目视图对象分页
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage);
}
