package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class TestAccessTokenConverterConfig {

    @Bean("accessTokenConverter")
    JwtAccessTokenConverter accessTokenConverterTest() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test123");
        return converter;
    }
}