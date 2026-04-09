package tn.esprit.spring.showmatchgoon.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.CinemaRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.CinemaResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Cinema;

@Component
public class CinemaMapper {

    public Cinema toEntity(CinemaRequestDTO request) {
        Cinema entity = new Cinema();
        entity.setNom(request.getNom());
        entity.setAdresse(request.getAdresse());
        entity.setVille(request.getVille());
        return entity;
    }

    public CinemaResponseDTO toResponseDTO(Cinema entity) {
        return CinemaResponseDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .adresse(entity.getAdresse())
                .ville(entity.getVille())
                .build();
    }

    public void updateEntityFromRequest(CinemaRequestDTO request, Cinema entity) {
        if (request.getNom() != null) {
            entity.setNom(request.getNom());
        }
        if (request.getAdresse() != null) {
            entity.setAdresse(request.getAdresse());
        }
        if (request.getVille() != null) {
            entity.setVille(request.getVille());
        }
    }
}
