package com.example.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.feign.CelebrityClient;
import com.example.feign.GreetingClient;
import com.example.model.Celebrity;
import com.example.model.Greeting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CelebrityControllerTest {

    private static final String GREETING_GOOD_AFTERNOON = "Good afternoon";
    private static final String CELEBRITY_ORNELLA_MUTI = "Ornella Muti";

    private MockMvc mockMvc;

    @Mock
    private GreetingClient greetingClient;

    @Mock
    private CelebrityClient celebrityClient;

    @InjectMocks
    private MessageController messageController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.messageController)
                .build();
    }

    @Test
    public void shouldReturnRandomGreeting() throws Exception {

        when(greetingClient.greeting(1)).thenReturn(new Greeting().setValue(GREETING_GOOD_AFTERNOON));
        when(celebrityClient.celebrity(1)).thenReturn(new Celebrity().setName(CELEBRITY_ORNELLA_MUTI));

        mockMvc.perform(get("/messages?greetingId=1&celebrityId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.value", is(GREETING_GOOD_AFTERNOON + ", " + CELEBRITY_ORNELLA_MUTI)));
    }
}