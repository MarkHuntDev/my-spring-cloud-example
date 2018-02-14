package com.example.controller;

import com.example.model.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

    private static List<Greeting> greetings = Arrays.asList(
            new Greeting().setValue("Good morning"),
            new Greeting().setValue("Good afternoon"),
            new Greeting().setValue("Good evening")
    );


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Greeting> greetings() {
        return greetings;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Greeting greeting(@PathVariable("id") Integer id) {
        return greetings.get(id);
    }


    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public Greeting randomGreeting() {
        if (greetings.isEmpty()) {
            return new Greeting();
        }
        return greetings.get(randomGreetingIndex());
    }

    private static int randomGreetingIndex() {
        return new Random().nextInt(greetings.size());
    }
}
