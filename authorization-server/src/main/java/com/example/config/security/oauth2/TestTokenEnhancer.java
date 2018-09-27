package com.example.config.security.oauth2;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestTokenEnhancer implements TokenEnhancer {

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
