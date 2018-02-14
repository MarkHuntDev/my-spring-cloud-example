package com.example.feign;

import com.example.model.Celebrity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("celebrity-service")
@RequestMapping("/celebrities")
public interface CelebrityClient {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Celebrity celebrity(@PathVariable("id") Integer celebrityId);
}
