package com.example.config.security.oauth2;

import com.example.config.SpringProfile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.IOException;

@Profile(SpringProfile.PROFILE_SECURED)
@Configuration
public class AccessTokenConverterConfig {

    @Value("${security.publicKey.filename}")
    private String publicKeyFilename;

    @Profile(SpringProfile.PROFILE_DEPENDENT)
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

    @Profile(SpringProfile.PROFILE_TEST)
    @Bean("accessTokenConverter")
    JwtAccessTokenConverter accessTokenConverterTest() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test123");
        return converter;
    }
}

class PublicKeyInitializationException extends RuntimeException {
    PublicKeyInitializationException(Throwable throwable) {
        super(throwable);
    }
}