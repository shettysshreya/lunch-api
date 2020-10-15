package com.task.lunch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    private String title;
    private List<Ingredient> ingredients;
}
