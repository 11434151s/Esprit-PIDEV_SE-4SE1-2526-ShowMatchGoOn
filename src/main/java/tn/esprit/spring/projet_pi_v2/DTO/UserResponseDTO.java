package tn.esprit.spring.projet_pi_v2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.spring.projet_pi_v2.Entity.Role;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String email;
    private String username;
    private Role role;
    private Date dateInscription;
    private boolean blocked;
    private String photoUrl; // ✅ Ajout
}