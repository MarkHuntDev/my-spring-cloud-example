package com.example.controller;

import com.example.model.Celebrity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    private static List<Celebrity> celebrities = Arrays.asList(
            new Celebrity().setName("Chuck Norris"),
            new Celebrity().setName("Ornella Muti"),
            new Celebrity().setName("Adriano Chelentano")
    );


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Celebrity> celebrities() {
        return celebrities;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Celebrity celebrity(@PathVariable("id") Integer id) {
        return celebrities.get(id);
    }


    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public Celebrity randomCelebrity() {
        if (celebrities.isEmpty()) {
            return new Celebrity();
        }
        return celebrities.get(randomCelebrityIndex());
    }

    private static int randomCelebrityIndex() {
        return new Random().nextInt(celebrities.size());
    }
}
