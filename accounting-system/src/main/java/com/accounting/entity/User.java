package com.accounting.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表 t_user
 */
@Data
@Entity
@Table(name = "t_user")
public class User {
    
    /**
     * 用户ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户账号，唯一约束
     */
    @Column(nullable = false, unique = true, length = 50)
    private String account;
    
    /**
     * 用户密码
     */
    @Column(nullable = false, length = 100)
    private String password;
    
    /**
     * 用户昵称
     */
    @Column(length = 50)
    private String nickname;
    
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
}
