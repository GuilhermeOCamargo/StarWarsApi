package com.testetecnico.StarWars.rest;

import static com.testetecnico.StarWars.builder.PlanetBuilder.createDefaultPlanet;
import static com.testetecnico.StarWars.builder.PlanetBuilder.createEmptyPlanet;

import com.google.common.collect.Lists;
import com.testetecnico.StarWars.controller.PlanetRestController;
import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.service.PlanetService;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.*;
/**
 * @author Guilherme Camargo
 * */
@RunWith(SpringRunner.class)
@WebMvcTest(PlanetRestController.class)
public class PlanetRestControllerIntegrationTest {

    @TestConfiguration
    static class PlanetServiceImplTestContextConfiguration {
        @Bean
        public PlanetService planetService() {
            return new PlanetService();
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlanetService planetService;

    @Test
    public void givenPlanets_whenGetAllPlanets_thenReturnJsonArray() throws Exception{
        Planet planet = createDefaultPlanet().build();
        given(planetService.findAll()).willReturn(Lists.newArrayList(planet));
        mockMvc.perform(get("/api/planets/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(planet.getName())));

    }
}
