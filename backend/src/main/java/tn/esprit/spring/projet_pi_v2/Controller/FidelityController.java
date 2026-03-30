package tn.esprit.spring.projet_pi_v2.Controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.FidelityRequestDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Fidelity;

import java.util.List;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.FidelityResponseDTO;
import tn.esprit.spring.projet_pi_v2.mapper.FidelityMapper;
import tn.esprit.spring.projet_pi_v2.service.FidelityService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fidelities")
@CrossOrigin("*")
public class FidelityController {

    @Autowired
    private FidelityService service;

    @Autowired
    private FidelityMapper mapper;

    // ✅ CREATE
    @PostMapping
    public FidelityResponseDTO create(
            @Valid @RequestBody FidelityRequestDTO dto) {

        Fidelity entity = mapper.toEntity(dto);

        Fidelity saved = service.addFidelity(entity);

        return mapper.toResponseDTO(saved);
    }

    // ✅ READ ALL
    @GetMapping
    public List<FidelityResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public FidelityResponseDTO getById(@PathVariable String id) {

        Fidelity fidelity = service.getById(id);

        return mapper.toResponseDTO(fidelity);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public FidelityResponseDTO update(
            @PathVariable String id,
            @Valid @RequestBody FidelityRequestDTO dto) {

        Fidelity entity = mapper.toEntity(dto);
        entity.setId(id);

        Fidelity updated = service.update(id, entity);

        return mapper.toResponseDTO(updated);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {

        service.delete(id);

        return "Fidelity supprimée avec succès";
    }
}