-- 创建数据库
CREATE DATABASE IF NOT EXISTS accounting_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE accounting_db;

-- 创建用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    account VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建记录表
DROP TABLE IF EXISTS t_record;
CREATE TABLE t_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    type VARCHAR(20) NOT NULL COMMENT '类型：income-收入，expense-支出',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收支记录表';

-- 插入测试数据
INSERT INTO t_user (account, password, nickname, create_time, update_time) VALUES
('admin', '123456', '管理员', NOW(), NOW()),
('test', '123456', '测试用户', NOW(), NOW());

INSERT INTO t_record (user_id, amount, type, category, remark, create_time, update_time) VALUES
(1, 5000.00, '收入', '薪资', '月薪', NOW(), NOW()),
(1, 1500.00, '支出', '餐饮', '日常吃饭', NOW(), NOW()),
(1, 300.00, '支出', '交通', '地铁公交', NOW(), NOW()),
(1, 2000.00, '收入', '奖金', '季度奖金', NOW(), NOW()),
(1, 800.00, '支出', '购物', '日用品', NOW(), NOW());
