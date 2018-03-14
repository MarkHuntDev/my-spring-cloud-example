package com.example.integrationtest.controller;

import com.example.controller.CelebrityController;
import com.example.integrationtest.controller.oauth2mock.WithMockOAuth2Scope;
import com.example.model.Celebrity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CelebrityControllerITViaJava {

    private CelebrityController celebrityController;

    @Autowired
    public void setCelebrityController(CelebrityController celebrityController) {
        this.celebrityController = celebrityController;
    }

    @WithMockOAuth2Scope
    @Test
    public void testRandomCelebrityWithTestProperty() {
        Celebrity randomCelebrityWithTestProperty = celebrityController.randomCelebrityWithTestProperty();
        System.out.println(randomCelebrityWithTestProperty.getName());
        Assert.assertNotNull(randomCelebrityWithTestProperty);
    }
}
