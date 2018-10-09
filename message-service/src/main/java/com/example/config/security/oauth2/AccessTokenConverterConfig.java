package com.example.config.security.oauth2;

import static com.example.config.SpringProfile.PROFILE_SECURED;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.IOException;

@Profile(PROFILE_SECURED)
@Configuration
public class AccessTokenConverterConfig {

    @Value("${security.publicKey.filename}")
    private String publicKeyFilename;

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
}

class PublicKeyInitializationException extends RuntimeException {
    PublicKeyInitializationException(Throwable throwable) {
        super(throwable);
    }
}