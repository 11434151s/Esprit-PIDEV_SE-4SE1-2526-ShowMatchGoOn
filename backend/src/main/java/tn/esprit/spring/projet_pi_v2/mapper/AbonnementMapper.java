package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;


import tn.esprit.spring.projet_pi_v2.Entity.Abonnement;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.AbonnementRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.AbonnementResponseDTO;

@Mapper(componentModel = "spring")
public interface AbonnementMapper {

    Abonnement toEntity(AbonnementRequestDTO dto);

    AbonnementResponseDTO toResponseDTO(Abonnement entity);
}