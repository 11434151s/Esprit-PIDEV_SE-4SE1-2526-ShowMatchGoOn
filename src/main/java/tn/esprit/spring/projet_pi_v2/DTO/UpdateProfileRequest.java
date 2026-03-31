package tn.esprit.spring.projet_pi_v2.DTO;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String email;
    private String photoUrl; // ✅ Ajout
}