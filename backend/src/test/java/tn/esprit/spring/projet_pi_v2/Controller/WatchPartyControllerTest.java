package tn.esprit.spring.projet_pi_v2.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyResponseDTO;
import tn.esprit.spring.projet_pi_v2.Security.JwtAuthFilter;
import tn.esprit.spring.projet_pi_v2.Security.JwtService;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IWatchPartyService;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WatchPartyController.class)
@AutoConfigureMockMvc(addFilters = false)
class WatchPartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWatchPartyService watchPartyService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldCreateWatchParty() throws Exception {
        WatchPartyResponseDTO response = new WatchPartyResponseDTO(
                "wp1",
                "Soiree Film",
                "content1",
                "client1",
                "admin1",
                List.of("p1", "p2"),
                List.of("r1"),
                new Date(),
                "ACTIVE"
        );

        when(watchPartyService.createWatchParty(any())).thenReturn(response);

        String requestBody = """
                {
                  "titre": "Soiree Film",
                  "contenuId": "content1"
                }
                """;

        mockMvc.perform(post("/watchparty/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wp1"))
                .andExpect(jsonPath("$.titre").value("Soiree Film"))
                .andExpect(jsonPath("$.contenuId").value("content1"))
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.adminId").value("admin1"))
                .andExpect(jsonPath("$.statut").value("ACTIVE"));

        verify(watchPartyService).createWatchParty(any());
    }

    @Test
    void shouldJoinWatchParty() throws Exception {
        WatchPartyResponseDTO response = new WatchPartyResponseDTO(
                "wp1",
                "Soiree Film",
                "content1",
                "client1",
                "admin1",
                List.of("p1", "p2", "p3"),
                List.of("r1"),
                new Date(),
                "ACTIVE"
        );

        when(watchPartyService.joinWatchParty("wp1")).thenReturn(response);

        mockMvc.perform(post("/watchparty/wp1/join"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wp1"))
                .andExpect(jsonPath("$.titre").value("Soiree Film"))
                .andExpect(jsonPath("$.statut").value("ACTIVE"));

        verify(watchPartyService).joinWatchParty("wp1");
    }

    @Test
    void shouldLeaveWatchParty() throws Exception {
        WatchPartyResponseDTO response = new WatchPartyResponseDTO(
                "wp1",
                "Soiree Film",
                "content1",
                "client1",
                "admin1",
                List.of("p1"),
                List.of("r1"),
                new Date(),
                "ACTIVE"
        );

        when(watchPartyService.leaveWatchParty("wp1")).thenReturn(response);

        mockMvc.perform(post("/watchparty/wp1/leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wp1"))
                .andExpect(jsonPath("$.titre").value("Soiree Film"));

        verify(watchPartyService).leaveWatchParty("wp1");
    }

    @Test
    void shouldDeleteWatchParty() throws Exception {
        doNothing().when(watchPartyService).deleteWatchParty("wp1");

        mockMvc.perform(delete("/watchparty/wp1"))
                .andExpect(status().isOk());

        verify(watchPartyService).deleteWatchParty("wp1");
    }

    @Test
    void shouldGetWatchPartyById() throws Exception {
        WatchPartyResponseDTO response = new WatchPartyResponseDTO(
                "wp1",
                "Soiree Film",
                "content1",
                "client1",
                "admin1",
                List.of("p1", "p2"),
                List.of("r1"),
                new Date(),
                "ACTIVE"
        );

        when(watchPartyService.getWatchPartyById("wp1")).thenReturn(response);

        mockMvc.perform(get("/watchparty/wp1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wp1"))
                .andExpect(jsonPath("$.titre").value("Soiree Film"))
                .andExpect(jsonPath("$.statut").value("ACTIVE"));

        verify(watchPartyService).getWatchPartyById("wp1");
    }

    @Test
    void shouldGetAllWatchParties() throws Exception {
        WatchPartyResponseDTO w1 = new WatchPartyResponseDTO(
                "wp1",
                "WatchParty 1",
                "content1",
                "client1",
                "admin1",
                List.of("p1"),
                List.of("r1"),
                new Date(),
                "ACTIVE"
        );

        WatchPartyResponseDTO w2 = new WatchPartyResponseDTO(
                "wp2",
                "WatchParty 2",
                "content2",
                "client2",
                "admin2",
                List.of("p2"),
                List.of("r2"),
                new Date(),
                "PENDING"
        );

        when(watchPartyService.getAllWatchParties()).thenReturn(List.of(w1, w2));

        mockMvc.perform(get("/watchparty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("wp1"))
                .andExpect(jsonPath("$[0].titre").value("WatchParty 1"))
                .andExpect(jsonPath("$[1].id").value("wp2"))
                .andExpect(jsonPath("$[1].titre").value("WatchParty 2"));

        verify(watchPartyService).getAllWatchParties();
    }
}