package com.example.contentmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Content {
    @Id
    private String id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private LocalDateTime releaseDate;

    @DBRef
    @NotNull(message = "Category is mandatory")
    private Category category;

    @DBRef
    @NotNull(message = "User is mandatory")
    private User addedBy;

    private List<Comment> comments = new ArrayList<>();
}
