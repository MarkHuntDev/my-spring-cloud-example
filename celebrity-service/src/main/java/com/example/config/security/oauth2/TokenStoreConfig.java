package com.example.config.security.oauth2;

import com.example.config.SpringProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Profile(SpringProfile.PROFILE_NOT_NO_SECURITY)
@Configuration
public class TokenStoreConfig {

    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    public void setAccessTokenConverter(JwtAccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }

    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(this.accessTokenConverter);
    }
}

