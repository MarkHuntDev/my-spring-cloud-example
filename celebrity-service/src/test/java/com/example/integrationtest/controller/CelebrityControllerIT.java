package com.example.integrationtest.controller;

import static org.junit.Assert.assertEquals;

import com.example.integrationtest.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Test;

public class CelebrityControllerIT extends BaseIntegrationTest {

    @Test
    public void testCelebrity() {
        final Response response = RestAssured
                .given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + this.getValidAccessToken())
                .get(this.getAuthServerUrl() + "/celebrities/0");
        assertEquals(200, response.getStatusCode());
    }
}
