package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码请求DTO
 * 用于修改密码接口的参数接收
 */
@Data
@ApiModel(value = "修改密码请求", description = "修改密码接口参数")
public class ChangePasswordRequest {

    /**
     * 原密码
     */
    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "原密码", required = true, example = "123456")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "新密码长度必须在6-20位之间")
    @ApiModelProperty(value = "新密码", required = true, example = "654321")
    private String newPassword;
}
