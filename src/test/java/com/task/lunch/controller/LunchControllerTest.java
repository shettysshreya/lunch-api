package com.task.lunch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.lunch.exception.LunchApiException;
import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import com.task.lunch.service.LunchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LunchControllerTest {

    private LunchController controller;
    private MockMvc mockMvc;

    @Mock
    private LunchService lunchService;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        controller = new LunchController(lunchService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        Mockito.when(lunchService.getPossibleRecipes(false, false))
                .thenReturn(buildMockRecipes());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lunch").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        List<Recipe> recipes = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(recipes.size(), 2);
    }

    @Test
    public void testGetRecipesByUseBy() throws Exception {
        Mockito.when(lunchService.getPossibleRecipes(true, false))
                .thenReturn(buildMockRecipes());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lunch")
                .param("use-by", "true").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        List<Recipe> recipes = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(recipes.size(), 2);
    }

    @Test
    public void testGetRecipesByUseByAndBestBefore() throws Exception {
        Mockito.when(lunchService.getPossibleRecipes(true, true)).thenReturn(buildMockRecipes());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lunch")
                .param("use-by", "true").param("best-before", "true")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        List<Recipe> recipes = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(recipes.size(), 2);
    }

    @Test
    public void testGetRecipesByBestBeforeOnly() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lunch")
                .param("best-before", "true")
                .accept(MediaType.APPLICATION_JSON);
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(requestBuilder).andReturn();
        });
        assertTrue(exception.getCause().getMessage().startsWith("Invalid"));
    }

    private List<Recipe> buildMockRecipes() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().title("Ingredient1").build());
        ingredients.add(Ingredient.builder().title("Ingredient2").build());

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(Recipe.builder().title("Recipe1").ingredients(ingredients).build());
        recipes.add(Recipe.builder().title("Recipe2").ingredients(ingredients).build());
        return recipes;
    }
}
