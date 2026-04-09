package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;
import tn.esprit.spring.projet_pi_v2.Entity.Fidelity;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.FidelityRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.FidelityResponseDTO;

@Mapper(componentModel = "spring")
public interface FidelityMapper {

    Fidelity toEntity(FidelityRequestDTO dto);

    FidelityResponseDTO toResponseDTO(Fidelity entity);
}
