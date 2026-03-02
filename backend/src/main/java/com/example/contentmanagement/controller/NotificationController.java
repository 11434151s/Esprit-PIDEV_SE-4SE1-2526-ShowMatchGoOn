package com.example.contentmanagement.controller;

import com.example.contentmanagement.dto.NotificationDTO;
import com.example.contentmanagement.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        return new ResponseEntity<>(notificationService.createNotification(notificationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(String.valueOf(userId)));
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
