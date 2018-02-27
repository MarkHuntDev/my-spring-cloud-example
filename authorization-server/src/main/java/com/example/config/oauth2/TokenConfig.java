package com.example.config.oauth2;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.List;

@Configuration
public class TokenConfig {

    @Value("${signing-key:test123}")
    private String signingKey;

    private List<TokenEnhancer> tokenEnhancers;

    @Autowired
    public void setTokenEnhancers(List<TokenEnhancer> tokenEnhancers) {
        this.tokenEnhancers = tokenEnhancers;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(this.signingKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(allTokenEnhancers());
        return chain;
    }

    private List<TokenEnhancer> allTokenEnhancers() {
        return ImmutableList
                .<TokenEnhancer>builder()
                .addAll(this.tokenEnhancers)
                .add(accessTokenConverter())
                .build();
    }
}
