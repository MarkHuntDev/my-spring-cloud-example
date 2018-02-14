package com.example.controller;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GreetingControllerTest {

    private static final String GREETING_GOOD_MORNING = "Good morning";
    private static final String GREETING_GOOD_AFTERNOON = "Good afternoon";
    private static final String GREETING_GOOD_EVENING = "Good evening";

    private MockMvc mockMvc;

    @InjectMocks
    private GreetingController greetingController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.greetingController)
                .build();
    }

    @Test
    public void shouldReturnListOfAllGreetings() throws Exception {
        mockMvc.perform(get("/greetings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].value", is(GREETING_GOOD_MORNING)))
                .andExpect(jsonPath("$[1].value", is(GREETING_GOOD_AFTERNOON)))
                .andExpect(jsonPath("$[2].value", is(GREETING_GOOD_EVENING)));
    }

    @Test
    public void shouldReturnGoodMorningGreetingByIdZero() throws Exception {
        mockMvc.perform(get("/greetings/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.value", is(GREETING_GOOD_MORNING)));
    }

    @Test
    public void shouldReturnRandomGreeting() throws Exception {
        mockMvc.perform(get("/greetings/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.value",
                        anyOf(is(GREETING_GOOD_MORNING), is(GREETING_GOOD_AFTERNOON), is(GREETING_GOOD_EVENING))));
    }
}