package com.example.contentmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Document(collection = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Series extends Content {
    @NotNull(message = "Number of seasons is mandatory")
    @Positive(message = "Number of seasons must be a positive number")
    private Integer numberOfSeasons;
    
    @NotNull(message = "Number of episodes is mandatory")
    @Positive(message = "Number of episodes must be a positive number")
    private Integer numberOfEpisodes;
    
    private Boolean isCompleted = false;
}
