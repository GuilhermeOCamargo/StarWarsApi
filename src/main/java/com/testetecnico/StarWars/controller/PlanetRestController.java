package com.testetecnico.StarWars.controller;

import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.model.dto.PlanetDTO;
import com.testetecnico.StarWars.service.Integration.PlanetIntegrationService;
import com.testetecnico.StarWars.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author Guilherme Camargo
 * */
@RestController
@RequestMapping("/api/planets")
public class PlanetRestController {
    @Autowired
    private PlanetService planetService;
    @Autowired
    private PlanetIntegrationService planetIntegrationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> insert(@RequestBody PlanetDTO dto){
        Planet planet = planetService.insertAsDto(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(planet.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Planet findById(@PathVariable Long id){
        return planetService.findById(id);
    }

    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Planet seachByName(@RequestParam(name = "name", required = true)String name){
        return planetService.findByName(name);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Planet> findAll(){
        return planetService.findAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        planetService.delete(id);
    }

    @GetMapping(value = "/service", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlanetDTO> findAllApi(){
        return planetIntegrationService.findAll();
    }

}
