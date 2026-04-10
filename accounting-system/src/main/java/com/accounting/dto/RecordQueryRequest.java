package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 记录查询请求DTO
 * 用于查询收支记录列表接口的参数接收
 */
@Data
@ApiModel(value = "记录查询请求", description = "查询收支记录列表接口参数")
public class RecordQueryRequest {

    /**
     * 开始日期（格式：yyyy-MM-dd）
     */
    @ApiModelProperty(value = "开始日期（格式：yyyy-MM-dd）", example = "2024-01-01")
    private String startDate;

    /**
     * 结束日期（格式：yyyy-MM-dd）
     */
    @ApiModelProperty(value = "结束日期（格式：yyyy-MM-dd）", example = "2024-12-31")
    private String endDate;

    /**
     * 类型：收入/支出
     */
    @ApiModelProperty(value = "类型（收入/支出）", example = "支出")
    private String type;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类", example = "餐饮")
    private String category;

    /**
     * 页码（从1开始，默认为1）
     */
    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    /**
     * 每页条数（默认为10）
     */
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;
}
