package tn.esprit.spring.showmatchgoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.showmatchgoon.Controller.CinemaController;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.CinemaRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.CinemaResponseDTO;
import tn.esprit.spring.showmatchgoon.Security.JwtAuthFilter;
import tn.esprit.spring.showmatchgoon.Service.CinemaService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CinemaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService cinemaService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCinema() throws Exception {
        CinemaRequestDTO request = new CinemaRequestDTO();
        request.setNom("Cinéma Test");
        request.setAdresse("123 Rue Principale");
        request.setVille("Tunis");

        CinemaResponseDTO response = new CinemaResponseDTO();
        response.setId("1");
        response.setNom("Cinéma Test");
        response.setAdresse("123 Rue Principale");
        response.setVille("Tunis");

        when(cinemaService.create(any(CinemaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cinemaService, times(1)).create(any(CinemaRequestDTO.class));
    }

    @Test
    public void testFindById() throws Exception {
        String id = "1";
        CinemaResponseDTO response = new CinemaResponseDTO();
        // Set response fields

        when(cinemaService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/cinemas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cinemaService, times(1)).findById(id);
    }

    @Test
    public void testFindAll() throws Exception {
        List<CinemaResponseDTO> responses = Arrays.asList(new CinemaResponseDTO(), new CinemaResponseDTO());

        when(cinemaService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/cinemas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cinemaService, times(1)).findAll();
    }

    @Test
    public void testUpdateCinema() throws Exception {
        String id = "1";
        CinemaRequestDTO request = new CinemaRequestDTO();
        request.setNom("Cinéma Modifié");
        request.setAdresse("456 Avenue Modifiée");
        request.setVille("Sfax");

        CinemaResponseDTO response = new CinemaResponseDTO();
        response.setId(id);
        response.setNom("Cinéma Modifié");
        response.setAdresse("456 Avenue Modifiée");
        response.setVille("Sfax");

        when(cinemaService.update(eq(id), any(CinemaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/cinemas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cinemaService, times(1)).update(eq(id), any(CinemaRequestDTO.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        String id = "1";

        doNothing().when(cinemaService).deleteById(id);

        mockMvc.perform(delete("/api/cinemas/{id}", id))
                .andExpect(status().isNoContent());

        verify(cinemaService, times(1)).deleteById(id);
    }
}