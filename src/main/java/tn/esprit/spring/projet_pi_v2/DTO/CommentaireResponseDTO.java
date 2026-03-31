package tn.esprit.spring.projet_pi_v2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentaireResponseDTO {

    private String id;
    private String contenu;
    private String postId;
    private String authorUsername; // ✅ Ajout
    private Date dateCommentaire;
}