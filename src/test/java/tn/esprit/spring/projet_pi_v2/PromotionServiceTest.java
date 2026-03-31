package tn.esprit.spring.projet_pi_v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import tn.esprit.spring.projet_pi_v2.DTO.PromotionRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Promotion;
import tn.esprit.spring.projet_pi_v2.Repository.PromotionRepository;
import tn.esprit.spring.projet_pi_v2.ServiceImpl.PromotionServiceImpl;
import tn.esprit.spring.projet_pi_v2.mapper.PromotionMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionMapper promotionMapper;

    @InjectMocks private PromotionServiceImpl promotionService;

    private Promotion fakePromotion;
    private PromotionRequestDTO fakeRequest;
    private PromotionResponseDTO fakeResponse;

    @BeforeEach
    void setUp() {
        fakePromotion = new Promotion();
        fakePromotion.setId("promo-1");
        fakePromotion.setCode("SUMMER20");
        fakePromotion.setPourcentageReduction(20.0);
        fakePromotion.setDateExpiration(new Date());
        fakePromotion.setActive(true);

        fakeRequest = new PromotionRequestDTO();
        fakeRequest.setCode("SUMMER20");
        fakeRequest.setPourcentageReduction(20.0);
        fakeRequest.setDateExpiration(new Date());

        fakeResponse = new PromotionResponseDTO();
        fakeResponse.setId("promo-1");
        fakeResponse.setCode("SUMMER20");
    }

    // ✅ Test 1 — createPromotion
    @Test
    void createPromotion_ShouldReturnPromotion() {
        when(promotionMapper.toEntity(fakeRequest)).thenReturn(fakePromotion);
        when(promotionRepository.save(any(Promotion.class))).thenReturn(fakePromotion);
        when(promotionMapper.toResponseDTO(fakePromotion)).thenReturn(fakeResponse);

        PromotionResponseDTO result = promotionService.createPromotion(fakeRequest);

        assertNotNull(result);
        assertEquals("SUMMER20", result.getCode());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    // ✅ Test 2 — getAllPromotions
    @Test
    void getAllPromotions_ShouldReturnList() {
        when(promotionRepository.findAll()).thenReturn(List.of(fakePromotion));
        when(promotionMapper.toResponseDTO(fakePromotion)).thenReturn(fakeResponse);

        List<PromotionResponseDTO> result = promotionService.getAllPromotions();

        assertEquals(1, result.size());
    }

    // ✅ Test 3 — getPromotionById trouvé
    @Test
    void getPromotionById_ShouldReturn_WhenExists() {
        when(promotionRepository.findById("promo-1"))
                .thenReturn(Optional.of(fakePromotion));
        when(promotionMapper.toResponseDTO(fakePromotion)).thenReturn(fakeResponse);

        PromotionResponseDTO result = promotionService.getPromotionById("promo-1");

        assertNotNull(result);
        assertEquals("promo-1", result.getId());
    }

    // ✅ Test 4 — getPromotionById non trouvé
    @Test
    void getPromotionById_ShouldThrow_WhenNotFound() {
        when(promotionRepository.findById("fake")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> promotionService.getPromotionById("fake"));
    }

    // ✅ Test 5 — deactivatePromotion
    @Test
    void deactivatePromotion_ShouldSetActiveFalse() {
        when(promotionRepository.findById("promo-1"))
                .thenReturn(Optional.of(fakePromotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(fakePromotion);

        promotionService.deactivatePromotion("promo-1");

        assertFalse(fakePromotion.isActive());
        verify(promotionRepository, times(1)).save(fakePromotion);
    }

    // ✅ Test 6 — deletePromotion
    @Test
    void deletePromotion_ShouldCallRepository() {
        doNothing().when(promotionRepository).deleteById("promo-1");

        promotionService.deletePromotion("promo-1");

        verify(promotionRepository, times(1)).deleteById("promo-1");
    }

    // ✅ Test 7 — getByCode trouvé
    @Test
    void getPromotionByCode_ShouldReturn_WhenExists() {
        when(promotionRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.of(fakePromotion));
        when(promotionMapper.toResponseDTO(fakePromotion)).thenReturn(fakeResponse);

        PromotionResponseDTO result = promotionService.getPromotionByCode("SUMMER20");

        assertEquals("SUMMER20", result.getCode());
    }
}