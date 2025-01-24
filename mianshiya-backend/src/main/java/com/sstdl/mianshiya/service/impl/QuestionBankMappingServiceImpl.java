package com.sstdl.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.CommonConstant;
import com.sstdl.mianshiya.mapper.QuestionBankMappingMapper;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingQueryRequest;
import com.sstdl.mianshiya.model.entity.Question;
import com.sstdl.mianshiya.model.entity.QuestionBank;
import com.sstdl.mianshiya.model.entity.QuestionBankMapping;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.QuestionBankMappingVO;
import com.sstdl.mianshiya.model.vo.UserVO;
import com.sstdl.mianshiya.service.QuestionBankMappingService;
import com.sstdl.mianshiya.service.QuestionBankService;
import com.sstdl.mianshiya.service.QuestionService;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.SqlUtils;
import com.sstdl.mianshiya.utils.ThrowUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author SSTDL
* @description 针对表【question_bank_mapping(题库题目)】的数据库操作Service实现
* @createDate 2024-12-17 20:45:06
*/
@Service
public class QuestionBankMappingServiceImpl extends ServiceImpl<QuestionBankMappingMapper, QuestionBankMapping>
    implements QuestionBankMappingService {
    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionService questionService;

    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     * @param questionBankMapping
     * @param add
     */
    @Override
    public void validQuestionBankMapping(QuestionBankMapping questionBankMapping, boolean add) {
        ThrowUtils.throwIf(questionBankMapping == null, ErrorCode.PARAMS_ERROR);
        // 题目和题库必须存在
        Long questionId = questionBankMapping.getQuestionId();
        if (questionId != null) {
            Question question = questionService.getById(questionId);
            ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        Long questionBankId = questionBankMapping.getQuestionBankId();
        if (questionBankId != null) {
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        }
    }

    /**
     * 获取查询条件
     * @param questionBankMappingQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankMapping> getQueryWrapper(QuestionBankMappingQueryRequest questionBankMappingQueryRequest) {
        QueryWrapper<QuestionBankMapping> queryWrapper = new QueryWrapper<>();
        if (questionBankMappingQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionBankMappingQueryRequest.getId();
        Long notId = questionBankMappingQueryRequest.getNotId();
        String sortField = questionBankMappingQueryRequest.getSortField();
        String sortOrder = questionBankMappingQueryRequest.getSortOrder();
        Long questionBankId = questionBankMappingQueryRequest.getQuestionBankId();
        Long questionId = questionBankMappingQueryRequest.getQuestionId();
        Long userId = questionBankMappingQueryRequest.getUserId();
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题库题目关联封装
     * @param questionBankMapping
     * @return
     */
    @Override
    public QuestionBankMappingVO getQuestionBankMappingVO(QuestionBankMapping questionBankMapping) {
        // 对象转封装类
        QuestionBankMappingVO questionBankMappingVO = QuestionBankMappingVO.objToVo(questionBankMapping);
        // 关联查询用户信息
        Long userId = questionBankMapping.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankMappingVO.setUserVO(userVO);
        return questionBankMappingVO;
    }

    /**
     * 分页获取题库题目关联封装
     * @param questionBankMappingPage
     * @return
     */
    @Override
    public Page<QuestionBankMappingVO> getQuestionBankMappingVOPage(Page<QuestionBankMapping> questionBankMappingPage) {
        List<QuestionBankMapping> questionBankMappingList = questionBankMappingPage.getRecords();
        Page<QuestionBankMappingVO> questionBankMappingVOPage = new Page<>(questionBankMappingPage.getCurrent(), questionBankMappingPage.getSize(), questionBankMappingPage.getTotal());
        if (CollUtil.isEmpty(questionBankMappingList)) {
            return questionBankMappingVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankMappingVO> questionBankMappingVOList = questionBankMappingList.stream().map(QuestionBankMappingVO::objToVo).collect(Collectors.toList());
        // 填充信息
        questionBankMappingVOList.forEach(questionBankMappingVO -> {
            Long userId = questionBankMappingVO.getUserId();
            UserVO userVO = UserVO.objToVo(userService.getById(userId));
            questionBankMappingVO.setUserVO(userVO);
        });
        questionBankMappingVOPage.setRecords(questionBankMappingVOList);
        return questionBankMappingVOPage;
    }

}




