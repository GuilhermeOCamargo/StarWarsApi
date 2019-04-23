package com.testetecnico.StarWars.service;

import com.google.common.collect.Lists;
import com.testetecnico.StarWars.builders.PlanetBuilder;
import com.testetecnico.StarWars.exceptions.DatabaseException;
import com.testetecnico.StarWars.exceptions.ObjectNotFoundException;
import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.model.dto.PlanetDTO;
import com.testetecnico.StarWars.repository.PlanetRepository;
import com.testetecnico.StarWars.service.Integration.PlanetIntegrationService;
import com.testetecnico.StarWars.validation.OnCreate;
import com.testetecnico.StarWars.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Guilherme Camargo
 * */
@Service
@Validated
public class PlanetService {
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private PlanetIntegrationService planetIntegrationService;

    private Planet save(Planet planet){
        try{
           return planetRepository.save(planet);
        }catch (Exception e){
            throw new DatabaseException("Erro ao salvar dados.", e);
        }
    }
    public void delete(Long id){
        try{
            planetRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ObjectNotFoundException("Planet not found.");
        }
    }

    public Planet findById(Long id) throws ObjectNotFoundException{
        Optional<Planet> planet = planetRepository.findById(id);
        return planet.orElseThrow(() -> new ObjectNotFoundException("Planet not found."));
    }
    public List<Planet> findAll(){
        List<Planet> planets = Lists.newArrayList(planetRepository.findAll());
        if(planets.isEmpty()){
            throw new ObjectNotFoundException("No Planet was found.");
        }
        return planets;
    }
    public Planet findByName(String name){
        return planetRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ObjectNotFoundException("Planet not found."));
    }

    @Validated(OnCreate.class)
    public Planet insertAsDto(@Valid PlanetDTO dto){
        return save(fromDto(dto));
    }
    @Validated(OnUpdate.class)
    public Planet updateAsDto(@Valid PlanetDTO dto){
        return save(fromDto(dto));
    }
    private Planet fromDto(PlanetDTO dto){
        if(dto.getId()!= null){
           return new PlanetBuilder(findById(dto.getId())).updateObject(dto).build();
        }else{
            int quantity;
            try{
                quantity = planetIntegrationService
                        .findByName(dto.getName()).getFilms().size();
            }catch (ObjectNotFoundException e){
                quantity = 0;
            }
            return new PlanetBuilder(dto).quantityMovies(quantity).build();
        }
    }
}
