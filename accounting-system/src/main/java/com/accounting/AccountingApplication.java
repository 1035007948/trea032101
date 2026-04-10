package com.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 记账系统启动类
 * Spring Boot应用入口
 */
@SpringBootApplication
public class AccountingApplication {
    
    /**
     * 主方法，启动Spring Boot应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }
}
