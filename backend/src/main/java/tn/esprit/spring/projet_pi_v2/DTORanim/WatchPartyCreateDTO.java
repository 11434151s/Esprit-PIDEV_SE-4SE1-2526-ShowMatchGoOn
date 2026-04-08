package tn.esprit.spring.projet_pi_v2.DTORanim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WatchPartyCreateDTO(

        @NotBlank(message = "Le titre est obligatoire")
        @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
        String titre,

        @NotBlank(message = "Le contenuId est obligatoire")
        String contenuId
) {
}