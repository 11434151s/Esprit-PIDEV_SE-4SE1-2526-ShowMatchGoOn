package tn.esprit.spring.showmatchgoon.DTOSarah.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeanceRequestDTO {

    @NotNull(message = "La date de la séance est obligatoire")
    @Future(message = "La date de la séance doit être dans le futur")
    private Date dateSeance;

    @NotBlank(message = "L'heure de la séance est obligatoire")
    private String heureSeance;

    @NotBlank(message = "L'ID de la salle est obligatoire")
    private String salleId;

    @NotBlank(message = "L'ID du cinéma est obligatoire")
    private String cinemaId;

    private String contenuId;
}
