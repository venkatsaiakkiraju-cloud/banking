package com.securebank.transaction.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
@Configuration @Getter
public class ServiceUrlConfig {
    @Value("${services.account-service.url:http://localhost:8083}")
    private String accountServiceUrl;
}
