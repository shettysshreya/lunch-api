package com.task.lunch.util;

import com.task.lunch.service.IngredientService;
import com.task.lunch.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Implementation of CommandLineRunner to load data into the embedded mongoDB
 * before application startup completes
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private IngredientService ingredientService;
    private RecipeService recipeService;

    @Autowired
    public DataLoader(IngredientService ingredientService, RecipeService recipeService){
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Loading data into the DB");
        ingredientService.loadIngredients();
        recipeService.loadRecipes();
    }
}
