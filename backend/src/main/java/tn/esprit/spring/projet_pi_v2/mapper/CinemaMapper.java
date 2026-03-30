package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import tn.esprit.spring.projet_pi_v2.Entity.Cinema;
import tn.esprit.spring.projet_pi_v2.DTO.request.CinemaRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.response.CinemaResponseDTO;

@Mapper(componentModel = "spring")
public interface CinemaMapper {

    Cinema toEntity(CinemaRequestDTO request);

    CinemaResponseDTO toResponseDTO(Cinema entity);

    void updateEntityFromRequest(CinemaRequestDTO request, @MappingTarget Cinema entity);
}
