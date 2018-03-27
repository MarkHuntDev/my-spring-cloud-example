package com.example.controller;

import com.example.config.oauth2mock.WithMockOAuth2Jwt;
import com.example.entity.Celebrity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CelebrityControllerIntegrationTest extends AbstractIntegrationTest {

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
