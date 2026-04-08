package tn.esprit.spring.projet_pi_v2.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackResponseDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackUpdateDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Feedback;
import tn.esprit.spring.projet_pi_v2.Mapper.FeedbackMapper;
import tn.esprit.spring.projet_pi_v2.Repository.FeedbackRepository;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IFeedbackService;
import tn.esprit.spring.projet_pi_v2.Repository.WatchPartyRepository;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final WatchPartyRepository watchPartyRepository;
    private final FeedbackMapper feedbackMapper; // ✅ injecté

    @Override
    public FeedbackResponseDTO createFeedback(FeedbackCreateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        watchPartyRepository.findById(dto.watchPartyId())
                .orElseThrow(() -> new RuntimeException("WatchParty not found"));

        if (feedbackRepository.existsByWatchPartyIdAndClientId(dto.watchPartyId(), userId)) {
            throw new RuntimeException("Vous avez déjà ajouté un feedback pour cette watch party");
        }

        Feedback feedback = new Feedback();
        feedback.setNote(dto.note());
        feedback.setCommentaire(dto.commentaire());
        feedback.setClientId(userId);
        feedback.setWatchPartyId(dto.watchPartyId());
        feedback.setDateFeedback(new Date());

        return feedbackMapper.toResponseDTO(feedbackRepository.save(feedback)); // ✅
    }

    @Override
    public List<FeedbackResponseDTO> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(feedbackMapper::toResponseDTO) // ✅
                .toList();
    }

    @Override
    public FeedbackResponseDTO updateFeedback(String id, FeedbackUpdateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!feedback.getClientId().equals(userId)) {
            throw new RuntimeException("You can only update your own feedback");
        }

        feedback.setNote(dto.note());
        feedback.setCommentaire(dto.commentaire());

        return feedbackMapper.toResponseDTO(feedbackRepository.save(feedback)); // ✅
    }

    @Override
    public void deleteFeedback(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!feedback.getClientId().equals(userId)) {
            throw new RuntimeException("You can only delete your own feedback");
        }

        feedbackRepository.deleteById(id);
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbacksByWatchParty(String watchPartyId) {
        return feedbackRepository.findByWatchPartyId(watchPartyId)
                .stream()
                .map(feedbackMapper::toResponseDTO)
                .toList();
    }

    @Override
    public FeedbackResponseDTO likeFeedback(String feedbackId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Annuler dislike si existant
        if (feedback.getDislikedByUserIds().contains(userId)) {
            feedback.getDislikedByUserIds().remove(userId);
            feedback.setDislikes(feedback.getDislikes() - 1);
        }

        // Toggle like
        if (feedback.getLikedByUserIds().contains(userId)) {
            feedback.getLikedByUserIds().remove(userId);
            feedback.setLikes(feedback.getLikes() - 1);
        } else {
            feedback.getLikedByUserIds().add(userId);
            feedback.setLikes(feedback.getLikes() + 1);
        }

        return feedbackMapper.toResponseDTO(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackResponseDTO dislikeFeedback(String feedbackId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Annuler like si existant
        if (feedback.getLikedByUserIds().contains(userId)) {
            feedback.getLikedByUserIds().remove(userId);
            feedback.setLikes(feedback.getLikes() - 1);
        }

        // Toggle dislike
        if (feedback.getDislikedByUserIds().contains(userId)) {
            feedback.getDislikedByUserIds().remove(userId);
            feedback.setDislikes(feedback.getDislikes() - 1);
        } else {
            feedback.getDislikedByUserIds().add(userId);
            feedback.setDislikes(feedback.getDislikes() + 1);
        }

        return feedbackMapper.toResponseDTO(feedbackRepository.save(feedback));
    }
}