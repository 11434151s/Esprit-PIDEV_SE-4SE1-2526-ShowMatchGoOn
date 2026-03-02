package com.example.contentmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Document(collection = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film extends Content {
    @Positive(message = "Duration must be a positive number")
    private Integer durationInMinutes;
    
    @NotBlank(message = "Director is mandatory")
    private String director;
}
