package com.sstdl.mianshiya.utils;

import com.sstdl.mianshiya.common.ErrorCode;
import com.sstdl.mianshiya.exception.BusinessException;

/**
 * @author SSTDL
 * @description 抛异常工具类
 */
public class ThrowUtils {
    /**
     * 条件成立 抛异常
     * @param condition
     * @param e
     */
    public static void throwIf(boolean condition, RuntimeException e) {
        if (condition) throw e;
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
