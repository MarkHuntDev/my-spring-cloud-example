package com.example.integrationtest.config.oauth2mock;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 * Step Builder pattern implementation
 * for {@link OAuth2Authentication} creation.
 */
class OAuth2AuthenticationBuilder {

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