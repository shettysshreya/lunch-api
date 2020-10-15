package com.task.lunch.service;

import com.task.lunch.exception.LunchApiException;
import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import com.task.lunch.repository.IngredientRepository;
import com.task.lunch.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    private String recipesApiUrl;
    private RestTemplate restTemplate;
    private RecipeRepository repository;

    @Autowired
    public RecipeService(RestTemplate restTemplate, RecipeRepository repository,
                             @Value("${spring.application.recipes.api.url}")String recipesApiUrl){
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.recipesApiUrl = recipesApiUrl;
    }

    /**
     * Retrieve recipes from the REST API and load into the database
     * @return
     */
    public List<Recipe> loadRecipes() throws LunchApiException {
        LOGGER.info("Retrieving recipes from:" + recipesApiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, List<Recipe>>> recipeResponse =
                restTemplate.exchange(recipesApiUrl, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<Map<String, List<Recipe>>>() {
                        });
        if (recipeResponse.getStatusCode() != HttpStatus.OK || !recipeResponse.hasBody()
                || CollectionUtils.isEmpty(recipeResponse.getBody().get("recipes"))) {
            throw new LunchApiException("Error loading recipes, code:" + recipeResponse.getStatusCode()
                    + ",message:" + recipeResponse.getBody());
        }
        List<Recipe> recipes = recipeResponse.getBody().get("recipes");
        LOGGER.info("Recipes size:" + recipes.size());
        return repository.saveAll(recipes);
    }
}
