package tn.esprit.spring.showmatchgoon.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.ReservationRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.ReservationResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Reservation;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDTO request) {
        Reservation entity = new Reservation();
        entity.setUserId(request.getUserId());
        entity.setNumeroPlace(request.getNumeroPlace());
        entity.setPrix(request.getPrix());
        entity.setContenuId(request.getContenuId());
        entity.setWatchPartyId(request.getWatchPartyId());
        return entity;
    }

    public ReservationResponseDTO toResponseDTO(Reservation entity) {
        return ReservationResponseDTO.builder()
                .id(entity.getId())
                .dateReservation(entity.getDateReservation())
                .numeroPlace(entity.getNumeroPlace())
                .statut(entity.getStatut())
                .watchPartyId(entity.getWatchPartyId())
                .contenuId(entity.getContenuId())
                .userId(entity.getUserId())
                .prix(entity.getPrix())
                .build();
    }

    public void updateEntityFromRequest(ReservationRequestDTO request, Reservation entity) {
        if (request.getUserId() != null) entity.setUserId(request.getUserId());
        if (request.getNumeroPlace() != null) entity.setNumeroPlace(request.getNumeroPlace());
        if (request.getPrix() != null) entity.setPrix(request.getPrix());
        if (request.getContenuId() != null) entity.setContenuId(request.getContenuId());
        if (request.getWatchPartyId() != null) entity.setWatchPartyId(request.getWatchPartyId());
    }
}
