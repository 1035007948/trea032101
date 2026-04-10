package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@ApiModel(description = "更新记录请求参数")
public class UpdateRecordRequest {

    @ApiModelProperty(value = "记录ID", required = true, example = "1")
    @NotNull(message = "记录ID不能为空")
    private Long id;

    @ApiModelProperty(value = "金额", required = true, example = "100.00")
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须为正数")
    private BigDecimal amount;

    @ApiModelProperty(value = "类型", required = true, allowableValues = "收入,支出", example = "支出")
    @NotBlank(message = "类型不能为空")
    @Pattern(regexp = "^(收入|支出)$", message = "类型必须为收入或支出")
    private String type;

    @ApiModelProperty(value = "分类", required = true, example = "餐饮")
    @NotBlank(message = "分类不能为空")
    private String category;

    @ApiModelProperty(value = "备注", example = "晚餐")
    private String remark;
}
