package tn.esprit.spring.showmatchgoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.showmatchgoon.Controller.SalleController;
import tn.esprit.spring.showmatchgoon.DTOSarah.request.SalleRequestDTO;
import tn.esprit.spring.showmatchgoon.DTOSarah.response.SalleResponseDTO;
import tn.esprit.spring.showmatchgoon.Security.JwtAuthFilter;
import tn.esprit.spring.showmatchgoon.Service.SalleService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalleService salleService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateSalle() throws Exception {
        SalleRequestDTO request = new SalleRequestDTO();
        request.setName("Salle Test");
        request.setCapacity(100);

        SalleResponseDTO response = new SalleResponseDTO();
        response.setId("1");
        response.setName("Salle Test");
        response.setCapacity(100);

        when(salleService.create(any(SalleRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/salles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(salleService, times(1)).create(any(SalleRequestDTO.class));
    }

    @Test
    public void testFindById() throws Exception {
        String id = "1";
        SalleResponseDTO response = new SalleResponseDTO();
        // Set response fields

        when(salleService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/salles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(salleService, times(1)).findById(id);
    }

    @Test
    public void testFindAll() throws Exception {
        List<SalleResponseDTO> responses = Arrays.asList(new SalleResponseDTO(), new SalleResponseDTO());

        when(salleService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/salles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(salleService, times(1)).findAll();
    }

    @Test
    public void testUpdateSalle() throws Exception {
        String id = "1";
        SalleRequestDTO request = new SalleRequestDTO();
        request.setName("Salle Modifiée");
        request.setCapacity(120);

        SalleResponseDTO response = new SalleResponseDTO();
        response.setId(id);
        response.setName("Salle Modifiée");
        response.setCapacity(120);

        when(salleService.update(eq(id), any(SalleRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/salles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(salleService, times(1)).update(eq(id), any(SalleRequestDTO.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        String id = "1";

        doNothing().when(salleService).deleteById(id);

        mockMvc.perform(delete("/api/salles/{id}", id))
                .andExpect(status().isNoContent());

        verify(salleService, times(1)).deleteById(id);
    }
}