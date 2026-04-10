package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 添加收支记录请求DTO
 * 用于添加收支记录接口的参数接收
 */
@Data
@ApiModel(value = "添加记录请求", description = "添加收支记录接口参数")
public class AddRecordRequest {

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须为正数")
    @ApiModelProperty(value = "金额", required = true, example = "100.00")
    private BigDecimal amount;

    /**
     * 类型：收入/支出
     */
    @NotBlank(message = "类型不能为空")
    @Pattern(regexp = "^(收入|支出)$", message = "类型必须为收入或支出")
    @ApiModelProperty(value = "类型（收入/支出）", required = true, example = "支出")
    private String type;

    /**
     * 分类
     * 收入分类：薪资、奖金、投资收益、其他收入
     * 支出分类：餐饮、购物、交通、娱乐、医疗、教育、住房、其他支出
     */
    @NotBlank(message = "分类不能为空")
    @ApiModelProperty(value = "分类", required = true, example = "餐饮")
    private String category;

    /**
     * 备注（可选）
     */
    @ApiModelProperty(value = "备注", example = "午餐")
    private String remark;
}
