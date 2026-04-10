-- =============================================
-- 个人收支记账系统数据库部署脚本
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS accounting_system 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE accounting_system;

-- =============================================
-- 用户表
-- =============================================
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    account VARCHAR(50) NOT NULL COMMENT '账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_account (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 记账记录表
-- =============================================
DROP TABLE IF EXISTS t_record;
CREATE TABLE t_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(15,2) NOT NULL COMMENT '金额',
    type VARCHAR(10) NOT NULL COMMENT '类型（收入/支出）',
    category VARCHAR(20) NOT NULL COMMENT '分类',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time),
    KEY idx_type (type),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='记账记录表';

-- =============================================
-- 初始化测试数据（可选）
-- =============================================

-- 插入测试用户（密码为123456）
INSERT INTO t_user (account, password, nickname) VALUES 
('13800138000', '123456', '测试用户1'),
('test@example.com', '123456', '测试用户2');

-- 插入测试记账记录
INSERT INTO t_record (user_id, amount, type, category, remark) VALUES 
(1, 5000, '收入', '薪资', '1月工资'),
(1, 500, '收入', '奖金', '项目奖金'),
(1, 100, '支出', '餐饮', '午餐'),
(1, 200, '支出', '购物', '日用品'),
(1, 50, '支出', '交通', '地铁'),
(1, 300, '支出', '娱乐', '电影'),
(2, 8000, '收入', '薪资', '1月工资'),
(2, 150, '支出', '餐饮', '聚餐'),
(2, 500, '支出', '购物', '衣服');

-- =============================================
-- 创建视图（可选）
-- =============================================

-- 用户收支统计视图
DROP VIEW IF EXISTS v_user_stats;
CREATE VIEW v_user_stats AS
SELECT 
    u.id AS user_id,
    u.account,
    u.nickname,
    COALESCE(SUM(CASE WHEN r.type = '收入' THEN r.amount ELSE 0 END), 0) AS total_income,
    COALESCE(SUM(CASE WHEN r.type = '支出' THEN r.amount ELSE 0 END), 0) AS total_expense,
    COALESCE(SUM(CASE WHEN r.type = '收入' THEN r.amount ELSE -r.amount END), 0) AS balance,
    COUNT(r.id) AS record_count
FROM t_user u
LEFT JOIN t_record r ON u.id = r.user_id
GROUP BY u.id, u.account, u.nickname;

-- =============================================
-- 创建存储过程（可选）
-- =============================================

-- 获取用户月度统计
DROP PROCEDURE IF EXISTS sp_monthly_stats;
DELIMITER //
CREATE PROCEDURE sp_monthly_stats(
    IN p_user_id BIGINT,
    IN p_year INT,
    IN p_month INT
)
BEGIN
    SELECT 
        COALESCE(SUM(CASE WHEN type = '收入' THEN amount ELSE 0 END), 0) AS total_income,
        COALESCE(SUM(CASE WHEN type = '支出' THEN amount ELSE 0 END), 0) AS total_expense,
        COUNT(*) AS record_count
    FROM t_record
    WHERE user_id = p_user_id
    AND YEAR(create_time) = p_year
    AND MONTH(create_time) = p_month;
END //
DELIMITER ;

-- 获取用户分类统计
DROP PROCEDURE IF EXISTS sp_category_stats;
DELIMITER //
CREATE PROCEDURE sp_category_stats(
    IN p_user_id BIGINT,
    IN p_type VARCHAR(10)
)
BEGIN
    SELECT 
        category,
        SUM(amount) AS total_amount,
        COUNT(*) AS record_count,
        ROUND(SUM(amount) * 100.0 / (SELECT SUM(amount) FROM t_record WHERE user_id = p_user_id AND (p_type IS NULL OR p_type = '' OR type = p_type)), 2) AS percentage
    FROM t_record
    WHERE user_id = p_user_id
    AND (p_type IS NULL OR p_type = '' OR type = p_type)
    GROUP BY category
    ORDER BY total_amount DESC;
END //
DELIMITER ;

-- =============================================
-- 索引优化建议
-- =============================================
-- 如果数据量较大，可以考虑添加以下索引：
-- ALTER TABLE t_record ADD INDEX idx_user_type (user_id, type);
-- ALTER TABLE t_record ADD INDEX idx_user_category (user_id, category);
-- ALTER TABLE t_record ADD INDEX idx_user_time (user_id, create_time);

-- =============================================
-- 完成提示
-- =============================================
SELECT '数据库部署完成！' AS message;
