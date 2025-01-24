package com.sstdl.mianshiya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.CommonConstant;
import com.sstdl.mianshiya.mapper.QuestionMapper;
import com.sstdl.mianshiya.model.dto.question.QuestionQueryRequest;
import com.sstdl.mianshiya.model.entity.Question;
import com.sstdl.mianshiya.model.entity.QuestionBankMapping;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.QuestionVO;
import com.sstdl.mianshiya.model.vo.UserVO;
import com.sstdl.mianshiya.service.QuestionBankMappingService;
import com.sstdl.mianshiya.service.QuestionService;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.ThrowUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author SSTDL
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-12-17 20:45:06
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    @Resource
    private QuestionBankMappingService questionBankMappingService;

    /**
     * 校验题目
     * @param question
     * @param b
     */
    @Override
    public void validQuestion(Question question, boolean b) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        String title = question.getTitle();
        String content = question.getContent();
        // 创建数据时，参数不能为空
        if (b) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content)) {
            ThrowUtils.throwIf(content.length() > 10240, ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取题目封装
     * @param question
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 获取用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        UserVO userVO = UserVO.objToVo(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    /**
     * 分页获取题目（管理员）
     * @param questionQueryRequest
     * @return
     */
    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest) {
        int pageSize = questionQueryRequest.getPageSize();
        int currentPage = questionQueryRequest.getCurrentPage();
        // 题目查询条件
        QueryWrapper<Question> queryWrapper = this.getQueryWrapper(questionQueryRequest);
        Long questionBankId = questionQueryRequest.getQuestionBankId();
        if (questionBankId != null) {
            // 查询题库内的题目
            LambdaQueryWrapper<QuestionBankMapping> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(QuestionBankMapping::getQuestionId);
            lambdaQueryWrapper.eq(QuestionBankMapping::getQuestionBankId, questionBankId);
            // 题目集合
            List<QuestionBankMapping> questionBankMappingList = questionBankMappingService.list(lambdaQueryWrapper);
            if (CollectionUtils.isNotEmpty(questionBankMappingList)) {
                Set<Long> questionIdSet = questionBankMappingList.stream().map(QuestionBankMapping::getQuestionId).collect(Collectors.toSet());
                // 服用之前的queryWrapper
                queryWrapper.in("id", questionIdSet);
            } else {
                return new Page<>(currentPage, pageSize, 0);
            }
        }
        // 查询数据库
        return this.page(new Page<>(currentPage, pageSize), queryWrapper);
    }

    /**
     * 分页获取题目列表（封装类）
     * @param questionPage
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getPages(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        List<QuestionVO> questionVOList = questionList.stream().map(QuestionVO::objToVo).collect(Collectors.toList());
        // 获取创建用户
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            UserVO userVO = UserVO.objToVo(userService.getById(userId));
            questionVO.setUserVO(userVO);
        });
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    /**
     * 根据查询请求获取查询包装器
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 获取信息
        Long id = questionQueryRequest.getId();
        Long notId = questionQueryRequest.getNotId();
        String searchText = questionQueryRequest.getSearchText();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 多字段搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 拼接查询条件
            queryWrapper.and(wrapper -> wrapper.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        // 标签查询
        queryWrapper.in(CollectionUtils.isNotEmpty(tags), "tags", tags);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
}




