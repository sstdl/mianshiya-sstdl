package com.sstdl.mianshiya.utils;

import com.sstdl.mianshiya.common.BaseResponse;
import com.sstdl.mianshiya.common.ErrorCode;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author SSTDL
 * @description 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    /**
     * 失败
     * @param code
     * @param msg
     * @return
     */
    public static BaseResponse<T> error(int code, String msg) {
        return new BaseResponse<>(code, null, msg);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse<T> error(ErrorCode errorCode,String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
