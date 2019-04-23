package com.testetecnico.StarWars.repository;

import com.testetecnico.StarWars.model.domain.Planet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Guilherme Camargo
 * */
@Repository
public interface PlanetRepository extends CrudRepository<Planet, Long> {
    @Transactional
    Optional<Planet> findByNameIgnoreCase(String name);
}
