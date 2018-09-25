package com.example.config;

import static com.example.config.SpringProfile.PROFILE_DEPENDENT;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(PROFILE_DEPENDENT)
@Configuration
@EnableEurekaClient
public class ServiceDiscoveryConfig {
}
