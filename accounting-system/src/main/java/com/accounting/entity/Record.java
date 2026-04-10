package com.accounting.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 记账记录实体类
 * 对应数据库表 t_record
 */
@Data
@Entity
@Table(name = "t_record")
public class Record {
    
    /**
     * 记录ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户ID，外键关联用户表
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    /**
     * 类型：收入/支出
     */
    @Column(nullable = false, length = 10)
    private String type;
    
    /**
     * 分类
     */
    @Column(nullable = false, length = 20)
    private String category;
    
    /**
     * 备注
     */
    @Column(length = 200)
    private String remark;
    
    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 设置金额，自动四舍五入取整
     * @param amount 金额
     */
    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount.setScale(0, RoundingMode.HALF_UP);
        } else {
            this.amount = amount;
        }
    }
}
