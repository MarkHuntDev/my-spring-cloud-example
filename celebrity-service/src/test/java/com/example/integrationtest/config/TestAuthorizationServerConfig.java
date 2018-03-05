package com.example.integrationtest.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Profile("test")
@Configuration
@EnableAuthorizationServer
public class TestAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private TokenStore tokenStore;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
        clients.inMemory()
          .withClient("test-client")
          .secret("test-secret")
          .authorizedGrantTypes("password")
          .scopes("celebrity-service")
          .autoApprove(true);
    } // @formatter:on

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) { // @formatter:off
        endpoints
          .tokenStore(this.tokenStore)
          .authenticationManager(this.authenticationManager)
          .tokenEnhancer(tokenEnhancerChain());
    } // @formatter:on

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("permitAll()");
    }

    // TOKEN ENHANCERS

    private JwtAccessTokenConverter accessTokenConverter;
    private List<TokenEnhancer> tokenEnhancers;

    @Autowired
    public void setAccessTokenConverter(JwtAccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }

    @Autowired
    public void setTokenEnhancers(List<TokenEnhancer> tokenEnhancers) {
        this.tokenEnhancers = tokenEnhancers;
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
                .add(this.accessTokenConverter)
                .build();
    }
}
