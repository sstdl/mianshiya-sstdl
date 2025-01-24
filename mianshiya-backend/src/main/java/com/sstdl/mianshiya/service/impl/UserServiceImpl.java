package com.sstdl.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.constant.CommonConstant;
import com.sstdl.mianshiya.constant.UserConstant;
import com.sstdl.mianshiya.exception.BusinessException;
import com.sstdl.mianshiya.mapper.UserMapper;
import com.sstdl.mianshiya.model.dto.user.UserQueryRequest;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.enums.UserRoleEnum;
import com.sstdl.mianshiya.model.vo.LoginUserVO;
import com.sstdl.mianshiya.model.vo.UserVO;
import com.sstdl.mianshiya.service.UserService;
import com.sstdl.mianshiya.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sstdl.mianshiya.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author WSH
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-12-17 20:45:06
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "sstdl";

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验参数
        if (StringUtils.isAnyBlank(userPassword, userAccount, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数缺失");
        }
        if (userAccount.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if (userPassword.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        // 判断密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        synchronized (userAccount.intern()) {
            // 判断账号是否重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("userAccount", userAccount);
            Long count = this.baseMapper.selectCount(queryWrapper);
            if (count != 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
            }
            // 密码加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 创建用户
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserRole(UserConstant.DEFAULT_ROLE);
            boolean save = this.save(user);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
            }
            return user.getId();
        }
    }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return this.getLoginUserVO(user);
    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        Long userId = user.getId();
        User currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
        }
        return currentUser;
    }

    /**
     * 根据User对象获取UserVO对象
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 根据User对象列表获取UserVO对象列表
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 根据UserQueryRequest对象获取QueryWrapper<User>对象
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String username = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        // 排序字段
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        // 构建查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id)
                .like(StringUtils.isNotBlank(username), "username", username)
                .like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile)
                .eq(StringUtils.isNotBlank(userRole), "userRole", userRole)
                .orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return isAdmin(user);
    }

    /**
     * 根据User对象判断用户是否为管理员
     * @param user
     * @return
     */
    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 根据User对象获取LoginUserVO对象
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }
}




