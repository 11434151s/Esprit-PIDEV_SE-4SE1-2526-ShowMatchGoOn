package tn.esprit.spring.projet_pi_v2.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Promotion;
import tn.esprit.spring.projet_pi_v2.Repository.PromotionRepository;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.PromotionService;
import tn.esprit.spring.projet_pi_v2.mapper.PromotionMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequestDTO dto) {
        Promotion promotion = promotionMapper.toEntity(dto);
        promotion.setActive(true); // ✅ Toujours active à la création
        return promotionMapper.toResponseDTO(promotionRepository.save(promotion));
    }

    @Override
    public List<PromotionResponseDTO> getAllPromotions() {
        return promotionRepository.findAll()
                .stream()
                .map(promotionMapper::toResponseDTO)
                .toList();
    }

    // ✅ Nouveau — récupère seulement les promos actives (pour les users)
    @Override
    public List<PromotionResponseDTO> getActivePromotions() {
        return promotionRepository.findByActiveTrue()
                .stream()
                .map(promotionMapper::toResponseDTO)
                .toList();
    }

    // ✅ Nouveau
    @Override
    public PromotionResponseDTO getPromotionById(String id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        return promotionMapper.toResponseDTO(promotion);
    }

    @Override
    public PromotionResponseDTO updatePromotion(String id, PromotionRequestDTO dto) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        promotionMapper.updatePromotionFromDto(dto, existing);
        return promotionMapper.toResponseDTO(promotionRepository.save(existing));
    }

    @Override
    public void deletePromotion(String id) {
        promotionRepository.deleteById(id);
    }

    // ✅ Nouveau — désactive sans supprimer (US-P06)
    @Override
    public void deactivatePromotion(String id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        promotion.setActive(false);
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionResponseDTO> getPromotionsByClient(String clientId) {
        return promotionRepository.findByClientId(clientId)
                .stream()
                .map(promotionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PromotionResponseDTO getPromotionByCode(String code) {
        // ✅ Optional au lieu de null check
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code promo introuvable"));
        return promotionMapper.toResponseDTO(promotion);
    }
}