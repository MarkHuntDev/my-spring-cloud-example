package com.example.integrationtest.config.oauth2mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class WithMockOAuth2JwtSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2Jwt> {

    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    public void setTokenServices(AuthorizationServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2Jwt mockOAuth2AndJwt) {

        OAuth2Request oAuth2Request = OAuth2RequestBuilder
                .builder()
                .withRequestParameters(null)
                .withClientId(null)
                .withAuthorities(authorities(mockOAuth2AndJwt.roles()))
                .withApproved(true)
                .withScopes(null)
                .withResourceIds(null)
                .withRedirectUri(null)
                .withResponseTypes(null)
                .withExtensionProperties(null)
                .build();

        OAuth2Authentication oAuth2Authentication = OAuth2AuthenticationBuilder
                .builder()
                .withOAuth2Request(oAuth2Request)
                .withAuthentication(null)
                .build();

        OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);

        oAuth2Authentication.setDetails(authenticationDetails(accessToken));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(oAuth2Authentication);
        return context;
    }

    private Collection<? extends GrantedAuthority> authorities(String[] roleNames) {
        return Arrays.stream(roleNames)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private OAuth2AuthenticationDetails authenticationDetails(OAuth2AccessToken accessToken) {
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
        httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
        return new OAuth2AuthenticationDetails(httpServletRequest);
    }
}
