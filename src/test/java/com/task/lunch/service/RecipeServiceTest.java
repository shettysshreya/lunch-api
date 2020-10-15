package com.task.lunch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import com.task.lunch.repository.IngredientRepository;
import com.task.lunch.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private static RecipeRepository repository;

    @Mock
    private RestTemplate template;
    private RecipeService service;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mapper = new ObjectMapper();
        service = new RecipeService(template, repository, "someUrl");
    }

    @Test
    public void testLoadIngredients() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        Map<String, List<Recipe>> recipes = mapper.readValue(new File("src/test/resources/recipes_api_response.json"),
                new TypeReference<Map<String, List<Recipe>>>() {});
        ResponseEntity<Map<String, List<Recipe>>> responseEntity = new ResponseEntity<Map<String, List<Recipe>>>(
                recipes,
                header,
                HttpStatus.OK
        );
        Mockito.when(template.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(repository.saveAll(anyList())).thenReturn(recipes.get("recipes"));
        List<Recipe> savedRecipes = service.loadRecipes();
        assertEquals(5, savedRecipes.size());
    }

}
