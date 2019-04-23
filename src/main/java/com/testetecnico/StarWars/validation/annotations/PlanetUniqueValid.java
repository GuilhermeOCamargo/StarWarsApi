package com.testetecnico.StarWars.validation.annotations;

import com.testetecnico.StarWars.validation.impl.PlanetUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author Guilherme Camargo
 * */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlanetUniqueValidator.class)
public @interface PlanetUniqueValid {
    String message()default "Planet with name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
    String name() default "";
    String id() default "";
}
