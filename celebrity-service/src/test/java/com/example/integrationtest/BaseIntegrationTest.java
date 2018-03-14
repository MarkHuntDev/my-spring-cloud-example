package com.example.integrationtest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Value("http://localhost:${local.server.port}")
    private String authServerUrl;

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    /**
     * Cached {@code access_token}.
     */
    private String accessToken;

    public String getValidAccessToken() {
        if (this.accessToken == null || !isValidToken(this.accessToken)) {
            this.accessToken = obtainAccessToken();
        }
        return this.accessToken;
    }

    private String obtainAccessToken() {

        // @formatter:off
        Response response = RestAssured
                .given()
                .auth().preemptive().basic("test-client", "test-secret")
                  .and()
                .with().params(accessTokenObtainingRequestParams())
                .when()
                .post(authServerUrl + "/oauth/token");
        // @formatter:on

        if (!isOk(response.getStatusCode())) {
            throw new RuntimeException("Cannot obtain access_token: " + response.getBody().asString());
        }

        return response.jsonPath().getString("access_token");
    }

    private boolean isValidToken(String accessToken) {
        // @formatter:off
        Response response = RestAssured
                .given()
                .auth().preemptive().basic("test-client", "test-secret")
                  .and()
                .with().params(accessTokenVerifyingRequestParams(accessToken))
                .when()
                .post(authServerUrl + "/oauth/check_token");
        // @formatter:on

        return isOk(response.getStatusCode());
    }

    private static boolean isOk(int statusCode) {
        return HttpStatus.OK.value() == statusCode;
    }

    private static Map<String, String> accessTokenObtainingRequestParams() {
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "password");
        params.put("client_id", "test-client");
        params.put("username", "user");
        params.put("password", "pass");

        return Collections.unmodifiableMap(params);
    }

    private static Map<String, String> accessTokenVerifyingRequestParams(String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", accessToken);
        return Collections.unmodifiableMap(params);
    }
}
