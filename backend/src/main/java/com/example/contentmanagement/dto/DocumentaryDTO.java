package com.example.contentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentaryDTO extends ContentDTO {
    @NotBlank(message = "Topic is mandatory")
    private String topic;
    
    @NotBlank(message = "Narrator is mandatory")
    private String narrator;
}
