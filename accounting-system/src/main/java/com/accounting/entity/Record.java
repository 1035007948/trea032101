package com.accounting.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 收支记录实体类
 * 对应数据库表：record
 */
@Data
@Entity
@Table(name = "record")
public class Record {

    /**
     * 记录ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID，外键关联user表
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 金额
     */
    @Column(nullable = false, precision = 19, scale = 2)
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
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 设置金额，自动四舍五入到整数
     *
     * @param amount 金额
     */
    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount.setScale(0, RoundingMode.HALF_UP);
        } else {
            this.amount = amount;
        }
    }

    /**
     * 实体保存前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 实体更新前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
