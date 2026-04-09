package tn.esprit.spring.showmatchgoon.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.CinemaRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.CinemaResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Cinema;
import tn.esprit.spring.showmatchgoon.mapper.CinemaMapper;
import tn.esprit.spring.showmatchgoon.exception.ResourceNotFoundException;
import tn.esprit.spring.showmatchgoon.Repository.CinemaRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    public CinemaResponseDTO create(CinemaRequestDTO request) {
        Cinema entity = cinemaMapper.toEntity(request);
        Cinema saved = cinemaRepository.save(entity);
        return cinemaMapper.toResponseDTO(saved);
    }

    public CinemaResponseDTO findById(String id) {
        Cinema entity = cinemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema", id));
        return cinemaMapper.toResponseDTO(entity);
    }

    public List<CinemaResponseDTO> findAll() {
        return StreamSupport.stream(cinemaRepository.findAll().spliterator(), false)
                .map(cinemaMapper::toResponseDTO)
                .toList();
    }

    public CinemaResponseDTO update(String id, CinemaRequestDTO request) {
        Cinema entity = cinemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema", id));
        cinemaMapper.updateEntityFromRequest(request, entity);
        Cinema saved = cinemaRepository.save(entity);
        return cinemaMapper.toResponseDTO(saved);
    }

    public void deleteById(String id) {
        if (!cinemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cinema", id);
        }
        cinemaRepository.deleteById(id);
    }
}
