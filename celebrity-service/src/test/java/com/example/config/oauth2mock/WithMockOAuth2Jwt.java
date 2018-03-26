package com.example.config.oauth2mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuth2JwtSecurityContextFactory.class)
public @interface WithMockOAuth2Jwt {
    String[] roles() default {"ROLE_USER"};
}