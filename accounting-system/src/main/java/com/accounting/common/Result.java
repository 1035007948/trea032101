package com.accounting.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一响应结果封装类
 * 用于封装API接口的返回结果
 *
 * @param <T> 数据类型
 */
@Data
@ApiModel(value = "统一响应结果", description = "API接口统一返回格式")
public class Result<T> {

    /**
     * 响应状态码
     * 200表示成功，其他表示失败
     */
    @ApiModelProperty(value = "响应状态码", example = "200")
    private Integer code;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", example = "成功")
    private String msg;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 创建成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 创建成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    /**
     * 创建错误响应（自定义状态码和消息）
     *
     * @param code 错误状态码
     * @param msg  错误消息
     * @param <T>  数据类型
     * @return 错误响应对象
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 创建错误响应（默认状态码500）
     *
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }
}
