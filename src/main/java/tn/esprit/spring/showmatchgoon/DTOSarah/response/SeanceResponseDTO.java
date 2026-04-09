package tn.esprit.spring.showmatchgoon.DTOSarah.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeanceResponseDTO {

    private String id;
    private Date dateSeance;
    private String heureSeance;
    private String numeroSalle;
    private String nomCinema;
    private String contenuId;
}
