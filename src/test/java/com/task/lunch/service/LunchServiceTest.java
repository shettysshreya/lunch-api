package com.task.lunch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LunchServiceTest {
    @Mock
    private IngredientService ingredientService;

    @Mock
    private RecipeService recipeService;

    private LunchService lunchService;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        lunchService = new LunchService(recipeService, ingredientService);
    }

    @Test
    public void testGetRecipesForAllAvailableIngredients() throws Exception{
        Mockito.when(recipeService.getAllRecipes()).thenReturn(buildMockRecipes());
        Mockito.when(ingredientService.getAllIngredients()).thenReturn(buildMockIngredients());
        List<Recipe> recipes = lunchService.getPossibleRecipes(false, false);
        assertEquals(3, recipes.size());
    }

    private List<Recipe> buildMockRecipes() throws Exception {
        Map<String, List<Recipe>> recipes = mapper.readValue(
                new File("src/test/resources/recipes_api_response.json"),
                new TypeReference<>() {});
        return recipes.get("recipes");
    }

    private List<Ingredient> buildMockIngredients() throws Exception {
        Map<String, List<Ingredient>> ingredients = mapper.readValue(
                new File("src/test/resources/ingredients_api_response.json"),
                new TypeReference<>() {});
        return ingredients.get("ingredients");
    }
}
