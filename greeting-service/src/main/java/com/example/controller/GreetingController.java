package com.example.controller;

import com.example.entity.Greeting;
import com.example.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@RestController
@RequestMapping("/greetings")
public class GreetingController {

    private final GreetingRepository repository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Greeting> greetings() {
        return this.repository.findAll();
    }


    @GetMapping("/{id}")
    public Greeting greeting(@PathVariable("id") Long id) {
        return this.repository.findOne(id);
    }


    @GetMapping("/random")
    public Greeting randomGreeting() {
        if (this.repository.count() == 0) {
            throw new IllegalArgumentException("No greetings found");
        }
        return this.repository.findOne(ThreadLocalRandom.current().nextLong(1, repository.count()));
    }
}
