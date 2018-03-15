package com.example.integrationtest.config.oauth2mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
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

    private static class OAuth2RequestBuilder {

        static RequestParametersStep builder() {
            return new Steps().startStep();
        }

        private static class Steps
                implements
                StartStep,
                RequestParametersStep,
                ClientIdStep,
                AuthoritiesStep,
                ApprovedStep,
                ScopesStep,
                ResourceIdsStep,
                RedirectUriStep,
                ResponseTypesStep,
                ExtensionPropertiesStep,
                BuildStep {

            private Map<String, String> requestParameters;
            private String clientId;
            private Collection<? extends GrantedAuthority> authorities;
            private boolean approved;
            private Set<String> scopes;
            private Set<String> resourceIds;
            private String redirectUri;
            private Set<String> responseTypes;
            private Map<String, Serializable> extensionProperties;

            @Override
            public RequestParametersStep startStep() {
                return this;
            }

            @Override
            public ClientIdStep withRequestParameters(Map<String, String> requestParameters) {
                this.requestParameters = requestParameters;
                return this;
            }

            @Override
            public AuthoritiesStep withClientId(String clientId) {
                this.clientId = clientId;
                return this;
            }

            @Override
            public ApprovedStep withAuthorities(Collection<? extends GrantedAuthority> authorities) {
                this.authorities = authorities;
                return this;
            }

            @Override
            public ScopesStep withApproved(boolean approved) {
                this.approved = approved;
                return this;
            }

            @Override
            public ResourceIdsStep withScopes(Set<String> scopes) {
                this.scopes = scopes;
                return this;
            }

            @Override
            public RedirectUriStep withResourceIds(Set<String> resourceIds) {
                this.resourceIds = resourceIds;
                return this;
            }

            @Override
            public ResponseTypesStep withRedirectUri(String redirectUri) {
                this.redirectUri = redirectUri;
                return this;
            }

            @Override
            public ExtensionPropertiesStep withResponseTypes(Set<String> responseTypes) {
                this.responseTypes = responseTypes;
                return this;
            }

            @Override
            public BuildStep withExtensionProperties(Map<String, Serializable> extensionProperties) {
                this.extensionProperties = extensionProperties;
                return this;
            }

            @Override
            public OAuth2Request build() {
                return new OAuth2Request(requestParameters, clientId, authorities, approved, scopes, resourceIds,
                        redirectUri, responseTypes, extensionProperties);
            }
        }

        interface StartStep {
            RequestParametersStep startStep();
        }

        interface RequestParametersStep {
            ClientIdStep withRequestParameters(Map<String, String> requestParameters);
        }

        interface ClientIdStep {
            AuthoritiesStep withClientId(String clientId);
        }

        interface AuthoritiesStep {
            ApprovedStep withAuthorities(Collection<? extends GrantedAuthority> authorities);
        }

        interface ApprovedStep {
            ScopesStep withApproved(boolean approved);
        }

        interface ScopesStep {
            ResourceIdsStep withScopes(Set<String> scopes);
        }

        interface ResourceIdsStep {
            RedirectUriStep withResourceIds(Set<String> resourceIds);
        }

        interface RedirectUriStep {
            ResponseTypesStep withRedirectUri(String redirectUri);
        }

        interface ResponseTypesStep {
            ExtensionPropertiesStep withResponseTypes(Set<String> responseTypes);
        }

        interface ExtensionPropertiesStep {
            BuildStep withExtensionProperties(Map<String, Serializable> extensionProperties);
        }

        interface BuildStep {
            OAuth2Request build();
        }
    }

    private static class OAuth2AuthenticationBuilder {

        static OAuth2RequestStep builder() {
            return new Steps().startStep();
        }

        private static class Steps
                implements
                StartStep,
                OAuth2RequestStep,
                AuthenticationStep,
                BuildStep {

            private OAuth2Request oAuth2Request;
            private Authentication authentication;

            @Override
            public OAuth2RequestStep startStep() {
                return this;
            }

            @Override
            public AuthenticationStep withOAuth2Request(OAuth2Request oAuth2Request) {
                this.oAuth2Request = oAuth2Request;
                return this;
            }

            @Override
            public BuildStep withAuthentication(Authentication authentication) {
                this.authentication = authentication;
                return this;
            }

            @Override
            public OAuth2Authentication build() {
                return new OAuth2Authentication(oAuth2Request, authentication);
            }
        }

        interface StartStep {
            OAuth2RequestStep startStep();
        }

        interface OAuth2RequestStep {
            AuthenticationStep withOAuth2Request(OAuth2Request oAuth2Request);
        }

        interface AuthenticationStep {
            BuildStep withAuthentication(Authentication authentication);
        }

        interface BuildStep {
            OAuth2Authentication build();
        }
    }
}
