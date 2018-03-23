package com.example.controller;

import com.example.feign.CelebrityClient;
import com.example.feign.GreetingClient;
import com.example.dto.Celebrity;
import com.example.dto.Greeting;
import com.example.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private GreetingClient greetingClient;
    private CelebrityClient celebrityClient;

    @Autowired
    public void setGreetingClient(GreetingClient greetingClient) {
        this.greetingClient = greetingClient;
    }

    @Autowired
    public void setCelebrityClient(CelebrityClient celebrityClient) {
        this.celebrityClient = celebrityClient;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Message message(@RequestParam("greetingId") int greetingId, @RequestParam("celebrityId") int celebrityId) {
        Greeting greeting = greetingClient.greeting(greetingId);
        Celebrity celebrity = celebrityClient.celebrity(celebrityId);
        return new Message().setValue(greeting.getValue() + ", " + celebrity.getName());
    }
}
