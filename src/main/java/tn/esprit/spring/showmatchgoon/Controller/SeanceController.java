package tn.esprit.spring.showmatchgoon.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SeanceRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SeanceResponseDTO;
import tn.esprit.spring.showmatchgoon.Service.SeanceService;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SeanceController {

    private final SeanceService seanceService;

    @PostMapping
    public ResponseEntity<SeanceResponseDTO> create(@Valid @RequestBody SeanceRequestDTO request) {
        SeanceResponseDTO created = seanceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(seanceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeanceResponseDTO>> findAll() {
        return ResponseEntity.ok(seanceService.findAll());
    }

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<SeanceResponseDTO>> findByCinemaId(@PathVariable String cinemaId) {
        return ResponseEntity.ok(seanceService.findByCinemaId(cinemaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody SeanceRequestDTO request) {
        return ResponseEntity.ok(seanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        seanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
