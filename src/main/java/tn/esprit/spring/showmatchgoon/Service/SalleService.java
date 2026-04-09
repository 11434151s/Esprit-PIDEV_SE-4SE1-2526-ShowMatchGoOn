package tn.esprit.spring.showmatchgoon.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SalleRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SalleResponseDTO;
import tn.esprit.spring.showmatchgoon.Entity.Salle;
import tn.esprit.spring.showmatchgoon.exception.ResourceNotFoundException;
import tn.esprit.spring.showmatchgoon.mapper.SalleMapper;
import tn.esprit.spring.showmatchgoon.Repository.SalleRepository;

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
