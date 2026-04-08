package tn.esprit.spring.projet_pi_v2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyResponseDTO;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IWatchPartyService;

import java.util.List;

@RestController
@RequestMapping("/watchparty")
@RequiredArgsConstructor
public class WatchPartyController {

    private final IWatchPartyService service;

    @PostMapping("/create")
    public ResponseEntity<WatchPartyResponseDTO> create(@Valid @RequestBody WatchPartyCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createWatchParty(dto));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<WatchPartyResponseDTO> join(@PathVariable String id) {
        return ResponseEntity.ok(service.joinWatchParty(id));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<WatchPartyResponseDTO> leave(@PathVariable String id) {
        return ResponseEntity.ok(service.leaveWatchParty(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteWatchParty(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchPartyResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getWatchPartyById(id));
    }

    @GetMapping
    public ResponseEntity<List<WatchPartyResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllWatchParties());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<String>> getParticipants(@PathVariable String id) {
        return ResponseEntity.ok(service.getWatchPartyById(id).participantIds());
    }
}