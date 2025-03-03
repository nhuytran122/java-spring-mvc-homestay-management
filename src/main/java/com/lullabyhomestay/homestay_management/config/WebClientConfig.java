package com.lullabyhomestay.homestay_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // Tăng giới hạn buffer lên 10MB
        final int maxBufferSize = 10 * 1024 * 1024; // 10 MB
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxBufferSize))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}