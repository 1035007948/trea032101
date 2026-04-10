package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 * 用于用户登录接口的参数接收
 */
@Data
@ApiModel(value = "登录请求", description = "用户登录接口参数")
public class LoginRequest {

    /**
     * 账号（手机号或邮箱）
     */
    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号（手机号或邮箱）", required = true, example = "13800138000")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
}
