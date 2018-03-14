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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// todo: give compliance name
// todo: refactoring
@Component
public class WithMockOAuth2ScopeSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2Scope> {

    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    public void setTokenServices(AuthorizationServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2Scope mockOAuth2Scope) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Set<String> scope = new HashSet<>();
        scope.add(mockOAuth2Scope.scope());

        GrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");


        OAuth2Request request = new OAuth2Request(null, null, Collections.singletonList(admin),
                true, scope, null, null, null, null);


        OAuth2Authentication auth = new OAuth2Authentication(request, null);

        OAuth2AccessToken accessToken = tokenServices.createAccessToken(auth);

        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
        httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
        OAuth2AuthenticationDetails details = new OAuth2AuthenticationDetails(httpServletRequest);
        auth.setDetails(details);

        context.setAuthentication(auth);

        return context;
    }
}
