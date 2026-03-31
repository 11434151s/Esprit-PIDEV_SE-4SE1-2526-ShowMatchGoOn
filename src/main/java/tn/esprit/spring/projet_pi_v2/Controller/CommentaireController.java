package tn.esprit.spring.projet_pi_v2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireResponseDTO;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.CommentaireService;
import java.util.List;

@RestController
@RequestMapping("/api/commentaires") // ✅ Cohérent avec /api/posts
@RequiredArgsConstructor
public class CommentaireController {

    private final CommentaireService commentaireService;

    @PostMapping
    public ResponseEntity<CommentaireResponseDTO> create(
            @Valid @RequestBody CommentaireRequestDTO dto) {
        return ResponseEntity.status(201).body(commentaireService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CommentaireResponseDTO>> getAll() {
        return ResponseEntity.ok(commentaireService.getAll());
    }

    // ✅ Ajout — récupérer un commentaire par ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentaireResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(commentaireService.getById(id));
    }

    // ✅ Ajout — récupérer tous les commentaires d'un post (très utile côté Angular)
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentaireResponseDTO>> getByPostId(
            @PathVariable String postId) {
        return ResponseEntity.ok(commentaireService.getByPostId(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentaireResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody CommentaireRequestDTO dto) {
        return ResponseEntity.ok(commentaireService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commentaireService.delete(id);
        return ResponseEntity.noContent().build();
    }
}