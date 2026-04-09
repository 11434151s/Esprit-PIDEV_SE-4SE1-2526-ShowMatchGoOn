package tn.esprit.spring.showmatchgoon.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.ReservationRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.ReservationResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Reservation;
import tn.esprit.spring.showmatchgoon.exception.ResourceNotFoundException;
import tn.esprit.spring.showmatchgoon.mapper.ReservationMapper;
import tn.esprit.spring.showmatchgoon.Repository.ReservationRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationResponseDTO create(ReservationRequestDTO request) {
        Reservation entity = reservationMapper.toEntity(request);
        entity.setDateReservation(new Date());
        entity.setStatut("CONFIRMEE");
        Reservation saved = reservationRepository.save(entity);
        return enrichResponse(reservationMapper.toResponseDTO(saved), saved);
    }

    public ReservationResponseDTO findById(String id) {
        Reservation entity = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return enrichResponse(reservationMapper.toResponseDTO(entity), entity);
    }

    public List<ReservationResponseDTO> findAll() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .map(entity -> enrichResponse(reservationMapper.toResponseDTO(entity), entity))
                .toList();
    }

    public List<ReservationResponseDTO> findByUserId(String userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(entity -> enrichResponse(reservationMapper.toResponseDTO(entity), entity))
                .toList();
    }

    public ReservationResponseDTO update(String id, ReservationRequestDTO request) {
        Reservation entity = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservationMapper.updateEntityFromRequest(request, entity);
        Reservation saved = reservationRepository.save(entity);
        return enrichResponse(reservationMapper.toResponseDTO(saved), saved);
    }

    public void deleteById(String id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    private ReservationResponseDTO enrichResponse(ReservationResponseDTO dto, Reservation entity) {
        // Enrichissement à partir de la séance si un lien existe (ex: via contenuId ou autre)
        // L'entité Reservation n'a pas de seanceId; on peut enrichir si vous ajoutez ce champ plus tard
        return dto;
    }
}
