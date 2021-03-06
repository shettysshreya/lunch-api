package com.task.lunch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.lunch.exception.LunchApiException;
import com.task.lunch.model.Ingredient;
import com.task.lunch.repository.IngredientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @Mock
    private IngredientRepository repository;

    @Mock
    private RestTemplate template;
    private IngredientService service;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mapper = new ObjectMapper();
        service = new IngredientService(template, repository, "someUrl");
    }

    @Test
    public void testLoadIngredients() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        Map<String, List<Ingredient>> ingredients = mapper.readValue(
                new File("src/test/resources/ingredients_api_response.json"),
                new TypeReference<>() {});
        ResponseEntity<Map<String, List<Ingredient>>> responseEntity = new ResponseEntity<Map<String, List<Ingredient>>>(
                ingredients,
                header,
                HttpStatus.OK
        );
        Mockito.when(template.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(repository.saveAll(anyList())).thenReturn(ingredients.get("ingredients"));
        List<Ingredient> savedIngredients = service.loadIngredients();
        assertEquals(16, savedIngredients.size());
    }

    @Test
    public void testLoadIngredientsWithAPIError() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map<String, List<Ingredient>>> responseEntity = new ResponseEntity<Map<String, List<Ingredient>>>(
                null,
                header,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        Mockito.when(template.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        LunchApiException exception = Assertions.assertThrows(LunchApiException.class,
                () -> service.loadIngredients());

        assertTrue(exception.getMessage().startsWith("Error loading ingredients"));
    }

    @Test
    public void testGetIngredientsByUseBy(){
        List<Ingredient> mockIngredients = new ArrayList<>();
        mockIngredients.add(Ingredient.builder().title("Ingredient1").build());
        mockIngredients.add(Ingredient.builder().title("Ingredient2").build());
        Mockito.when(repository.findByUseByGreaterThan(any())).thenReturn(mockIngredients);
        List<Ingredient> ingredients = service.getIngredientsByUseBy(new Date());
        assertEquals(2, ingredients.size());
    }

}
