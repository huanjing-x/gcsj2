package com.gcsj2.config;

import org.springframework.context.annotation.Configuration;

/**
 * AI 配置类（预留扩展，密钥等配置在 application.yml 中）
 */
@Configuration
public class AiConfig {
    // AI 相关配置通过 @Value 注入到 AiServiceImpl 中
    // 此处预留，后续可扩展 Bean 定义（如 RestTemplate 超时配置等）
}
