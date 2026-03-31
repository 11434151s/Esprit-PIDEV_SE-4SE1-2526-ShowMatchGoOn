package tn.esprit.spring.projet_pi_v2.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTO.UserResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Role;
import tn.esprit.spring.projet_pi_v2.ServiceImpl.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import tn.esprit.spring.projet_pi_v2.DTO.UpdateProfileRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    // ✅ Liste tous les users — ADMIN seulement
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ Changer le rôle — ADMIN seulement
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> changeRole(
            @PathVariable String id,
            @RequestParam String role) {
        return ResponseEntity.ok(userService.changeRole(id, role));
    }

    // ✅ Bloquer/Débloquer — ADMIN seulement
    @PutMapping("/{id}/toggle-block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> toggleBlock(@PathVariable String id) {
        return ResponseEntity.ok(userService.toggleBlock(id));
    }

    // ✅ Supprimer — ADMIN seulement
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Voir son propre profil
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    // ✅ Modifier son profil
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @RequestBody UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return ResponseEntity.ok(userService.updateProfile(email, request));
    }
}