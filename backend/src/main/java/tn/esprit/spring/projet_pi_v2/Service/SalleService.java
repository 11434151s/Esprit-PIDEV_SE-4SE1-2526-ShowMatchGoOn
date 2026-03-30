package tn.esprit.spring.projet_pi_v2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.projet_pi_v2.DTO.request.SalleRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.response.SalleResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Salle;
import tn.esprit.spring.projet_pi_v2.exception.ResourceNotFoundException;
import tn.esprit.spring.projet_pi_v2.mapper.SalleMapper;
import tn.esprit.spring.projet_pi_v2.Repository.SalleRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SalleService {

    private final SalleRepository salleRepository;
    private final SalleMapper salleMapper;

    public SalleResponseDTO create(SalleRequestDTO request) {
        Salle entity = salleMapper.toEntity(request);
        Salle saved = salleRepository.save(entity);
        return salleMapper.toResponseDTO(saved);
    }

    public SalleResponseDTO findById(String id) {
        Salle entity = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle", id));
        return salleMapper.toResponseDTO(entity);
    }

    public List<SalleResponseDTO> findAll() {
        return StreamSupport.stream(salleRepository.findAll().spliterator(), false)
                .map(salleMapper::toResponseDTO)
                .toList();
    }

    public SalleResponseDTO update(String id, SalleRequestDTO request) {
        Salle entity = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle", id));
        salleMapper.updateEntityFromRequest(request, entity);
        Salle saved = salleRepository.save(entity);
        return salleMapper.toResponseDTO(saved);
    }

    public void deleteById(String id) {
        if (!salleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Salle", id);
        }
        salleRepository.deleteById(id);
    }
}
