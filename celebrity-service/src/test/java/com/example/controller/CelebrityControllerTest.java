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

public class CelebrityControllerTest {

    private static final String CELEBRITY_CHUCK_NORRIS = "Chuck Norris";
    private static final String CELEBRITY_ORNELLA_MUTI = "Ornella Muti";
    private static final String CELEBRITY_ADRIANO_CHELENTANO = "Adriano Chelentano";

    private MockMvc mockMvc;

    @InjectMocks
    private CelebrityController celebrityController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.celebrityController)
                .build();
    }

    @Test
    public void shouldReturnListOfAllGreetings() throws Exception {
        mockMvc.perform(get("/celebrities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(CELEBRITY_CHUCK_NORRIS)))
                .andExpect(jsonPath("$[1].name", is(CELEBRITY_ORNELLA_MUTI)))
                .andExpect(jsonPath("$[2].name", is(CELEBRITY_ADRIANO_CHELENTANO)));
    }

    @Test
    public void shouldReturnGoodMorningGreetingByIdZero() throws Exception {
        mockMvc.perform(get("/celebrities/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(CELEBRITY_CHUCK_NORRIS)));
    }

    @Test
    public void shouldReturnRandomGreeting() throws Exception {
        mockMvc.perform(get("/celebrities/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name",
                        anyOf(is(CELEBRITY_CHUCK_NORRIS), is(CELEBRITY_ORNELLA_MUTI), is(CELEBRITY_ADRIANO_CHELENTANO))));
    }
}