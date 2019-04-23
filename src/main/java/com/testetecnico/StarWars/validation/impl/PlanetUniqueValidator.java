package com.testetecnico.StarWars.validation.impl;

import com.testetecnico.StarWars.exceptions.ObjectNotFoundException;
import com.testetecnico.StarWars.model.domain.Planet;
import com.testetecnico.StarWars.service.PlanetService;
import com.testetecnico.StarWars.validation.annotations.PlanetUniqueValid;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Guilherme Camargo
 * */
public class PlanetUniqueValidator implements ConstraintValidator<PlanetUniqueValid, Object> {

    @Autowired
    private PlanetService planetService;
    private String name;
    private String message;
    private String id;

    @Override
    public void initialize(PlanetUniqueValid constraintAnnotation) {
        this.name = constraintAnnotation.name();
        this.message = constraintAnnotation.message();
        this.id = constraintAnnotation.id();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean result=false;
        try{
            final String name = BeanUtils.getProperty(o, this.name);
            final String id = BeanUtils.getProperty(o, this.id);
            Planet planet = planetService.findByName(name);
            if(id != null && !id.isEmpty()){
                if(planet.getId()== Long.parseLong(id)){
                    result = true;
                }
            }else {
                result = false;
            }
        }catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e){
            result = false;
        }catch (ObjectNotFoundException e){
            result = true;
        }

        if(!result){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("name").addConstraintViolation();
        }
        return result;
    }
}
