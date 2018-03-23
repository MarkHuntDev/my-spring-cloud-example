package com.example.controller;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

import com.example.config.JacksonConfig;
import com.example.dto.Celebrity;
import com.example.dto.Greeting;
import com.example.dto.Message;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("stub")
public class MessageControllerIT {

    private static final Logger log = LoggerFactory.getLogger(MessageControllerIT.class);

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule
            .inSimulationMode(dsl(
                    service("greeting-service:8080")
                            .get("/greetings/1")
                            .willReturn(
                                    success(toJsonString(new Greeting().setValue("Good afternoon")), "application/json")),
                    service("celebrity-service:8080")
                            .get("/celebrities/1")
                            .willReturn(
                                    success(toJsonString(new Celebrity().setName("Ornella Muti")), "application/json"))
            )).printSimulationData();

    @Autowired
    private MessageController controller;

    @Test
    public void testMessage() {
        Message message = controller.message(1, 1);
        System.out.println(message.getValue());
        Assert.assertEquals("Good afternoon, Ornella Muti", message.getValue());
    }

    private static String toJsonString(Object object) {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        try {
            JacksonConfig.staticMappingJackson2HttpMessageConverter().write(object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return mockHttpOutputMessage.getBodyAsString();
    }
}
