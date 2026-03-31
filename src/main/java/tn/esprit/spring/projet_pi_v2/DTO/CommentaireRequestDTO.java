package tn.esprit.spring.projet_pi_v2.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentaireRequestDTO {

    @NotBlank(message = "Le contenu est obligatoire")
    @Size(min = 5, max = 500, message = "Le contenu doit contenir entre 5 et 500 caractères")
    private String contenu;

    @NotBlank(message = "L'identifiant du post est obligatoire")
    private String postId;
}