package com.example.endpoint;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Profile("test")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig {
}
