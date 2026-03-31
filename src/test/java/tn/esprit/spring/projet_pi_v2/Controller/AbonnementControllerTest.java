package tn.esprit.spring.projet_pi_v2.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.request.AbonnementRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.dtowafa.response.AbonnementResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Abonnement;
import tn.esprit.spring.projet_pi_v2.Entity.AbonnementType;
import tn.esprit.spring.projet_pi_v2.mapper.AbonnementMapper;
import tn.esprit.spring.projet_pi_v2.service.AbonnementService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbonnementControllerTest {

    @Mock
    private AbonnementService service;

    @Mock
    private AbonnementMapper mapper;

    @InjectMocks
    private AbonnementController controller;

    private Abonnement abonnement;
    private AbonnementRequestDTO requestDTO;
    private AbonnementResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Entité
        abonnement = new Abonnement();
        abonnement.setId("abc123");
        abonnement.setType(AbonnementType.PREMIUM);
        abonnement.setPrix(14.99);
        abonnement.setDescription("Accès illimité à tous les contenus premium");

        // Request DTO
        requestDTO = new AbonnementRequestDTO();
        requestDTO.setType(AbonnementType.PREMIUM);
        requestDTO.setPrix(14.99);
        requestDTO.setDescription("Accès illimité à tous les contenus premium");

        // Response DTO
        responseDTO = new AbonnementResponseDTO();
        responseDTO.setId("abc123");
        responseDTO.setType("PREMIUM");
        responseDTO.setPrix(14.99);
        responseDTO.setDescription("Accès illimité à tous les contenus premium");
    }

    // =============================================
    // ✅ TEST CREATE
    // =============================================
    @Test
    void testCreate_ShouldReturnCreatedAbonnement() {
        when(mapper.toEntity(requestDTO)).thenReturn(abonnement);
        when(service.addAbonnement(abonnement)).thenReturn(abonnement);
        when(mapper.toResponseDTO(abonnement)).thenReturn(responseDTO);

        AbonnementResponseDTO result = controller.create(requestDTO);

        assertNotNull(result);
        assertEquals("abc123", result.getId());
        assertEquals("PREMIUM", result.getType());
        assertEquals(14.99, result.getPrix());
        assertEquals("Accès illimité à tous les contenus premium", result.getDescription());

        verify(service, times(1)).addAbonnement(abonnement);
        verify(mapper, times(1)).toEntity(requestDTO);
        verify(mapper, times(1)).toResponseDTO(abonnement);
    }

    // =============================================
    // ✅ TEST GET ALL
    // =============================================
    @Test
    void testGetAll_ShouldReturnListOfAbonnements() {
        Abonnement abonnement2 = new Abonnement();
        abonnement2.setId("def456");
        abonnement2.setType(AbonnementType.BASIC);
        abonnement2.setPrix(4.99);
        abonnement2.setDescription("Accès basique aux contenus");

        AbonnementResponseDTO responseDTO2 = new AbonnementResponseDTO();
        responseDTO2.setId("def456");
        responseDTO2.setType("BASIC");
        responseDTO2.setPrix(4.99);
        responseDTO2.setDescription("Accès basique aux contenus");

        when(service.getAll()).thenReturn(Arrays.asList(abonnement, abonnement2));
        when(mapper.toResponseDTO(abonnement)).thenReturn(responseDTO);
        when(mapper.toResponseDTO(abonnement2)).thenReturn(responseDTO2);

        List<AbonnementResponseDTO> result = controller.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("abc123", result.get(0).getId());
        assertEquals("def456", result.get(1).getId());
        assertEquals("PREMIUM", result.get(0).getType());
        assertEquals("BASIC", result.get(1).getType());

        verify(service, times(1)).getAll();
    }

    // =============================================
    // ✅ TEST GET BY ID
    // =============================================
    @Test
    void testGetById_ShouldReturnAbonnement() {
        when(service.getById("abc123")).thenReturn(abonnement);
        when(mapper.toResponseDTO(abonnement)).thenReturn(responseDTO);

        AbonnementResponseDTO result = controller.getById("abc123");

        assertNotNull(result);
        assertEquals("abc123", result.getId());
        assertEquals("PREMIUM", result.getType());

        verify(service, times(1)).getById("abc123");
    }

    @Test
    void testGetById_WhenNotFound_ShouldReturnNull() {
        when(service.getById("notfound")).thenReturn(null);
        when(mapper.toResponseDTO(null)).thenReturn(null);

        AbonnementResponseDTO result = controller.getById("notfound");

        assertNull(result);
        verify(service, times(1)).getById("notfound");
    }

    // =============================================
    // ✅ TEST UPDATE
    // =============================================
    @Test
    void testUpdate_ShouldReturnUpdatedAbonnement() {
        Abonnement updated = new Abonnement();
        updated.setId("abc123");
        updated.setType(AbonnementType.BASIC);
        updated.setPrix(9.99);
        updated.setDescription("Description mise à jour");

        AbonnementResponseDTO updatedResponse = new AbonnementResponseDTO();
        updatedResponse.setId("abc123");
        updatedResponse.setType("BASIC");
        updatedResponse.setPrix(9.99);
        updatedResponse.setDescription("Description mise à jour");

        AbonnementRequestDTO updateRequest = new AbonnementRequestDTO();
        updateRequest.setType(AbonnementType.BASIC);
        updateRequest.setPrix(9.99);
        updateRequest.setDescription("Description mise à jour");

        when(mapper.toEntity(updateRequest)).thenReturn(updated);
        when(service.update(eq("abc123"), any(Abonnement.class))).thenReturn(updated);
        when(mapper.toResponseDTO(updated)).thenReturn(updatedResponse);

        AbonnementResponseDTO result = controller.update("abc123", updateRequest);

        assertNotNull(result);
        assertEquals("abc123", result.getId());
        assertEquals("BASIC", result.getType());
        assertEquals(9.99, result.getPrix());

        verify(service, times(1)).update(eq("abc123"), any(Abonnement.class));
    }

    // =============================================
    // ✅ TEST DELETE
    // =============================================
    @Test
    void testDelete_ShouldReturnSuccessMessage() {
        doNothing().when(service).delete("abc123");

        String result = controller.delete("abc123");

        assertEquals("Abonnement supprimé avec succès", result);
        verify(service, times(1)).delete("abc123");
    }

    @Test
    void testDelete_ShouldCallServiceOnce() {
        doNothing().when(service).delete("abc123");

        controller.delete("abc123");

        verify(service, times(1)).delete("abc123");
        verifyNoMoreInteractions(service);
    }

    // =============================================
    // ✅ TEST GET ALL - Liste vide
    // =============================================
    @Test
    void testGetAll_WhenEmpty_ShouldReturnEmptyList() {
        when(service.getAll()).thenReturn(List.of());

        List<AbonnementResponseDTO> result = controller.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, times(1)).getAll();
    }
}