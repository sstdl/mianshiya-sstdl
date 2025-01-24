package com.sstdl.mianshiya.interceptor;

import com.sstdl.mianshiya.annotation.AuthCheck;
import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.exception.BusinessException;
import com.sstdl.mianshiya.model.entity.User;
import com.sstdl.mianshiya.model.enums.UserRoleEnum;
import com.sstdl.mianshiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author SSTDL
 * @description 管理员校验
 */
@Aspect
@Component
@Slf4j
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param proceedingJoinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint, AuthCheck authCheck) throws Throwable {
        String mustRule = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRuleEnum = UserRoleEnum.getEnumByValue(mustRule);
        // 不需要校验
        if (mustRuleEnum == null) {
            return proceedingJoinPoint.proceed();
        }
        // 校验用户角色
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户角色异常");
        }
        // 用户被封禁
        if (userRoleEnum.equals(UserRoleEnum.BAN)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该用户已被封禁");
        }
        // 必须有管理员权限
        if (mustRuleEnum.equals(UserRoleEnum.ADMIN)) {
            log.info("用户角色为：{}",userRoleEnum.getValue());
            // 用户没有管理员权限，直接拒绝
            if (!userRoleEnum.equals(UserRoleEnum.ADMIN)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该用户没有管理员权限");
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
