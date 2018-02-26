package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Profile( {"default", "local"})
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private TokenStore tokenStore;
    private TokenEnhancerChain tokenEnhancerChain;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    public void setTokenEnhancerChain(TokenEnhancerChain tokenEnhancerChain) {
        this.tokenEnhancerChain = tokenEnhancerChain;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
        clients.inMemory()
          .withClient("client-example")
          .secret("client-secret")
          .authorizedGrantTypes("password")
          .scopes("message-service")
          .autoApprove(true);
    } // @formatter:on

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) { // @formatter:off
        endpoints
          .tokenStore(this.tokenStore)
          .authenticationManager(this.authenticationManager)
          .tokenEnhancer(this.tokenEnhancerChain);
    } // @formatter:on
}
