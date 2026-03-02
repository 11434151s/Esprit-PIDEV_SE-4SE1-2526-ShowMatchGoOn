package com.example.contentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContentDTO {
    private String id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private LocalDateTime releaseDate;

    @NotNull(message = "Category ID is mandatory")
    private String categoryId;

    private String categoryName;

    private String addedById;

    private String addedByUsername;

    @Pattern(regexp = "FILM|SERIES|DOCUMENTARY", message = "Content type must be FILM, SERIES, or DOCUMENTARY")
    private String contentType;
}
