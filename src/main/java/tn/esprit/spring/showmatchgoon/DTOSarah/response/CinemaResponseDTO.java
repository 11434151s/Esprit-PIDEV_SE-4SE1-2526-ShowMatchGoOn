package tn.esprit.spring.showmatchgoon.DTOSarah.response;

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
