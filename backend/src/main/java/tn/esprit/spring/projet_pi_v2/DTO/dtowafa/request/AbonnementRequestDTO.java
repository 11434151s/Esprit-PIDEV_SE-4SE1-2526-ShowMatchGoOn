package tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.spring.projet_pi_v2.Entity.AbonnementType;

@Getter
@Setter
public class AbonnementRequestDTO {

    @NotNull(message = "Le type est obligatoire")
    private AbonnementType type;

    @Positive(message = "Le prix doit être positif")
    private double prix;

    @NotBlank(message = "La description est obligatoire")
    private String description;
}