package tn.esprit.spring.projet_pi_v2.DTORanim;

import jakarta.validation.constraints.*;

public record FeedbackUpdateDTO(
        @NotBlank(message = "Commentaire obligatoire")
        @Size(min = 5, max = 300, message = "Commentaire entre 5 et 300 caractères")
        String commentaire,

        @NotNull(message = "Note obligatoire")
        @Min(value = 1, message = "Note minimum 1")
        @Max(value = 5, message = "Note maximum 5")
        Integer note
) {
}