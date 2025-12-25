package com.demo.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payway")
public class PaywayConfig {

    private String merchantId;
    private String apiKey;
    private String secretKey;
    private String baseUrl;
    private String callbackUrl;
    private String returnUrl;
    private String paymentOption; // abapay, cards, abapay_deeplink
}
