package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.spring.projet_pi_v2.Entity.Reservation;
import tn.esprit.spring.projet_pi_v2.DTO.request.ReservationRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.response.ReservationResponseDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReservation", ignore = true)
    @Mapping(target = "statut", ignore = true)
    Reservation toEntity(ReservationRequestDTO request);

    @Mapping(target = "nomCinema", ignore = true)
    @Mapping(target = "numeroSalle", ignore = true)
    @Mapping(target = "dateSeance", ignore = true)
    @Mapping(target = "heureSeance", ignore = true)
    ReservationResponseDTO toResponseDTO(Reservation entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReservation", ignore = true)
    @Mapping(target = "statut", ignore = true)
    void updateEntityFromRequest(ReservationRequestDTO request, @MappingTarget Reservation entity);
}
