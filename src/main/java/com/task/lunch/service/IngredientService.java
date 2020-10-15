package com.task.lunch.service;

import com.task.lunch.LunchApplication;
import com.task.lunch.exception.LunchApiException;
import com.task.lunch.model.Ingredient;
import com.task.lunch.repository.IngredientRepository;
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
public class IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LunchApplication.class);

    private String ingredientsApiUrl;
    private RestTemplate restTemplate;
    private IngredientRepository repository;

    @Autowired
    public IngredientService(RestTemplate restTemplate, IngredientRepository repository,
                             @Value("${spring.application.ingredients.api.url}")String ingredientsApiUrl){
      this.restTemplate = restTemplate;
      this.repository = repository;
      this.ingredientsApiUrl = ingredientsApiUrl;
    }

    /**
     * Retrieve ingredients from the REST API and load into the database
     * @return
     */
    public List<Ingredient> loadIngredients() throws LunchApiException {
        LOGGER.info("Retrieving ingredients from:" + ingredientsApiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, List<Ingredient>>> ingredientResponse =
                restTemplate.exchange(ingredientsApiUrl, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<Map<String, List<Ingredient>>>() {
                        });
        if (ingredientResponse.getStatusCode() != HttpStatus.OK || !ingredientResponse.hasBody()
                || CollectionUtils.isEmpty(ingredientResponse.getBody().get("ingredients"))) {
            throw new LunchApiException("Error loading ingredients, code:" + ingredientResponse.getStatusCode()
                    + ",message:" + ingredientResponse.getBody());
        }
        List<Ingredient> ingredients = ingredientResponse.getBody().get("ingredients");
        LOGGER.info("Ingredients size:" + ingredients.size());
        return repository.saveAll(ingredients);
    }
}
