package com.accounting.config;

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
 * 用于生成API文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 创建API文档
     * @return Docket对象
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accounting.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建API基本信息
     * @return ApiInfo对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("个人收支记账系统API文档")
                .description("个人收支记账系统接口文档，包含用户管理、记账记录管理、统计等功能")
                .contact(new Contact("Accounting System", "", ""))
                .version("1.0.0")
                .build();
    }
}
