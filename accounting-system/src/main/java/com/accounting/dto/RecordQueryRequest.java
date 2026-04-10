package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "记录查询请求参数")
public class RecordQueryRequest {

    @ApiModelProperty(value = "开始日期", example = "2024-01-01")
    private String startDate;

    @ApiModelProperty(value = "结束日期", example = "2024-12-31")
    private String endDate;

    @ApiModelProperty(value = "类型", allowableValues = "收入,支出", example = "支出")
    private String type;

    @ApiModelProperty(value = "分类", example = "餐饮")
    private String category;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "页大小", example = "10")
    private Integer pageSize = 10;
}
