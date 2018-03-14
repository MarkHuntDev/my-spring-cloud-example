package com.example.integrationtest.controller.oauth2mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuth2AndJwtSecurityContextFactory.class)
public @interface WithMockOAuth2AndJwt {
    String[] roles() default {"ROLE_USER"};
}