package tn.esprit.spring.projet_pi_v2.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.AbonnementRequestDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Abonnement;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.AbonnementResponseDTO;
import tn.esprit.spring.projet_pi_v2.mapper.AbonnementMapper;
import tn.esprit.spring.projet_pi_v2.service.AbonnementService;


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/abonnements")
@CrossOrigin("*")
public class AbonnementController {

    @Autowired
    private AbonnementService service;

    @Autowired
    private AbonnementMapper mapper;

    // =============================
    // ✅ CREATE
    // =============================
    @PostMapping
    public AbonnementResponseDTO create(
            @Valid @RequestBody AbonnementRequestDTO dto) {

        Abonnement entity = mapper.toEntity(dto);
        Abonnement saved = service.addAbonnement(entity);

        return mapper.toResponseDTO(saved);
    }

    // =============================
    // ✅ READ ALL
    // =============================
    @GetMapping
    public List<AbonnementResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =============================
    // ✅ READ BY ID
    // =============================
    @GetMapping("/{id}")
    public AbonnementResponseDTO getById(@PathVariable String id) {

        Abonnement abonnement = service.getById(id);

        return mapper.toResponseDTO(abonnement);
    }

    // =============================
    // ✅ UPDATE
    // =============================
    @PutMapping("/{id}")
    public AbonnementResponseDTO update(
            @PathVariable String id,
            @Valid @RequestBody AbonnementRequestDTO dto) {

        Abonnement entity = mapper.toEntity(dto);
        entity.setId(id);

        Abonnement updated = service.update(id, entity);

        return mapper.toResponseDTO(updated);
    }

    // =============================
    // ✅ DELETE
    // =============================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {

        service.delete(id);

        return "Abonnement supprimé avec succès";
    }
}