package com.example.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AuthorizationServerEndpointTest {

    @Value("http://localhost:${local.server.port}")
    private String authServerUrl;

    @Test
    public void shouldObtainJwtAccessToken() { // @formatter:off

        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", "test-client");
        params.put("username", "user");
        params.put("password", "pass");

        Response jwtResponse = RestAssured
                .given().auth().preemptive().basic("test-client", "test-secret")
                  .and()
                .with()
                .params(params)
                .when()
                .post(this.authServerUrl + "/oauth/token");

        assertEquals(200, jwtResponse.getStatusCode());

        assertNotNull(jwtResponse.jsonPath().get("access_token"));
        assertNotNull(jwtResponse.jsonPath().get("testProperty"));
    } // @formatter:on
}
