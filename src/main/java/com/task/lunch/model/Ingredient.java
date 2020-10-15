package com.task.lunch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {

    private String title;

    @JsonProperty("best-before")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bestBefore;

    @JsonProperty("use-by")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date useBy;
}
