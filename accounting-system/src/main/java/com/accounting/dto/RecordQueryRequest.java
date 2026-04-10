package com.accounting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 记账记录查询请求DTO
 */
@Data
@ApiModel("记账记录查询请求")
public class RecordQueryRequest {
    
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期（格式：yyyy-MM-dd）", example = "2024-01-01")
    private String startDate;
    
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期（格式：yyyy-MM-dd）", example = "2024-12-31")
    private String endDate;
    
    /**
     * 类型（收入/支出）
     */
    @ApiModelProperty(value = "类型（收入/支出）", example = "支出")
    private String type;
    
    /**
     * 分类
     */
    @ApiModelProperty(value = "分类", example = "餐饮")
    private String category;
    
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}
