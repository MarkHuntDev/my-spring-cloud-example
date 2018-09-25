package com.example.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(SpringProfile.PROFILE_DEPENDENT)
@Configuration
@EnableEurekaClient
public class ServiceDiscoveryConfig {
}
