package tn.esprit.spring.showmatchgoon.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SalleRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SalleResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Salle;

@Component
public class SalleMapper {

    public Salle toEntity(SalleRequestDTO request) {
        Salle entity = new Salle();
        entity.setName(request.getName());
        entity.setCapacity(request.getCapacity());
        return entity;
    }

    public SalleResponseDTO toResponseDTO(Salle entity) {
        return SalleResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .build();
    }

    public void updateEntityFromRequest(SalleRequestDTO request, Salle entity) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getCapacity() != null) entity.setCapacity(request.getCapacity());
    }
}
