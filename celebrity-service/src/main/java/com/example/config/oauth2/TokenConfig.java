package com.example.config.oauth2;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

@Configuration
public class TokenConfig {

    @Value("${security.publicKey.filename}")
    private String publicKeyFilename;

    @Profile("default")
    @Bean("accessTokenConverter")
    JwtAccessTokenConverter accessTokenConverterDefault() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        Resource resource = new ClassPathResource(this.publicKeyFilename);
        String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (final IOException ex) {
            throw new PublicKeyInitializationException(ex);
        }

        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Profile("test")
    @Bean("accessTokenConverter")
    JwtAccessTokenConverter accessTokenConverterTest() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test123");
        return converter;
    }

    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(this.accessTokenConverterDefault());
    }
}

class PublicKeyInitializationException extends RuntimeException {
    PublicKeyInitializationException(Throwable throwable) {
        super(throwable);
    }
}