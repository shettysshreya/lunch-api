package com.task.lunch.controller;

import com.task.lunch.exception.LunchApiException;
import com.task.lunch.model.Recipe;
import com.task.lunch.service.LunchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    public List<Recipe> getRecipes(@Validated @RequestParam(value = "use-by", required = false, defaultValue = "false") boolean useBy,
                                   @Validated @RequestParam(value = "best-before", required = false, defaultValue = "false") boolean bestBefore)
            throws LunchApiException {
        LOGGER.info(String.format("Received request with use-by:%s, best-before:%s", useBy, bestBefore));
        //Disable sort by best-before if use-by filter is not used
        if (bestBefore && !useBy) {
            String errorMessage = "Invalid request param, best-before cannot be used without use-by filter";
            LOGGER.error(errorMessage);
            throw new LunchApiException(errorMessage);
        }
        return lunchService.getPossibleRecipes(useBy, bestBefore);
    }
}
