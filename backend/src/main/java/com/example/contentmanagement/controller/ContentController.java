package com.example.contentmanagement.controller;

import com.example.contentmanagement.dto.*;
import com.example.contentmanagement.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping("/films")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO filmDTO, Authentication authentication) {
        return new ResponseEntity<>(contentService.createFilm(filmDTO, authentication.getName()), HttpStatus.CREATED);
    }

    @PostMapping("/series")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SeriesDTO> createSeries(@Valid @RequestBody SeriesDTO seriesDTO, Authentication authentication) {
        return new ResponseEntity<>(contentService.createSeries(seriesDTO, authentication.getName()), HttpStatus.CREATED);
    }

    @PostMapping("/documentaries")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DocumentaryDTO> createDocumentary(@Valid @RequestBody DocumentaryDTO documentaryDTO, Authentication authentication) {
        return new ResponseEntity<>(contentService.createDocumentary(documentaryDTO, authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentById(String.valueOf(id)));
    }

    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllContent());
    }

    @PutMapping("/films/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable Long id, @Valid @RequestBody FilmDTO filmDTO) {
        return ResponseEntity.ok(contentService.updateFilm(String.valueOf(id), filmDTO));
    }

    @PutMapping("/series/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SeriesDTO> updateSeries(@PathVariable Long id, @Valid @RequestBody SeriesDTO seriesDTO) {
        return ResponseEntity.ok(contentService.updateSeries(String.valueOf(id), seriesDTO));
    }

    @PutMapping("/documentaries/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DocumentaryDTO> updateDocumentary(@PathVariable Long id, @Valid @RequestBody DocumentaryDTO documentaryDTO) {
        return ResponseEntity.ok(contentService.updateDocumentary(String.valueOf(id), documentaryDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
