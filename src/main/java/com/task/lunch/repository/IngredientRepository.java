package com.task.lunch.repository;

import com.task.lunch.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {

    List<Ingredient> findAll();
    List<Ingredient> findByUseByGreaterThan(Date today);
}
