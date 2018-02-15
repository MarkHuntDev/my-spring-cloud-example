package com.example.controller;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

import com.example.model.Message;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("stub")
public class MessageControllerIT {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule
            .inSimulationMode(
                    dsl(service("localhost:8500")
                            .get("/messages?greetingId=1&celebrityId=1")
                            .willReturn(
                                    success("{\"value\":\"Good afternoon, Ornella Muti\"}", "application/json"))));

    @Autowired
    private MessageController controller;

    @Test
    public void testMessage() {
        Message message = controller.message(1, 1);
        Assert.assertEquals("Good afternoon, Ornella Muti", message.getValue());
    }
}
