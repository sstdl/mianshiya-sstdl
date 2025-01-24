package com.sstdl.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sstdl.mianshiya.model.dto.user.UserQueryRequest;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.vo.LoginUserVO;
import com.sstdl.mianshiya.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author SSTDL
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-12-17 20:45:06
*/
public interface UserService extends IService<User> {

    // 用户注册
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    // 用户登录
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    // 获取当前登录的用户
    User getLoginUser(HttpServletRequest request);

    // 根据User对象获取LoginUserVO对象
    LoginUserVO getLoginUserVO(User user);

    // 判断用户是否注销
    boolean userLogout(HttpServletRequest request);

    // 根据User对象获取UserVO对象
    UserVO getUserVO(User user);

    // 根据User对象列表获取UserVO对象列表
    List<UserVO> getUserVO(List<User> userList);

    // 根据UserQueryRequest对象获取QueryWrapper<User>对象
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    // 根据HttpServletRequest对象判断用户是否为管理员
    boolean isAdmin(HttpServletRequest request);

    // 根据User对象判断用户是否为管理员
    boolean isAdmin(User user);
}
