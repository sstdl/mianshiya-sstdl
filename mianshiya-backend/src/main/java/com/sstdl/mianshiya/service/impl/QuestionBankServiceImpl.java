package com.sstdl.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.CommonConstant;
import com.sstdl.mianshiya.mapper.QuestionBankMapper;
import com.sstdl.mianshiya.model.dto.questionBank.QuestionBankQueryRequest;
import com.sstdl.mianshiya.model.entity.QuestionBank;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.QuestionBankVO;
import com.sstdl.mianshiya.model.vo.UserVO;
import com.sstdl.mianshiya.service.QuestionBankService;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.SqlUtils;
import com.sstdl.mianshiya.utils.ThrowUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author SSTDL
* @description 针对表【question_bank(题库)】的数据库操作Service实现
* @createDate 2024-12-17 20:45:06
*/
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
    implements QuestionBankService{

    @Resource
    private UserService userService;

    /**
     * 校验数据
     * @param questionBank
     * @param add 对创建的数据进行校验
     */
    @Override
    public void validQuestionBank(QuestionBank questionBank, boolean add) {
        ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR);
        String title = questionBank.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     * @param questionBankQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        if (questionBankQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionBankQueryRequest.getId();
        Long notId = questionBankQueryRequest.getNotId();
        String title = questionBankQueryRequest.getTitle();
        String searchText = questionBankQueryRequest.getSearchText();
        String sortField = questionBankQueryRequest.getSortField();
        String sortOrder = questionBankQueryRequest.getSortOrder();
        Long userId = questionBankQueryRequest.getUserId();
        String description = questionBankQueryRequest.getDescription();
        String picture = questionBankQueryRequest.getPicture();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("description", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(picture), "picture", picture);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 获取题库封装
     * @param questionBank
     * @return
     */
    @Override
    public QuestionBankVO getQuestionBankVO(QuestionBank questionBank) {
        // 对象转封装类
        QuestionBankVO questionBankVO = QuestionBankVO.objToVo(questionBank);
        // 关联查询用户信息
        Long userId = questionBank.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankVO.setUserVO(userVO);

        return questionBankVO;
    }

    /**
     * 分页获取题库封装
     * @param questionBankPage
     * @return
     */
    @Override
    public Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage) {
        List<QuestionBank> questionBankList = questionBankPage.getRecords();
        Page<QuestionBankVO> questionBankVOPage = new Page<>(questionBankPage.getCurrent(), questionBankPage.getSize(), questionBankPage.getTotal());
        if (CollUtil.isEmpty(questionBankList)) {
            return questionBankVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankVO> questionBankVOList = questionBankList.stream().map(QuestionBankVO::objToVo).collect(Collectors.toList());
        // 填充信息
        questionBankVOList.forEach(questionBankVO -> {
            Long userId = questionBankVO.getUserId();
            UserVO userVO = UserVO.objToVo(userService.getById(userId));
            questionBankVO.setUserVO(userVO);
        });
        questionBankVOPage.setRecords(questionBankVOList);
        return questionBankVOPage;
    }
}
