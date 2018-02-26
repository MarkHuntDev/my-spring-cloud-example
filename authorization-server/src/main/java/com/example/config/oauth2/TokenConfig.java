package com.example.config.oauth2;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.List;

@Configuration
public class TokenConfig {

    @Value("${signing-key:oui214hmui23o4hm1pui3o2hp4m1o3h2m1o43}")
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
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(allTokenEnhancers());
        return chain;
    }

    private List<TokenEnhancer> allTokenEnhancers() {
        TokenEnhancer[] restTokenEnhancers = this.tokenEnhancers.toArray(new TokenEnhancer[this.tokenEnhancers.size()]);
        return Lists.asList(accessTokenConverter(), restTokenEnhancers);
    }
}
