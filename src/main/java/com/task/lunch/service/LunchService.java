package com.task.lunch.service;

import com.task.lunch.model.Ingredient;
import com.task.lunch.model.Recipe;
import com.task.lunch.model.RecipeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LunchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LunchService.class);

    private RecipeService recipeService;
    private IngredientService ingredientService;

    @Autowired
    public LunchService(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    /**
     * Get all possible recipes based on the available ingredients in the database
     * Recipes are filtered by ingredient's use by date and sorted by best before date if applicable
     * @param filterByUseBy boolean flag to filter recipes whose ingredients are past use by date
     * @param sortByBestBefore boolean flag to sort recipes by ingredients best before date
     * @return List of recipes
     */
    public List<Recipe> getPossibleRecipes(boolean filterByUseBy, boolean sortByBestBefore) {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        //minimumUseByDate is set to yesterday so ingredients with current date as the use by date are considered to be usable
        Date minimumUseByDate = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Ingredient> availableIngredients = filterByUseBy ? ingredientService.getIngredientsByUseBy(minimumUseByDate)
                                                : ingredientService.getAllIngredients();
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (availableIngredients.containsAll(recipe.getIngredients())) {
                result.add(recipe);
            }
        }
        return sortByBestBefore ? sortRecipesByIngredientBestByDateDesc(availableIngredients, result) : result;
    }

    private List<Recipe> sortRecipesByIngredientBestByDateDesc(List<Ingredient> availableIngredients,
                                                               List<Recipe> possibleRecipes) {
        Map<String, Ingredient> ingredientMap = availableIngredients.stream()
                .collect(Collectors.toMap(Ingredient::getTitle, ingredient -> ingredient));
        for (Recipe recipe : possibleRecipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setUseBy(ingredientMap.get(ingredient.getTitle()).getUseBy());
                ingredient.setBestBefore(ingredientMap.get(ingredient.getTitle()).getBestBefore());
            }
        }
        possibleRecipes.sort(new RecipeComparator());
        return possibleRecipes;
    }
}
