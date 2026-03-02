package com.example.contentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDTO extends ContentDTO {
    @Positive(message = "Duration must be positive")
    private Integer durationInMinutes;
    
    @NotBlank(message = "Director is mandatory")
    private String director;
}
