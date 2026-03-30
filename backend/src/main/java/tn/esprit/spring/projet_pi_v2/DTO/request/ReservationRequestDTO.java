package tn.esprit.spring.projet_pi_v2.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @NotBlank(message = "L'ID de la séance est obligatoire")
    private String seanceId;

    @NotBlank(message = "L'ID de l'utilisateur est obligatoire")
    private String userId;

    @NotBlank(message = "Le numéro de place est obligatoire")
    private String numeroPlace;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être strictement positif")
    private Double prix;

    private String contenuId;
    private String watchPartyId;
}
