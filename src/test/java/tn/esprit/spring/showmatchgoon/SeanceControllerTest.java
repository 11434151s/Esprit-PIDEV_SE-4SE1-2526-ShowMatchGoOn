package tn.esprit.spring.showmatchgoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.showmatchgoon.Controller.SeanceController;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SeanceRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SeanceResponseDTO;
import tn.esprit.spring.showmatchgoon.Security.JwtAuthFilter;
import tn.esprit.spring.showmatchgoon.Service.SeanceService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeanceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SeanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeanceService seanceService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateSeance() throws Exception {
        SeanceRequestDTO request = new SeanceRequestDTO();
        request.setDateSeance(new Date(System.currentTimeMillis() + 86400000));
        request.setHeureSeance("20:00");
        request.setSalleId("salle1");
        request.setCinemaId("cinema1");
        request.setContenuId("contenu1");

        SeanceResponseDTO response = new SeanceResponseDTO();
        response.setId("1");
        response.setDateSeance(request.getDateSeance());
        response.setHeureSeance("20:00");
        response.setNumeroSalle("salle1");
        response.setNomCinema("cinema1");
        response.setContenuId("contenu1");

        when(seanceService.create(any(SeanceRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/seances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(seanceService, times(1)).create(any(SeanceRequestDTO.class));
    }

    @Test
    public void testFindById() throws Exception {
        String id = "1";
        SeanceResponseDTO response = new SeanceResponseDTO();
        // Set response fields

        when(seanceService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/seances/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(seanceService, times(1)).findById(id);
    }

    @Test
    public void testFindAll() throws Exception {
        List<SeanceResponseDTO> responses = Arrays.asList(new SeanceResponseDTO(), new SeanceResponseDTO());

        when(seanceService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/seances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(seanceService, times(1)).findAll();
    }

    @Test
    public void testFindByCinemaId() throws Exception {
        String cinemaId = "cinema1";
        List<SeanceResponseDTO> responses = Arrays.asList(new SeanceResponseDTO());

        when(seanceService.findByCinemaId(cinemaId)).thenReturn(responses);

        mockMvc.perform(get("/api/seances/cinema/{cinemaId}", cinemaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(seanceService, times(1)).findByCinemaId(cinemaId);
    }

    @Test
    public void testUpdateSeance() throws Exception {
        String id = "1";
        SeanceRequestDTO request = new SeanceRequestDTO();
        request.setDateSeance(new Date(System.currentTimeMillis() + 86400000));
        request.setHeureSeance("21:00");
        request.setSalleId("salle2");
        request.setCinemaId("cinema2");
        request.setContenuId("contenu2");

        SeanceResponseDTO response = new SeanceResponseDTO();
        response.setId(id);
        response.setDateSeance(request.getDateSeance());
        response.setHeureSeance("21:00");
        response.setNumeroSalle("salle2");
        response.setNomCinema("cinema2");
        response.setContenuId("contenu2");

        when(seanceService.update(eq(id), any(SeanceRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/seances/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(seanceService, times(1)).update(eq(id), any(SeanceRequestDTO.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        String id = "1";

        doNothing().when(seanceService).deleteById(id);

        mockMvc.perform(delete("/api/seances/{id}", id))
                .andExpect(status().isNoContent());

        verify(seanceService, times(1)).deleteById(id);
    }
}