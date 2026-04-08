package tn.esprit.spring.projet_pi_v2.ServiceInterface;

import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackResponseDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackUpdateDTO;

import java.util.List;

public interface IFeedbackService {

    FeedbackResponseDTO createFeedback(FeedbackCreateDTO dto);

    List<FeedbackResponseDTO> getAllFeedback();

    FeedbackResponseDTO updateFeedback(String id, FeedbackUpdateDTO dto);
    List<FeedbackResponseDTO> getFeedbacksByWatchParty(String watchPartyId);
    FeedbackResponseDTO likeFeedback(String feedbackId);
    FeedbackResponseDTO dislikeFeedback(String feedbackId);

    void deleteFeedback(String id);
}