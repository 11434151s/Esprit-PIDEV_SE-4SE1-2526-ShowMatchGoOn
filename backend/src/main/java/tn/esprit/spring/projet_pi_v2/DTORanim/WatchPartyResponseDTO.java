package tn.esprit.spring.projet_pi_v2.DTORanim;

import java.util.Date;
import java.util.List;

public record WatchPartyResponseDTO(
        String id,
        String titre,
        String contenuId,
        String clientId,
        String adminId,
        List<String> participantIds,
        List<String> reservationIds,
        Date dateCreation,
        String statut
) {
}
