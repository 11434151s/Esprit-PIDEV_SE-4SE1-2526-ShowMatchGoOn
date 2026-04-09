package tn.esprit.spring.showmatchgoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.showmatchgoon.Controller.ReservationController;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.ReservationRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.ReservationResponseDTO;
import tn.esprit.spring.showmatchgoon.Security.JwtAuthFilter;
import tn.esprit.spring.showmatchgoon.Service.ReservationService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateReservation() throws Exception {
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setSeanceId("seance1");
        request.setUserId("user1");
        request.setNumeroPlace("A1");
        request.setPrix(10.0);

        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId("1");
        response.setUserId("user1");
        response.setNumeroPlace("A1");
        response.setPrix(10.0);

        when(reservationService.create(any(ReservationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservationService, times(1)).create(any(ReservationRequestDTO.class));
    }

    @Test
    public void testFindById() throws Exception {
        String id = "1";
        ReservationResponseDTO response = new ReservationResponseDTO();
        // Set response fields

        when(reservationService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservationService, times(1)).findById(id);
    }

    @Test
    public void testFindAll() throws Exception {
        List<ReservationResponseDTO> responses = Arrays.asList(new ReservationResponseDTO(), new ReservationResponseDTO());

        when(reservationService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservationService, times(1)).findAll();
    }

    @Test
    public void testFindByUserId() throws Exception {
        String userId = "user1";
        List<ReservationResponseDTO> responses = Arrays.asList(new ReservationResponseDTO());

        when(reservationService.findByUserId(userId)).thenReturn(responses);

        mockMvc.perform(get("/api/reservations/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservationService, times(1)).findByUserId(userId);
    }

    @Test
    public void testUpdateReservation() throws Exception {
        String id = "1";
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setSeanceId("seance1");
        request.setUserId("user1");
        request.setNumeroPlace("B2");
        request.setPrix(12.5);

        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(id);
        response.setUserId("user1");
        response.setNumeroPlace("B2");
        response.setPrix(12.5);

        when(reservationService.update(eq(id), any(ReservationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/reservations/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservationService, times(1)).update(eq(id), any(ReservationRequestDTO.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        String id = "1";

        doNothing().when(reservationService).deleteById(id);

        mockMvc.perform(delete("/api/reservations/{id}", id))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteById(id);
    }
}