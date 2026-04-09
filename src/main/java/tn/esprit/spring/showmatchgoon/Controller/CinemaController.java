package tn.esprit.spring.showmatchgoon.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.CinemaRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.CinemaResponseDTO;
import tn.esprit.spring.showmatchgoon.Service.CinemaService;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CinemaController {

    private final CinemaService cinemaService;

    @PostMapping
    public ResponseEntity<CinemaResponseDTO> create(@Valid @RequestBody CinemaRequestDTO request) {
        CinemaResponseDTO created = cinemaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(cinemaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CinemaResponseDTO>> findAll() {
        return ResponseEntity.ok(cinemaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CinemaResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody CinemaRequestDTO request) {
        return ResponseEntity.ok(cinemaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        cinemaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
