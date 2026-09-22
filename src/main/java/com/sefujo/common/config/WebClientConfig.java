package com.sefujo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("greenhouseWebClient")
    public WebClient greenhouseWebClient() {
        return WebClient.builder()
                .baseUrl("https://boards-api.greenhouse.io")
                .codecs(configurer ->
                        configurer.defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024)
                )
                .build();
    }

    // Add another WebClient bean when implementing another source.
}
