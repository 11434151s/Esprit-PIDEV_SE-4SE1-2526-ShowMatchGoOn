package tn.esprit.spring.projet_pi_v2.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.FidelityRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.FidelityResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Fidelity;
import tn.esprit.spring.projet_pi_v2.Entity.FidelityLevel;
import tn.esprit.spring.projet_pi_v2.mapper.FidelityMapper;
import tn.esprit.spring.projet_pi_v2.service.FidelityService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FidelityControllerTest {

    @Mock
    private FidelityService service;

    @Mock
    private FidelityMapper mapper;

    @InjectMocks
    private FidelityController controller;

    private Fidelity fidelity;
    private FidelityRequestDTO requestDTO;
    private FidelityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Entité
        fidelity = new Fidelity();
        fidelity.setId("fid123");
        fidelity.setPoints(500);
        fidelity.setLevel(FidelityLevel.GOLD);
        fidelity.setDescription("Niveau GOLD avec avantages exclusifs");

        // Request DTO
        requestDTO = new FidelityRequestDTO();
        requestDTO.setPoints(500);
        requestDTO.setLevel("GOLD");
        requestDTO.setDescription("Niveau GOLD avec avantages exclusifs");

        // Response DTO
        responseDTO = new FidelityResponseDTO();
        responseDTO.setId("fid123");
        responseDTO.setPoints(500);
        responseDTO.setLevel("GOLD");
        responseDTO.setDescription("Niveau GOLD avec avantages exclusifs");
    }

    // =============================================
    // ✅ TEST CREATE
    // =============================================
    @Test
    void testCreate_ShouldReturnCreatedFidelity() {
        when(mapper.toEntity(requestDTO)).thenReturn(fidelity);
        when(service.addFidelity(fidelity)).thenReturn(fidelity);
        when(mapper.toResponseDTO(fidelity)).thenReturn(responseDTO);

        FidelityResponseDTO result = controller.create(requestDTO);

        assertNotNull(result);
        assertEquals("fid123", result.getId());
        assertEquals("GOLD", result.getLevel());
        assertEquals(500, result.getPoints());
        assertEquals("Niveau GOLD avec avantages exclusifs", result.getDescription());

        verify(service, times(1)).addFidelity(fidelity);
        verify(mapper, times(1)).toEntity(requestDTO);
        verify(mapper, times(1)).toResponseDTO(fidelity);
    }

    // =============================================
    // ✅ TEST GET ALL
    // =============================================
    @Test
    void testGetAll_ShouldReturnListOfFidelities() {
        Fidelity fidelity2 = new Fidelity();
        fidelity2.setId("fid456");
        fidelity2.setPoints(100);
        fidelity2.setLevel(FidelityLevel.BRONZE);
        fidelity2.setDescription("Niveau BRONZE pour débutants");

        FidelityResponseDTO responseDTO2 = new FidelityResponseDTO();
        responseDTO2.setId("fid456");
        responseDTO2.setPoints(100);
        responseDTO2.setLevel("BRONZE");
        responseDTO2.setDescription("Niveau BRONZE pour débutants");

        when(service.getAll()).thenReturn(Arrays.asList(fidelity, fidelity2));
        when(mapper.toResponseDTO(fidelity)).thenReturn(responseDTO);
        when(mapper.toResponseDTO(fidelity2)).thenReturn(responseDTO2);

        List<FidelityResponseDTO> result = controller.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("fid123", result.get(0).getId());
        assertEquals("GOLD", result.get(0).getLevel());
        assertEquals("fid456", result.get(1).getId());
        assertEquals("BRONZE", result.get(1).getLevel());

        verify(service, times(1)).getAll();
    }

    // =============================================
    // ✅ TEST GET BY ID
    // =============================================
    @Test
    void testGetById_ShouldReturnFidelity() {
        when(service.getById("fid123")).thenReturn(fidelity);
        when(mapper.toResponseDTO(fidelity)).thenReturn(responseDTO);

        FidelityResponseDTO result = controller.getById("fid123");

        assertNotNull(result);
        assertEquals("fid123", result.getId());
        assertEquals("GOLD", result.getLevel());
        assertEquals(500, result.getPoints());

        verify(service, times(1)).getById("fid123");
    }

    @Test
    void testGetById_WhenNotFound_ShouldReturnNull() {
        when(service.getById("notfound")).thenReturn(null);
        when(mapper.toResponseDTO(null)).thenReturn(null);

        FidelityResponseDTO result = controller.getById("notfound");

        assertNull(result);
        verify(service, times(1)).getById("notfound");
    }

    // =============================================
    // ✅ TEST UPDATE
    // =============================================
    @Test
    void testUpdate_ShouldReturnUpdatedFidelity() {
        Fidelity updated = new Fidelity();
        updated.setId("fid123");
        updated.setPoints(1000);
        updated.setLevel(FidelityLevel.PLATINUM);
        updated.setDescription("Niveau PLATINUM mis à jour");

        FidelityResponseDTO updatedResponse = new FidelityResponseDTO();
        updatedResponse.setId("fid123");
        updatedResponse.setPoints(1000);
        updatedResponse.setLevel("PLATINUM");
        updatedResponse.setDescription("Niveau PLATINUM mis à jour");

        FidelityRequestDTO updateRequest = new FidelityRequestDTO();
        updateRequest.setPoints(1000);
        updateRequest.setLevel("PLATINUM");
        updateRequest.setDescription("Niveau PLATINUM mis à jour");

        when(mapper.toEntity(updateRequest)).thenReturn(updated);
        when(service.update(eq("fid123"), any(Fidelity.class))).thenReturn(updated);
        when(mapper.toResponseDTO(updated)).thenReturn(updatedResponse);

        FidelityResponseDTO result = controller.update("fid123", updateRequest);

        assertNotNull(result);
        assertEquals("fid123", result.getId());
        assertEquals("PLATINUM", result.getLevel());
        assertEquals(1000, result.getPoints());

        verify(service, times(1)).update(eq("fid123"), any(Fidelity.class));
    }

    // =============================================
    // ✅ TEST DELETE
    // =============================================
    @Test
    void testDelete_ShouldReturnSuccessMessage() {
        doNothing().when(service).delete("fid123");

        String result = controller.delete("fid123");

        assertEquals("Fidelity supprimée avec succès", result);
        verify(service, times(1)).delete("fid123");
    }

    @Test
    void testDelete_ShouldCallServiceOnce() {
        doNothing().when(service).delete("fid123");

        controller.delete("fid123");

        verify(service, times(1)).delete("fid123");
        verifyNoMoreInteractions(service);
    }

    // =============================================
    // ✅ TEST GET ALL - Liste vide
    // =============================================
    @Test
    void testGetAll_WhenEmpty_ShouldReturnEmptyList() {
        when(service.getAll()).thenReturn(List.of());

        List<FidelityResponseDTO> result = controller.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, times(1)).getAll();
    }

    // =============================================
    // ✅ TEST Points positifs
    // =============================================
    @Test
    void testCreate_WithZeroPoints_ShouldSucceed() {
        Fidelity bronzeFidelity = new Fidelity();
        bronzeFidelity.setId("fid789");
        bronzeFidelity.setPoints(0);
        bronzeFidelity.setLevel(FidelityLevel.BRONZE);
        bronzeFidelity.setDescription("Niveau débutant sans points");

        FidelityRequestDTO bronzeRequest = new FidelityRequestDTO();
        bronzeRequest.setPoints(0);
        bronzeRequest.setLevel("BRONZE");
        bronzeRequest.setDescription("Niveau débutant sans points");

        FidelityResponseDTO bronzeResponse = new FidelityResponseDTO();
        bronzeResponse.setId("fid789");
        bronzeResponse.setPoints(0);
        bronzeResponse.setLevel("BRONZE");
        bronzeResponse.setDescription("Niveau débutant sans points");

        when(mapper.toEntity(bronzeRequest)).thenReturn(bronzeFidelity);
        when(service.addFidelity(bronzeFidelity)).thenReturn(bronzeFidelity);
        when(mapper.toResponseDTO(bronzeFidelity)).thenReturn(bronzeResponse);

        FidelityResponseDTO result = controller.create(bronzeRequest);

        assertNotNull(result);
        assertEquals(0, result.getPoints());
        assertEquals("BRONZE", result.getLevel());
    }
}