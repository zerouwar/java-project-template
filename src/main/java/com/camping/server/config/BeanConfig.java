package com.camping.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

@Configuration
public class BeanConfig {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    ZoneId zoneId(){
        return ZoneId.of("UTC+8");
    }
}
