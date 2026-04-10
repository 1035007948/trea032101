package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 */
@Data
@ApiModel("登录请求")
public class LoginRequest {
    
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true, example = "13800138000")
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
