package tn.esprit.spring.projet_pi_v2.DTORanim;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackCreateDTO(

        @NotBlank(message = "Le commentaire est obligatoire")
        @Size(min = 5, max = 300, message = "Le commentaire doit contenir entre 5 et 300 caractères")
        String commentaire,

        @NotNull(message = "La note est obligatoire")
        @Min(value = 1, message = "La note minimum est 1")
        @Max(value = 5, message = "La note maximum est 5")
        Integer note,

        @NotBlank(message = "Le watchPartyId est obligatoire")
        String watchPartyId
) {
}