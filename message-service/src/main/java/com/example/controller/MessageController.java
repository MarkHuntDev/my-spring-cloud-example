package com.example.controller;

import com.example.dto.Celebrity;
import com.example.dto.Greeting;
import com.example.dto.Message;
import com.example.feign.CelebrityClient;
import com.example.feign.GreetingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final GreetingClient greetingClient;
    private final CelebrityClient celebrityClient;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Message message(@RequestParam("greetingId") int greetingId, @RequestParam("celebrityId") int celebrityId) {
        Greeting greeting = greetingClient.greeting(greetingId);
        Celebrity celebrity = celebrityClient.celebrity(celebrityId);
        return new Message().setValue(greeting.getValue() + ", " + celebrity.getName());
    }
}
