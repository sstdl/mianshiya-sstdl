package com.sstdl.mianshiya.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sstdl.mianshiya.annotation.AuthCheck;
import com.sstdl.mianshiya.common.BaseResponse;
import com.sstdl.mianshiya.common.DeleteRequest;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.UserConstant;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingAddRequest;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingQueryRequest;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingRemoveRequest;
import com.sstdl.mianshiya.model.dto.questionBankMapping.QuestionBankMappingUpdateRequest;
import com.sstdl.mianshiya.model.entity.QuestionBankMapping;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.QuestionBankMappingVO;
import com.sstdl.mianshiya.service.QuestionBankMappingService;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.ResultUtils;
import com.sstdl.mianshiya.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author SSTDL
 * @description 题目与题库关联接口
 */
@RestController
@RequestMapping("/questionBankMapping")
@Slf4j
public class QuestionBankMappingController {
    @Resource
    private QuestionBankMappingService questionBankMappingService;

    @Resource
    private UserService userService;

    /**
     * 创建题库题目关联（仅管理员可用）
     * @param questionBankMappingAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestionBankMapping(@RequestBody QuestionBankMappingAddRequest questionBankMappingAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankMappingAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 实体类和 DTO 进行转换
        QuestionBankMapping questionBankMapping = new QuestionBankMapping();
        BeanUtils.copyProperties(questionBankMappingAddRequest, questionBankMapping);
        // 数据校验
        questionBankMappingService.validQuestionBankMapping(questionBankMapping, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        questionBankMapping.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionBankMappingService.save(questionBankMapping);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBankMappingId = questionBankMapping.getId();
        return ResultUtils.success(newQuestionBankMappingId);
    }

    /**
     * 删除题库题目关联
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionBankMapping(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionBankMapping oldQuestionBankMapping = questionBankMappingService.getById(id);
        ThrowUtils.throwIf(oldQuestionBankMapping == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        ThrowUtils.throwIf(!oldQuestionBankMapping.getUserId().equals(user.getId()) && !userService.isAdmin(request),
                ErrorCode.NO_AUTH_ERROR);
        // 操作数据库
        boolean result = questionBankMappingService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题库题目关联（仅管理员可用）
     * @param questionBankMappingUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBankMapping(@RequestBody QuestionBankMappingUpdateRequest questionBankMappingUpdateRequest) {
        ThrowUtils.throwIf(questionBankMappingUpdateRequest == null || questionBankMappingUpdateRequest.getId() <= 0,
                ErrorCode.PARAMS_ERROR);
        // 实体类和 DTO 进行转换
        QuestionBankMapping questionBankMapping = new QuestionBankMapping();
        BeanUtils.copyProperties(questionBankMappingUpdateRequest, questionBankMapping);
        // 数据校验
        questionBankMappingService.validQuestionBankMapping(questionBankMapping, false);
        // 判断是否存在
        long id = questionBankMappingUpdateRequest.getId();
        QuestionBankMapping oldQuestionBankMapping = questionBankMappingService.getById(id);
        ThrowUtils.throwIf(oldQuestionBankMapping == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionBankMappingService.updateById(questionBankMapping);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题库题目关联（封装类）
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankMappingVO> getQuestionBankMappingVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QuestionBankMapping questionBankMapping = questionBankMappingService.getById(id);
        ThrowUtils.throwIf(questionBankMapping == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionBankMappingService.getQuestionBankMappingVO(questionBankMapping));
    }

    /**
     * 分页获取题库题目关联列表（仅管理员可用）
     *
     * @param questionBankMappingQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionBankMapping>> listQuestionBankMappingByPage(@RequestBody QuestionBankMappingQueryRequest questionBankMappingQueryRequest) {
        long current = questionBankMappingQueryRequest.getCurrentPage();
        long size = questionBankMappingQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBankMapping> questionBankMappingPage = questionBankMappingService.page(new Page<>(current, size),
                questionBankMappingService.getQueryWrapper(questionBankMappingQueryRequest));
        return ResultUtils.success(questionBankMappingPage);
    }

    /**
     * 分页获取题库题目关联列表（封装类）
     * @param questionBankMappingQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionBankMappingVO>> listQuestionBankMappingVOByPage(@RequestBody QuestionBankMappingQueryRequest questionBankMappingQueryRequest) {
        long current = questionBankMappingQueryRequest.getCurrentPage();
        long size = questionBankMappingQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBankMapping> questionBankMappingPage = questionBankMappingService.page(new Page<>(current, size),
                questionBankMappingService.getQueryWrapper(questionBankMappingQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBankMappingService.getQuestionBankMappingVOPage(questionBankMappingPage));
    }

    /**
     * 分页获取当前登录用户创建的题库题目关联列表
     *
     * @param questionBankMappingQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionBankMappingVO>> listMyQuestionBankMappingVOByPage(@RequestBody QuestionBankMappingQueryRequest questionBankMappingQueryRequest,
                                                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankMappingQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionBankMappingQueryRequest.setUserId(loginUser.getId());
        long current = questionBankMappingQueryRequest.getCurrentPage();
        long size = questionBankMappingQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBankMapping> questionBankMappingPage = questionBankMappingService.page(new Page<>(current, size),
                questionBankMappingService.getQueryWrapper(questionBankMappingQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBankMappingService.getQuestionBankMappingVOPage(questionBankMappingPage));
    }

    // endregion

    /**
     * 移除题库题目关联（仅管理员可用）
     *
     * @param questionBankMappingRemoveRequest
     * @return
     */
    @PostMapping("/remove")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeQuestionBankMapping(
            @RequestBody QuestionBankMappingRemoveRequest questionBankMappingRemoveRequest
    ) {
        // 参数校验
        ThrowUtils.throwIf(questionBankMappingRemoveRequest == null, ErrorCode.PARAMS_ERROR);
        Long questionBankId = questionBankMappingRemoveRequest.getQuestionBankId();
        Long questionId = questionBankMappingRemoveRequest.getQuestionId();
        ThrowUtils.throwIf(questionBankId == null || questionId == null, ErrorCode.PARAMS_ERROR);
        // 构造查询
        LambdaQueryWrapper<QuestionBankMapping> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankMapping.class)
                .eq(QuestionBankMapping::getQuestionBankId, questionBankId)
                .eq(QuestionBankMapping::getQuestionId, questionId);
        boolean result = questionBankMappingService.remove(lambdaQueryWrapper);
        return ResultUtils.success(result);
    }
}
