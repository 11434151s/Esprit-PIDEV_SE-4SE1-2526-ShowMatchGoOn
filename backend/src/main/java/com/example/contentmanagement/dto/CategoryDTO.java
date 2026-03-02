package com.example.contentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private String id;

    @NotBlank(message = "Category name is mandatory")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;
}
