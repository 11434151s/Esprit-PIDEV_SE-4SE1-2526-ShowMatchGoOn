package tn.esprit.spring.showmatchgoon.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SeanceRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SeanceResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Seance;

@Component
public class SeanceMapper {

    public Seance toEntity(SeanceRequestDTO request) {
        Seance entity = new Seance();
        entity.setDateSeance(request.getDateSeance());
        entity.setHeureSeance(request.getHeureSeance());
        entity.setSalle(request.getSalleId());
        entity.setCinemaId(request.getCinemaId());
        entity.setContenuId(request.getContenuId());
        return entity;
    }

    public SeanceResponseDTO toResponseDTO(Seance entity) {
        return SeanceResponseDTO.builder()
                .id(entity.getId())
                .dateSeance(entity.getDateSeance())
                .heureSeance(entity.getHeureSeance())
                .numeroSalle(entity.getSalle())
                .nomCinema(null)
                .contenuId(entity.getContenuId())
                .build();
    }

    public void updateEntityFromRequest(SeanceRequestDTO request, Seance entity) {
        if (request.getDateSeance() != null) entity.setDateSeance(request.getDateSeance());
        if (request.getHeureSeance() != null) entity.setHeureSeance(request.getHeureSeance());
        if (request.getSalleId() != null) entity.setSalle(request.getSalleId());
        if (request.getCinemaId() != null) entity.setCinemaId(request.getCinemaId());
        if (request.getContenuId() != null) entity.setContenuId(request.getContenuId());
    }
}
