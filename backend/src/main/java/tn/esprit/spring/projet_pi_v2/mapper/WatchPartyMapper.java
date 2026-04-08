package tn.esprit.spring.projet_pi_v2.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.WatchParty;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchPartyMapper {

    // Les champs ont les mêmes noms → MapStruct mappe automatiquement
    WatchPartyResponseDTO toResponseDTO(WatchParty watchParty);
}