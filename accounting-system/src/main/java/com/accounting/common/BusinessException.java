package com.accounting.common;

/**
 * 业务异常类
 * 用于封装业务逻辑中抛出的异常
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 构造方法（默认错误码500）
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造方法（自定义错误码）
     * @param code 错误码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误码
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }
}
