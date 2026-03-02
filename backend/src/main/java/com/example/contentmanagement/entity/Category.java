package com.example.contentmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "categorys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    private String id;

    @NotBlank(message = "Category name is mandatory")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;

    @Builder.Default
    private List<String> contentIds = new ArrayList<>();
}
