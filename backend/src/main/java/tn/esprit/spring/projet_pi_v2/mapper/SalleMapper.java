package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import tn.esprit.spring.projet_pi_v2.Entity.Salle;
import tn.esprit.spring.projet_pi_v2.DTO.request.SalleRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.response.SalleResponseDTO;

@Mapper(componentModel = "spring")
public interface SalleMapper {

    Salle toEntity(SalleRequestDTO request);

    SalleResponseDTO toResponseDTO(Salle entity);

    void updateEntityFromRequest(SalleRequestDTO request, @MappingTarget Salle entity);
}
