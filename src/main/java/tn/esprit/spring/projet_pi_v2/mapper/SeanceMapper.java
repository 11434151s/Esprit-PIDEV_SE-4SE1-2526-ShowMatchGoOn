package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.spring.projet_pi_v2.Entity.Seance;
import tn.esprit.spring.projet_pi_v2.DTO.request.SeanceRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.response.SeanceResponseDTO;

@Mapper(componentModel = "spring")
public interface SeanceMapper {

    @Mapping(target = "salle", source = "salleId")
    @Mapping(target = "cinemaId", source = "cinemaId")
    Seance toEntity(SeanceRequestDTO request);

    @Mapping(target = "numeroSalle", ignore = true)
    @Mapping(target = "nomCinema", ignore = true)
    SeanceResponseDTO toResponseDTO(Seance entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salle", source = "salleId")
    @Mapping(target = "cinemaId", source = "cinemaId")
    void updateEntityFromRequest(SeanceRequestDTO request, @MappingTarget Seance entity);
}
