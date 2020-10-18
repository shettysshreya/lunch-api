package com.task.lunch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ingredient {

    @NonNull
    @EqualsAndHashCode.Include
    @JsonValue
    private String title;

    @JsonProperty("best-before")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bestBefore;

    @JsonProperty("use-by")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date useBy;
}
