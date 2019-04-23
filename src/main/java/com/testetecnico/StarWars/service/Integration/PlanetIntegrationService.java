package com.testetecnico.StarWars.service.Integration;

import com.testetecnico.StarWars.exceptions.ObjectNotFoundException;
import com.testetecnico.StarWars.model.dto.PlanetDTO;
import com.testetecnico.StarWars.model.dto.ServiceResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @author Guilherme Camargo
 * */
@Service
public class PlanetIntegrationService {

    RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://swapi.co/api/";

    public List<PlanetDTO> findAll(){
        ResponseEntity<ServiceResponseDTO> response = restTemplate.exchange(getCompleteUrl("planets/"), HttpMethod.GET,createHeader(), ServiceResponseDTO.class);
        validateResponse(response);
        List<PlanetDTO> planets = response.getBody().getResults();
        planets.forEach(planetDTO -> {
            planetDTO.setQuantity_movies(planetDTO.getFilms().size());});
        return planets;
    }
    public PlanetDTO findByName(String name){
        ResponseEntity<ServiceResponseDTO> response = restTemplate.exchange(getCompleteUrl("planets/?search="+name), HttpMethod.GET,createHeader(), ServiceResponseDTO.class);
        validateResponse(response);
        ServiceResponseDTO serviceResponseDTO = response.getBody();
        return serviceResponseDTO.getResults().get(0);
    }

    private HttpEntity<String> createHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        return entity;
    }
    private void validateResponse(ResponseEntity<ServiceResponseDTO> response){
        if(response.getBody().getResults().isEmpty()){
            throw new ObjectNotFoundException("No Planet was found.");
        }

    }
    private String getCompleteUrl(String resource){
        return BASE_URL+resource;
    }
}
