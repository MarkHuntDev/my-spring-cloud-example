package com.example.controller;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.entity.Celebrity;
import com.example.repository.CelebrityRepository;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class CelebrityControllerTest {

    // Celebrity names
    private static final String CELEBRITY_NAME_CHUCK_NORRIS = "Chuck Norris";
    private static final String CELEBRITY_NAME_ORNELLA_MUTI = "Ornella Muti";
    private static final String CELEBRITY_NAME_ADRIANO_CELENTANO = "Adriano Celentano";

    // Celebrities
    private static final Celebrity CELEBRITY_CHUCK_NORRIS =
            new Celebrity()
                    .setId(1L)
                    .setName(CELEBRITY_NAME_CHUCK_NORRIS);

    private static final Celebrity CELEBRITY_ORNELLA_MUTI =
            new Celebrity()
                    .setId(2L)
                    .setName(CELEBRITY_NAME_ORNELLA_MUTI);

    private static final Celebrity CELEBRITY_ADRIANO_CELENTANO =
            new Celebrity()
                    .setId(3L)
                    .setName(CELEBRITY_NAME_ADRIANO_CELENTANO);

    // Celebrity list
    private static final List<Celebrity> CELEBRITIES = Lists.newArrayList(
            CELEBRITY_CHUCK_NORRIS,
            CELEBRITY_ORNELLA_MUTI,
            CELEBRITY_ADRIANO_CELENTANO);

    private MockMvc mockMvc;

    @Mock
    private CelebrityRepository celebrityRepository;

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

        when(celebrityRepository.findAll()).thenReturn(CELEBRITIES);

        mockMvc.perform(get("/celebrities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(CELEBRITY_NAME_CHUCK_NORRIS)))
                .andExpect(jsonPath("$[1].name", is(CELEBRITY_NAME_ORNELLA_MUTI)))
                .andExpect(jsonPath("$[2].name", is(CELEBRITY_NAME_ADRIANO_CELENTANO)));
    }

    @Test
    public void shouldReturnGoodMorningGreetingByIdZero() throws Exception {

        when(celebrityRepository.findOne(1L)).thenReturn(CELEBRITY_CHUCK_NORRIS);

        mockMvc.perform(get("/celebrities/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(CELEBRITY_NAME_CHUCK_NORRIS)));
    }

    @Test
    public void shouldReturnRandomGreeting() throws Exception {

        when(celebrityRepository.count()).thenReturn(3L);
        when(celebrityRepository.findOne(1L)).thenReturn(CELEBRITY_CHUCK_NORRIS);
        when(celebrityRepository.findOne(2L)).thenReturn(CELEBRITY_ORNELLA_MUTI);
        when(celebrityRepository.findOne(3L)).thenReturn(CELEBRITY_ADRIANO_CELENTANO);

        mockMvc.perform(get("/celebrities/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name",
                        anyOf(is(CELEBRITY_NAME_CHUCK_NORRIS), is(CELEBRITY_NAME_ORNELLA_MUTI), is(CELEBRITY_NAME_ADRIANO_CELENTANO))));
    }
}