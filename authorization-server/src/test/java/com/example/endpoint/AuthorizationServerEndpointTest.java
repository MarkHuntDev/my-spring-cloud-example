package com.example.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationServerEndpointTest {

    @Value("${oauth2.clientId}")
    private String oAuth2ClientId;

    @Value("${oauth2.clientSecret}")
    private String oAuth2ClientSecret;

    @Value("http://localhost:${local.server.port}")
    private String authServerUrl;

    @Test
    public void shouldObtainJwtAccessToken() {

        // @formatter:off
        Response jwtResponse = RestAssured
                .given().auth().preemptive().basic(this.oAuth2ClientId, this.oAuth2ClientSecret)
                  .and()
                .with()
                .params(this.accessTokenObtainingRequestParams())
                .when()
                .post(this.authServerUrl + "/oauth/token");
        // @formatter:on

        assertEquals(200, jwtResponse.getStatusCode());

        assertNotNull(jwtResponse.jsonPath().get("access_token"));
        assertNotNull(jwtResponse.jsonPath().get("testProperty"));
    }

    private Map<String, String> accessTokenObtainingRequestParams() {
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "password");
        params.put("client_id", this.oAuth2ClientId);
        params.put("username", "user");
        params.put("password", "pass");

        return Collections.unmodifiableMap(params);
    }
}
