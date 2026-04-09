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
public class ReservationResponseDTO {

    private String id;
    private Date dateReservation;
    private String numeroPlace;
    private String statut;
    private Double prix;
    private String userId;
    private String contenuId;
    private String watchPartyId;
    // Informations utiles (remplies par le service à partir de Seance/Cinema/Salle)
    private String nomCinema;
    private String numeroSalle;
    private Date dateSeance;
    private String heureSeance;
}
