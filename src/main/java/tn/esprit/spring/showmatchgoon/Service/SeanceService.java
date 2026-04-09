package tn.esprit.spring.showmatchgoon.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SeanceRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SeanceResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Cinema;
import tn.esprit.spring.showmatchgoon.Entity.Salle;
import tn.esprit.spring.showmatchgoon.Entity.Seance;
import tn.esprit.spring.showmatchgoon.exception.ResourceNotFoundException;
import tn.esprit.spring.showmatchgoon.mapper.SeanceMapper;
import tn.esprit.spring.showmatchgoon.Repository.CinemaRepository;
import tn.esprit.spring.showmatchgoon.Repository.SalleRepository;
import tn.esprit.spring.showmatchgoon.Repository.SeanceRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final CinemaRepository cinemaRepository;
    private final SalleRepository salleRepository;
    private final SeanceMapper seanceMapper;

    public SeanceResponseDTO create(SeanceRequestDTO request) {
        Seance entity = seanceMapper.toEntity(request);
        Seance saved = seanceRepository.save(entity);
        return enrichResponse(seanceMapper.toResponseDTO(saved), saved);
    }

    public SeanceResponseDTO findById(String id) {
        Seance entity = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance", id));
        return enrichResponse(seanceMapper.toResponseDTO(entity), entity);
    }

    public List<SeanceResponseDTO> findAll() {
        return StreamSupport.stream(seanceRepository.findAll().spliterator(), false)
                .map(entity -> enrichResponse(seanceMapper.toResponseDTO(entity), entity))
                .toList();
    }

    public SeanceResponseDTO update(String id, SeanceRequestDTO request) {
        Seance entity = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance", id));
        seanceMapper.updateEntityFromRequest(request, entity);
        Seance saved = seanceRepository.save(entity);
        return enrichResponse(seanceMapper.toResponseDTO(saved), saved);
    }

    public void deleteById(String id) {
        if (!seanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seance", id);
        }
        seanceRepository.deleteById(id);
    }

    public List<SeanceResponseDTO> findByCinemaId(String cinemaId) {
        return seanceRepository.findByCinemaId(cinemaId).stream()
                .map(entity -> enrichResponse(seanceMapper.toResponseDTO(entity), entity))
                .toList();
    }

    private SeanceResponseDTO enrichResponse(SeanceResponseDTO dto, Seance entity) {
        if (entity.getCinemaId() != null) {
            cinemaRepository.findById(entity.getCinemaId())
                    .map(Cinema::getNom)
                    .ifPresent(dto::setNomCinema);
        }
        if (entity.getSalle() != null) {
            salleRepository.findById(entity.getSalle())
                    .map(Salle::getName)
                    .ifPresent(dto::setNumeroSalle);
        }
        return dto;
    }
}
