package com.accounting.common;

import lombok.Data;

/**
 * 统一响应结果类
 * 用于封装API接口的返回结果
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 提示信息
     */
    private String msg;
    
    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回成功结果（无数据）
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 返回成功结果（带数据）
     * @param data 返回的数据
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    /**
     * 返回错误结果（带状态码）
     * @param code 错误码
     * @param msg 错误信息
     * @param <T> 数据类型
     * @return 错误结果
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回错误结果（默认500错误码）
     * @param msg 错误信息
     * @param <T> 数据类型
     * @return 错误结果
     */
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }
}
