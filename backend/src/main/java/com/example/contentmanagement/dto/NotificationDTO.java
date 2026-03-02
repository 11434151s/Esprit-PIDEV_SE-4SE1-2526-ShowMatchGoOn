package com.example.contentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;

    @NotBlank(message = "Message is mandatory")
    private String message;

    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "INTERNAL|EMAIL|SMS", message = "Type must be INTERNAL, EMAIL, or SMS")
    private String type;

    private LocalDateTime createdAt;

    @Builder.Default
    private Boolean isRead = false;

    @NotNull(message = "User ID is mandatory")
    private String userId;

    private String username;
}
