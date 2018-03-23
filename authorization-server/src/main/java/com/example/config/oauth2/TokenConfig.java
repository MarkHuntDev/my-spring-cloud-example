package com.example.config.oauth2;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.List;

@Configuration
public class TokenConfig {

    /**
     * Alias of keystore.
     */
    @Value("${security.keystore.alias}")
    private String keyStoreAlias;

    /**
     * Filename of keystore.
     */
    @Value("${security.keystore.filename}")
    private String keyStoreFileName;

    /**
     * Passphrase of keystore.
     */
    @Value("${security.keystore.passphrase}")
    private String keyStorePassphrase;

    /**
     * All {@link TokenEnhancer}s which process {@code JWT}.
     */
    private List<TokenEnhancer> tokenEnhancers;

    @Autowired
    public void setTokenEnhancers(List<TokenEnhancer> tokenEnhancers) {
        this.tokenEnhancers = tokenEnhancers;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(this.keyStoreKeyFactory().getKeyPair(this.keyStoreAlias));
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(this.accessTokenConverter());
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(this.allTokenEnhancers());
        return chain;
    }

    private List<TokenEnhancer> allTokenEnhancers() {
        return ImmutableList
                .<TokenEnhancer>builder()
                .addAll(this.tokenEnhancers)
                // JwtAccessTokenConverter must be at the end of enhancers list
                .add(this.accessTokenConverter())
                .build();
    }

    private KeyStoreKeyFactory keyStoreKeyFactory() {
        return new KeyStoreKeyFactory(new ClassPathResource(this.keyStoreFileName), this.keyStorePassphrase.toCharArray());
    }
}
