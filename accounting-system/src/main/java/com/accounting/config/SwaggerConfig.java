package com.accounting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 * 用于配置API文档生成
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 是否启用Swagger
     */
    @Value("${swagger.enabled:true}")
    private boolean enabled;

    /**
     * API文档标题
     */
    @Value("${swagger.title:个人收支记账系统API}")
    private String title;

    /**
     * API文档描述
     */
    @Value("${swagger.description:个人收支记账系统接口文档}")
    private String description;

    /**
     * API版本
     */
    @Value("${swagger.version:1.0.0}")
    private String version;

    /**
     * 联系人名称
     */
    @Value("${swagger.contact.name:Accounting System}")
    private String contactName;

    /**
     * 扫描的包路径
     */
    @Value("${swagger.base-package:com.accounting.controller}")
    private String basePackage;

    /**
     * 创建Swagger Docket实例
     *
     * @return Docket实例
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(enabled)
                // API文档基本信息
                .apiInfo(apiInfo())
                // 选择哪些API生成文档
                .select()
                // 扫描指定包下的API
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                // 扫描所有路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建API文档基本信息
     *
     * @return API信息对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title(title)
                // 文档描述
                .description(description)
                // 版本号
                .version(version)
                // 联系人信息
                .contact(new Contact(contactName, "", ""))
                .build();
    }
}
