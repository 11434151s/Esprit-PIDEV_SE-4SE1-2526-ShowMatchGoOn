package tn.esprit.spring.projet_pi_v2.DTO;

import lombok.Data;
import java.util.Date;
@Data
public class PostResponseDTO {
    private String id;
    private String titre;
    private String contenu;
    private Date datePublication;

    // ✅ Ajout
    private String authorUsername;
}