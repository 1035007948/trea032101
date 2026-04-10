package com.accounting.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "收支记录实体")
public class Record {

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "金额", example = "100.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "类型", allowableValues = "收入,支出", example = "支出")
    private String type;

    @ApiModelProperty(value = "分类", example = "餐饮")
    private String category;

    @ApiModelProperty(value = "备注", example = "午餐")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount.setScale(0, RoundingMode.HALF_UP);
        } else {
            this.amount = amount;
        }
    }
}
