package com.example.feign;

import com.example.model.Greeting;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("greeting-service")
@RequestMapping("/greetings")
public interface GreetingClient {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Greeting greeting(@PathVariable("id") Integer greetingId);
}
