package com.testetecnico.StarWars.builders;

import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.model.dto.PlanetDTO;

/**
 * @author Guilherne Camargo
 * */
public class PlanetBuilder {
    private Planet planet;

    public PlanetBuilder(Planet planet) {
        this.planet = planet;
    }
    public PlanetBuilder(PlanetDTO dto) {
        this.planet = new Planet(dto.getName(),
                dto.getClimate(), dto.getTerrain());
    }
    public PlanetBuilder quantityMovies(int quantityMovies){
        this.planet.setQuantityMovies(quantityMovies);
        return this;
    }
    public PlanetBuilder updateObject(PlanetDTO dto){
        this.planet.setClimate(dto.getClimate());
        this.planet.setName(dto.getName());
        this.planet.setTerrain(dto.getTerrain());
        return this;
    }
    public Planet build(){
        return this.planet;
    }
}
