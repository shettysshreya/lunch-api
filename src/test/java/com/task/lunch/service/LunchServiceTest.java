package com.task.lunch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.lunch.TestDataManager;
import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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

    @Test
    public void testGetRecipesByUsedByAndBestBefore() {
        List<Ingredient> ingredientsForRecipe1 = new ArrayList<>();
        ingredientsForRecipe1.add(Ingredient.builder().title("FreshIngredient1").build());
        ingredientsForRecipe1.add(Ingredient.builder().title("StaleIngredient1").build());

        List<Ingredient> ingredientsForRecipe2 = new ArrayList<>();
        ingredientsForRecipe2.add(Ingredient.builder().title("FreshIngredient2").build());
        ingredientsForRecipe2.add(Ingredient.builder().title("FreshIngredient3").build());


        Recipe recipe1 = Recipe.builder().title("Recipe1").ingredients(ingredientsForRecipe1).build();
        Recipe recipe2 = Recipe.builder().title("Recipe2").ingredients(ingredientsForRecipe2).build();

        List<Recipe> mockRecipesFromDB = new ArrayList<>();
        mockRecipesFromDB.add(recipe1);
        mockRecipesFromDB.add(recipe2);

        LocalDate date = LocalDate.now();

        List<Ingredient> availableIngredients = new ArrayList<>();
        availableIngredients.add(TestDataManager.buildIngredient("FreshIngredient1", date.plusDays(10), date.plusDays(15)));
        availableIngredients.add(TestDataManager.buildIngredient("FreshIngredient2", date.plusDays(10), date.plusDays(15)));
        availableIngredients.add(TestDataManager.buildIngredient("FreshIngredient3", date.plusDays(10), date.plusDays(15)));
        availableIngredients.add(TestDataManager.buildIngredient("StaleIngredient1", date.minusDays(5), date.plusDays(5)));

        Mockito.when(recipeService.getAllRecipes()).thenReturn(mockRecipesFromDB);
        Mockito.when(ingredientService.getIngredientsByUseBy(any())).thenReturn(availableIngredients);
        List<Recipe> recipes = lunchService.getPossibleRecipes(true, true);
        assertEquals(2, recipes.size());
        assertEquals("Recipe2", recipes.get(0).getTitle());
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
