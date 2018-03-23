package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth2.clientId}")
    private String oAuth2ClientId;

    @Value("${oauth2.clientSecret}")
    private String oAuth2ClientSecret;

    @Value("${oauth2.scope}")
    private String oAuth2Scope;

    private AuthenticationManager authenticationManager;
    private TokenEnhancerChain tokenEnhancerChain;
    private TokenStore tokenStore;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setTokenEnhancerChain(TokenEnhancerChain tokenEnhancerChain) {
        this.tokenEnhancerChain = tokenEnhancerChain;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
        // used "password" Grant Type Flow for simplicity
        clients.inMemory()
          .withClient(this.oAuth2ClientId)
          .secret(this.oAuth2ClientSecret)
          .authorizedGrantTypes("password")
          .scopes(this.oAuth2Scope)
          .autoApprove(true);
    } // @formatter:on

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) { // @formatter:off
        endpoints
          .tokenStore(this.tokenStore)
          .authenticationManager(this.authenticationManager)
          .tokenEnhancer(this.tokenEnhancerChain);
    } // @formatter:on

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("permitAll()");
    }
}
