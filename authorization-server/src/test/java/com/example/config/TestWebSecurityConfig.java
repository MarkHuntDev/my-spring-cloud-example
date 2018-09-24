package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("test")
@Configuration
@EnableWebSecurity
public class TestWebSecurityConfig {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
        auth
          .inMemoryAuthentication()
          .withUser("user").password("pass").roles("USER")
            .and()
          .withUser("admin").password("admin").roles("ADMIN");
    } // @formatter:on
}
