package com.task.lunch;

import com.task.lunch.model.Ingredient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TestDataManager {

    public static Ingredient buildIngredient(String title, LocalDate bestBefore, LocalDate useBy){
        Date bestBeforeDate = Date.from(bestBefore.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date useByDate = Date.from(useBy.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Ingredient.builder().title(title).useBy(useByDate).bestBefore(bestBeforeDate).build();
    }
}
