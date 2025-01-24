package com.sstdl.mianshiya.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sstdl.mianshiya.annotation.AuthCheck;
import com.sstdl.mianshiya.common.BaseResponse;
import com.sstdl.mianshiya.common.DeleteRequest;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.UserConstant;
import com.sstdl.mianshiya.exception.BusinessException;
import com.sstdl.mianshiya.model.dto.question.QuestionAddRequest;
import com.sstdl.mianshiya.model.dto.question.QuestionEditRequest;
import com.sstdl.mianshiya.model.dto.question.QuestionQueryRequest;
import com.sstdl.mianshiya.model.dto.question.QuestionUpdateRequest;
import com.sstdl.mianshiya.model.entity.Question;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.QuestionVO;
import com.sstdl.mianshiya.service.QuestionService;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.ResultUtils;
import com.sstdl.mianshiya.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SSTDL
 * @description 题目接口
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    /**
     * 添加题目
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 获取用户
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 校验合法性
        questionService.validQuestion(question, true);
        // 添加题目
        boolean save = questionService.save(question);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(question.getId());
    }

    /**
     * 删除题目
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目（仅管理员可用）
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        ThrowUtils.throwIf(questionUpdateRequest == null || questionUpdateRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        questionService.validQuestion(question, false);
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目（封装类）
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question));
    }

    /**
     * 分页获取题目（管理员）
     * @param queryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> getQuestionByPage(@RequestBody QuestionQueryRequest queryRequest) {
        ThrowUtils.throwIf(queryRequest == null, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.listQuestionByPage(queryRequest);
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目列表（封装类）
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage));
    }

    /**
     * 获取当前登录用户题目列表（封装类）
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo/my")
    public BaseResponse<Page<QuestionVO>> listQuestionVOMyByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        int pageSize = questionQueryRequest.getPageSize();
        int currentPage = questionQueryRequest.getCurrentPage();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> page = questionService.page(new Page<>(currentPage, pageSize), questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(page));
    }

    /**
     * 编辑题目（给用户使用）
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionEditRequest == null, ErrorCode.PARAMS_ERROR);
        Question question = new Question();
        BeanUtil.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        questionService.validQuestion(question, false);
        // 获取用户id
        Long loginUser = userService.getLoginUser(request).getId();
        Long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 判断是否为管理员和本人
        ThrowUtils.throwIf(!oldQuestion.getUserId().equals(loginUser) && !userService.isAdmin(request), ErrorCode.NO_AUTH_ERROR);
        // 更新
        boolean res = questionService.updateById(question);
        ThrowUtils.throwIf(!res, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(true);
    }
}
