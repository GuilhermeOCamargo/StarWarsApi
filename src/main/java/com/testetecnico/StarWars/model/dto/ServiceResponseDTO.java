package com.testetecnico.StarWars.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guilherme Camargo
 * */
public class ServiceResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Long count;
    @JsonIgnore
    private String next;
    @JsonIgnore
    private String previous;
    private List<PlanetDTO> results;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PlanetDTO> getResults() {
        return results;
    }

    public void setResults(List<PlanetDTO> results) {
        this.results = results;
    }
}
