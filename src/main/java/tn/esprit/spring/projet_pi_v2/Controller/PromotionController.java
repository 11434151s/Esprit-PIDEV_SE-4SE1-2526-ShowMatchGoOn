package tn.esprit.spring.projet_pi_v2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionResponseDTO;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.PromotionService;
import java.util.List;

@RestController
@RequestMapping("/api/promotions") // ✅ Préfixe /api cohérent
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    // ✅ ADMIN seulement — créer une promo
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PromotionResponseDTO> create(
            @Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.status(201).body(promotionService.createPromotion(dto));
    }

    // Public — voir toutes les promos actives
    @GetMapping
    public ResponseEntity<List<PromotionResponseDTO>> getActive() {
        return ResponseEntity.ok(promotionService.getActivePromotions());
    }

    // ✅ ADMIN seulement — voir toutes les promos (actives + inactives)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PromotionResponseDTO>> getAll() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    // Public — voir une promo par ID
    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    // Public — vérifier un code promo
    @GetMapping("/code/{code}")
    public ResponseEntity<PromotionResponseDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(promotionService.getPromotionByCode(code));
    }

    // ✅ ADMIN seulement — modifier
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PromotionResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, dto));
    }

    // ✅ ADMIN seulement — désactiver sans supprimer (US-P06)
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        promotionService.deactivatePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ ADMIN seulement — supprimer
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // Voir les promos d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PromotionResponseDTO>> getByClient(
            @PathVariable String clientId) {
        return ResponseEntity.ok(promotionService.getPromotionsByClient(clientId));
    }
}