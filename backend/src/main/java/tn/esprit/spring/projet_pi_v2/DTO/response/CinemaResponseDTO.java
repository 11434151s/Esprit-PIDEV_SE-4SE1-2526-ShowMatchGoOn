package tn.esprit.spring.projet_pi_v2.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaResponseDTO {

    private String id;
    private String nom;
    private String adresse;
    private String ville;
}
