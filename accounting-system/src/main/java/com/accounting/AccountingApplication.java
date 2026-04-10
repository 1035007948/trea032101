package com.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 个人收支记账系统启动类
 * Spring Boot应用程序入口
 */
@SpringBootApplication
public class AccountingApplication {

    /**
     * 应用程序入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }
}
