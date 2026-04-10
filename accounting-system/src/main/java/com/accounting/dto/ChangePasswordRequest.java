package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码请求DTO
 */
@Data
@ApiModel("修改密码请求")
public class ChangePasswordRequest {
    
    /**
     * 原密码
     */
    @ApiModelProperty(value = "原密码", required = true, example = "123456")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码（6-20位）", required = true, example = "654321")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "新密码长度必须在6-20位之间")
    private String newPassword;
}
