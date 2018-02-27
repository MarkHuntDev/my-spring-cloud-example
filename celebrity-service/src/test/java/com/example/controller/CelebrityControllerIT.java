package com.example.controller;

import static org.junit.Assert.assertEquals;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CelebrityControllerIT {

    @LocalServerPort
    private int port;

    @Test
//    @WithAnonymousUser
    public void testCelebrity() {
        final Response response = RestAssured
                .given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTk2Nzg4NTIsInVzZXJfbmFtZSI6InVzZXIiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZWNkNWJlYmEtYjNiZS00MzMzLWI5ZTctMWUxNTdhZTBkMjY0IiwiY2xpZW50X2lkIjoiY2xpZW50LWV4YW1wbGUiLCJzY29wZSI6WyJtZXNzYWdlLXNlcnZpY2UiXX0.z8vnknvctWmmVQOrUaOhEKI8n4wXA3Cli3XBi3A60u4")
                .get("http://localhost:" + port + "/celebrities/0");
        assertEquals(200, response.getStatusCode());
    }
}
