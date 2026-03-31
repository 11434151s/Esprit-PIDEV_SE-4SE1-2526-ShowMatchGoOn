package tn.esprit.spring.projet_pi_v2.ServiceInterface;

import tn.esprit.spring.projet_pi_v2.DTO.PromotionRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionResponseDTO;
import java.util.List;

public interface PromotionService {

    PromotionResponseDTO createPromotion(PromotionRequestDTO dto);
    List<PromotionResponseDTO> getAllPromotions();
    List<PromotionResponseDTO> getActivePromotions();        // ✅ Ajout
    PromotionResponseDTO getPromotionById(String id);        // ✅ Ajout
    PromotionResponseDTO updatePromotion(String id, PromotionRequestDTO dto);
    void deletePromotion(String id);
    void deactivatePromotion(String id);                     // ✅ Ajout US-P06
    List<PromotionResponseDTO> getPromotionsByClient(String clientId);
    PromotionResponseDTO getPromotionByCode(String code);
}