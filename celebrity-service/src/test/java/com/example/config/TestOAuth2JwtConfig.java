package com.example.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Configuration
public class TestOAuth2JwtConfig {

    private TokenStore tokenStore;
    private JwtAccessTokenConverter accessTokenConverter;
    private List<TokenEnhancer> tokenEnhancers;

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    public void setAccessTokenConverter(JwtAccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }

    @Autowired
    public void setTokenEnhancers(List<TokenEnhancer> tokenEnhancers) {
        this.tokenEnhancers = tokenEnhancers;
    }

    @Bean
    @Primary
    DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(this.tokenStore);
        tokenServices.setTokenEnhancer(this.tokenEnhancerChain());
        return tokenServices;
    }

    @Bean
    TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(allTokenEnhancers());
        return chain;
    }

    @Component
    static class TestTokenEnhancer implements TokenEnhancer {

        @Value("${additional.jwt.property.name:testProperty}")
        private String additionalJwtPropertyName;

        @Value("${additional.jwt.property.value:testValue}")
        private String additionalJwtPropertyValue;

        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

            final Map<String, Object> additionalJwtProperties = ImmutableMap
                    .<String, Object>builder()
                    .put(this.additionalJwtPropertyName, this.additionalJwtPropertyValue)
                    .build();

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalJwtProperties);

            return accessToken;
        }
    }

    private List<TokenEnhancer> allTokenEnhancers() {
        return ImmutableList
                .<TokenEnhancer>builder()
                .addAll(this.tokenEnhancers)
                // JwtAccessTokenConverter must be at the end of enhancers list
                .add(this.accessTokenConverter)
                .build();
    }
}
