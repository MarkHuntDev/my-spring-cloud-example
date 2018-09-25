package com.example.controller;

import com.example.config.SpringProfile;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

@ActiveProfiles(SpringProfile.PROFILE_TEST)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static GenericContainer postgres = new GenericContainer("postgres:10.3")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "message_service")
            .withEnv("POSTGRES_USER", "ms_user")
            .withEnv("POSTGRES_PASSWORD", "ms_pass")
            .withClasspathResourceMapping("create_db_roles.sql", "/docker-entrypoint-initdb.d/create_db_roles.sql", BindMode.READ_ONLY);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final String JDBC_URL =
                String.format("jdbc:postgresql://%s:%d/message_service?currentSchema=cs", postgres.getContainerIpAddress(), postgres.getFirstMappedPort());

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            EnvironmentTestUtils.addEnvironment("testcontainers", applicationContext.getEnvironment(),
                    "spring.datasource.url=" + JDBC_URL,
                    "spring.datasource.username=cs_user2",
                    "spring.datasource.password=cs_pass2",
                    "liquibase.url=" + JDBC_URL,
                    "liquibase.user=cs_user1",
                    "liquibase.password=cs_pass1");
        }
    }

}
