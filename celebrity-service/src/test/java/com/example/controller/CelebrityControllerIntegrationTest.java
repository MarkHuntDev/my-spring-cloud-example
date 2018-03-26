package com.example.controller;

import com.example.entity.Celebrity;
import com.example.config.oauth2mock.WithMockOAuth2Jwt;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CelebrityControllerIntegrationTest {

    private CelebrityController celebrityController;

    @Autowired
    public void setCelebrityController(CelebrityController celebrityController) {
        this.celebrityController = celebrityController;
    }

    @Test
    @WithMockOAuth2Jwt(roles = "ROLE_ADMIN")
    public void testRandomCelebrityWithTestProperty() {
        Celebrity randomCelebrityWithTestProperty = celebrityController.randomCelebrityWithTestProperty();
        System.out.println(randomCelebrityWithTestProperty.getName());
        Assert.assertNotNull(randomCelebrityWithTestProperty);
    }
}