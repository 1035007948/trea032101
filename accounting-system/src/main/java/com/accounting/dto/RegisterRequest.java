package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注册请求DTO
 * 用于用户注册接口的参数接收
 */
@Data
@ApiModel(value = "注册请求", description = "用户注册接口参数")
public class RegisterRequest {

    /**
     * 账号（手机号或邮箱）
     */
    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^(1[3-9]\\d{9})|([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "账号格式不正确，请输入手机号或邮箱")
    @ApiModelProperty(value = "账号（手机号或邮箱）", required = true, example = "13800138000")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度必须在6-20位之间")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    /**
     * 昵称（可选，默认为账号）
     */
    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;
}
