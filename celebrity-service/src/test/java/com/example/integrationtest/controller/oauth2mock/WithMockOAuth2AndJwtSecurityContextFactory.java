package com.example.integrationtest.controller.oauth2mock;

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
public class WithMockOAuth2AndJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2AndJwt> {

    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    public void setTokenServices(AuthorizationServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2AndJwt mockOAuth2AndJwt) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        OAuth2Request oAuth2Request = oAuth2Request(mockOAuth2AndJwt);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, null);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setDetails(authenticationDetails(accessToken));
        context.setAuthentication(oAuth2Authentication);

        return context;
    }

    private OAuth2Request oAuth2Request(WithMockOAuth2AndJwt mockOAuth2AndJwt) {
        return new OAuth2Request(null, null, authorities(mockOAuth2AndJwt.roles()), true, null, null, null, null, null);
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

    private static class OAuth2AuthenticationBuilder {

    }
}
