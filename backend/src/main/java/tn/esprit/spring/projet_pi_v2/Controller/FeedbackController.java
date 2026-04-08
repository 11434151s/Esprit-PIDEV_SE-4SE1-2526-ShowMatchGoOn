package tn.esprit.spring.projet_pi_v2.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackResponseDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackUpdateDTO;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IFeedbackService;

import java.util.List;
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final IFeedbackService service;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody FeedbackCreateDTO dto) {
        return ResponseEntity.ok(service.createFeedback(dto));
    }

    @GetMapping
    public List<FeedbackResponseDTO> getAll() {
        return service.getAllFeedback();
    }

    @PutMapping("/{id}")
    public FeedbackResponseDTO update(@PathVariable String id,
                                      @Valid @RequestBody FeedbackUpdateDTO dto) {
        return service.updateFeedback(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteFeedback(id);
    }

    // Feedbacks d'une WatchParty spécifique
    @GetMapping("/watchparty/{watchPartyId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getByWatchParty(
            @PathVariable String watchPartyId) {
        return ResponseEntity.ok(service.getFeedbacksByWatchParty(watchPartyId));
    }

    // Like
    @PostMapping("/{id}/like")
    public ResponseEntity<FeedbackResponseDTO> like(@PathVariable String id) {
        return ResponseEntity.ok(service.likeFeedback(id));
    }

    // Dislike
    @PostMapping("/{id}/dislike")
    public ResponseEntity<FeedbackResponseDTO> dislike(@PathVariable String id) {
        return ResponseEntity.ok(service.dislikeFeedback(id));
    }
}