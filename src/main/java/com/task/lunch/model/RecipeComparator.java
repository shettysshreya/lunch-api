package com.task.lunch.model;

import java.util.Comparator;
import java.util.Date;

/**
 * Comparator implementation to compare recipes and
 * sort by best before date of the corresponding ingredients
 */
public class RecipeComparator implements Comparator<Recipe> {

    @Override
    public int compare(Recipe recipe1, Recipe recipe2) {
        Date minDateRecipe1 = recipe1.getIngredients().stream()
                .map(ingredient -> ingredient.getBestBefore()).min(Date::compareTo).get();
        Date minDateRecipe2 = recipe2.getIngredients().stream()
                .map(ingredient -> ingredient.getBestBefore()).min(Date::compareTo).get();
        return  minDateRecipe2.compareTo(minDateRecipe1);
    }
}
