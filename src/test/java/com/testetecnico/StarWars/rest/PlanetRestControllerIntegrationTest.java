package com.testetecnico.StarWars.rest;

import com.testetecnico.StarWars.StarWarsApiApplication;
import com.testetecnico.StarWars.exceptions.ObjectNotFoundException;
import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.model.dto.ServiceResponseDTO;
import com.testetecnico.StarWars.repository.PlanetRepository;
import com.testetecnico.StarWars.service.Integration.PlanetIntegrationService;
import com.testetecnico.StarWars.service.PlanetService;

import static com.testetecnico.StarWars.builder.PlanetBuilder.*;
import static org.hamcrest.Matchers.*;

import com.testetecnico.StarWars.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.*;
/**
 * @author Guilherme Camargo
 * */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StarWarsApiApplication.class)
@WebAppConfiguration
public class PlanetRestControllerIntegrationTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private PlanetService planetService;
    @MockBean
    private PlanetRepository planetRepository;
    @MockBean
    private RestTemplate restTemplate;

    public List<Planet> planets;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //list with 2 objects
        planets = createList();
    }

    @Test
    public void givenPlanets_whenGetAllPlanets_thenReturn200AndJsonArray() throws Exception{
        given(planetRepository.findAll()).willReturn(planets);
        mockMvc.perform(get("/api/planets/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(planets.get(0).getName())));

    }
    @Test
    public void givenPlanets_whenGetAllPlanetsFromApi_thenReturn200AndJsonArray() throws Exception{
        ResponseEntity<ServiceResponseDTO> response = new ResponseEntity<ServiceResponseDTO>(HttpStatus.OK);
        doReturn(response).when(restTemplate).exchange(eq(anyString()),
                eq(HttpMethod.GET),ArgumentMatchers.any(HttpEntity.class),
                ServiceResponseDTO.class);
        mockMvc.perform(get("/api/planets/service")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
    @Test
    public void givenId_whenGetById_thenReturn200AndJsonObject() throws Exception{
        Planet planet = planets.get(0);
        given(planetRepository.findById(1L)).willReturn(Optional.of(planet));
        mockMvc.perform(get("/api/planets/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(planet.getName())));

    }
    @Test
    public void givenId_whenGetById_thenReturn404() throws Exception{
        given(planetRepository.findById(anyLong())).willThrow(ObjectNotFoundException.class);
        mockMvc.perform(get("/api/planets/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());

    }
    @Test
    public void givenName_whenSearchByName_thenReturn200AndJsonObject() throws Exception{
        Planet planet = planets.get(0);
        given(planetRepository.findByNameIgnoreCase(planet.getName())).willReturn(Optional.of(planet));
        mockMvc.perform(get("/api/planets/search?name="+planet.getName())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(planet.getName())));

    }
    @Test
    public void givenName_whenSearchByName_thenReturn404() throws Exception{
        given(planetRepository.findByNameIgnoreCase(anyString())).willThrow(ObjectNotFoundException.class);
        mockMvc.perform(get("/api/planets/search?name=testName")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());

    }
    @Test
    public void givenId_whenDeleteById_thenReturn201()throws Exception{
        doNothing().when(planetRepository).deleteById(anyLong());
        mockMvc.perform(delete("/api/planets/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
    }
    @Test
    public void givenId_whenDeleteById_thenReturn404()throws Exception{
        doThrow(ObjectNotFoundException.class).when(planetRepository).deleteById(anyLong());
        mockMvc.perform(delete("/api/planets/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
    }
    @Test
    public void givenPlanet_whenInsert_thenReturn201AndHeaderLocation()throws Exception{
        given(planetRepository.save(ArgumentMatchers.any(Planet.class)))
                .willReturn(createDefaultPlanet().withId(1).build());
        mockMvc.perform(post("/api/planets")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtils.mapToJson(createDefaultPlanet().build())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }
    @Test
    public void givenPlanet_whenInsert_thenReturn400AndErrorList()throws Exception{
        mockMvc.perform(post("/api/planets")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtils.mapToJson(createEmptyPlanet().build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", notNullValue()));
    }
}
