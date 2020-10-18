package com.task.lunch.controller;

import com.task.lunch.model.Recipe;
import com.task.lunch.service.LunchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LunchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LunchController.class);

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @GetMapping("/lunch")
    public List<Recipe> getRecipes(@RequestParam(value="use-by", required = false, defaultValue = "false") boolean useBy,
                                   @RequestParam(value="best-before", required = false, defaultValue = "false") boolean bestBefore) {
        return lunchService.getPossibleRecipes(useBy, bestBefore);
    }

}
