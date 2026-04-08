package tn.esprit.spring.projet_pi_v2.DTORanim;

import java.util.Date;

public record FeedbackResponseDTO(
        String id,
        String commentaire,
        int note,
        String clientId,
        String watchPartyId,
        Date dateFeedback,
        int likes,
        int dislikes
) {}
