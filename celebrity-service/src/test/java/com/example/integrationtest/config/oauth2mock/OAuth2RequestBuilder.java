package com.example.integrationtest.config.oauth2mock;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Step Builder pattern implementation
 * for {@link OAuth2Request} creation.
 */
class OAuth2RequestBuilder {

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