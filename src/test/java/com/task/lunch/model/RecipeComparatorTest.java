package com.task.lunch.model;

import com.task.lunch.TestDataManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeComparatorTest {

    private RecipeComparator comparator = new RecipeComparator();

    @Test
    public void testRecipeComparator(){
        LocalDate date = LocalDate.now();

        List<Ingredient> ingredientsForRecipe1 = new ArrayList<>();
        ingredientsForRecipe1.add(TestDataManager.buildIngredient("FreshIngredient1", date.plusDays(10), date.plusDays(15)));
        ingredientsForRecipe1.add(TestDataManager.buildIngredient("StaleIngredient1", date.minusDays(1), date.plusDays(5)));

        List<Ingredient> ingredientsForRecipe2 = new ArrayList<>();
        ingredientsForRecipe2.add(TestDataManager.buildIngredient("FreshIngredient1", date.plusDays(10), date.plusDays(15)));
        ingredientsForRecipe2.add(TestDataManager.buildIngredient("FreshIngredient2", date.plusDays(10), date.plusDays(15)));

        Recipe recipe1 = Recipe.builder().title("RecipeForStaleIngredients").ingredients(ingredientsForRecipe1).build();
        Recipe recipe2 = Recipe.builder().title("RecipeForFreshIngredients").ingredients(ingredientsForRecipe2).build();

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

        recipes.sort(comparator);
        assertEquals("RecipeForFreshIngredients", recipes.get(0).getTitle());
    }
}
