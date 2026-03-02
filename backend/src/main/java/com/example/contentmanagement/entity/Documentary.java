package com.example.contentmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Document(collection = "documentarys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Documentary extends Content {
    @NotBlank(message = "Topic is mandatory")
    private String topic;
    
    @NotBlank(message = "Narrator is mandatory")
    private String narrator;
}
