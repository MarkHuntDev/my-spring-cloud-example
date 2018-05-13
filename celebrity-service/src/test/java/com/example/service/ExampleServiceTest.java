package com.example.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;

@ActiveProfiles("method-security-test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExampleService.class, ExampleServiceTest.MethodSecurityConfiguration.class})
public class ExampleServiceTest {

    private ExampleService service;

    @Autowired
    public void setService(ExampleService service) {
        this.service = service;
    }

    @Test
    @WithMockUser(username = "john_doe")
    public void testAuthenticated() {
        String actualMessage = service.example();
        Assert.assertEquals("Message of john_doe", actualMessage);
    }

    @Test(expected = AuthenticationException.class)
    public void testNotAuthenticated() {
        service.example();
        Assert.fail();
    }

    @TestConfiguration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    static class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }
}

@Service
class ExampleService {
    @PreAuthorize("isAuthenticated()")
    String example() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return "Message of " + principal.getName();
    }
}