package com.example.controller;

import com.example.entity.Celebrity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    private static List<Celebrity> celebrities = Arrays.asList(
            new Celebrity().setName("Chuck Norris"),
            new Celebrity().setName("Ornella Muti"),
            new Celebrity().setName("Adriano Chelentano")
    );

    private TokenStore tokenStore;

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Celebrity> celebrities() {
        return celebrities;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Celebrity celebrity(@PathVariable("id") Integer id) {
        return celebrities.get(id);
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Celebrity randomCelebrity() {
        if (celebrities.isEmpty()) {
            return new Celebrity();
        }
        return celebrities.get(randomCelebrityIndex());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/random-with-test-property", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Celebrity randomCelebrityWithTestProperty() {
        Celebrity celebrity = randomCelebrity();

        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails authDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(authDetails.getTokenValue());
        Map<String, Object> jwtAdditionalInformation = accessToken.getAdditionalInformation();

        String testPropertyValue = (String) jwtAdditionalInformation.get("testProperty");

        celebrity.setName(celebrity.getName() + " ; Test Property Value: " + testPropertyValue);
        return celebrity;
    }

    private static int randomCelebrityIndex() {
        return new Random().nextInt(celebrities.size());
    }
}
