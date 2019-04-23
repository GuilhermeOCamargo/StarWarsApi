package com.testetecnico.StarWars.builder;

import com.google.common.collect.Lists;
import com.testetecnico.StarWars.model.domain.Planet;

import java.util.List;

/**
 * @author Guilherme Camargo
 * */
public class PlanetBuilder {
    private static Planet planet;
    private static PlanetBuilder builder;

    private PlanetBuilder(){
    }
    public static PlanetBuilder withId(long id){
        builder.planet.setId(id);
        return builder;
    }
    public static PlanetBuilder withName(String name){
        builder.planet.setName(name);
        return builder;
    }
    public static PlanetBuilder withClimate(String climate){
        builder.planet.setClimate(climate);
        return builder;
    }
    public static PlanetBuilder withTerrain(String terrain){
        builder.planet.setTerrain(terrain);
        return builder;
    }
    public static PlanetBuilder withQuantityMovies(int quantityMovies){
        builder.planet.setQuantityMovies(quantityMovies);
        return builder;
    }
    public static Planet build(){
        return planet;
    }
    public static PlanetBuilder createDefaultPlanet(){
        builder = new PlanetBuilder();
        builder.planet = new Planet();
        planet = new Planet();
        return withName("Tatooine").withClimate("Arid")
               .withTerrain("Desert").withQuantityMovies(5);
    }
    public static PlanetBuilder createEmptyPlanet(){
        planet = new Planet();
        return builder;
    }
    public static List<Planet> createList(){
        return Lists.newArrayList(
                createDefaultPlanet().build(),
                createEmptyPlanet().withName("Alderaan").withClimate("temperate")
                        .withTerrain("jungle, rainforests").build()
        );
    }

}
