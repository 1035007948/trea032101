package com.accounting.common;

/**
 * 业务异常类
 * 用于封装业务逻辑错误信息
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误状态码
     */
    private Integer code;

    /**
     * 构造业务异常（默认状态码500）
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造业务异常（自定义状态码）
     *
     * @param code    错误状态码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误状态码
     *
     * @return 错误状态码
     */
    public Integer getCode() {
        return code;
    }
}
