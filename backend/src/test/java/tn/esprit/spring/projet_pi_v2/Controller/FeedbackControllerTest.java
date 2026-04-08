package tn.esprit.spring.projet_pi_v2.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackResponseDTO;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IFeedbackService;
import tn.esprit.spring.projet_pi_v2.Security.JwtAuthFilter;
import tn.esprit.spring.projet_pi_v2.Security.JwtService;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFeedbackService feedbackService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldAddFeedback() throws Exception {
        FeedbackResponseDTO response = new FeedbackResponseDTO(
                "fb1",
                "Très bien",
                5,
                "client1",
                "wp1",
                new Date()
        );

        when(feedbackService.createFeedback(any())).thenReturn(response);

        String requestBody = """
                {
                  "commentaire": "Très bien",
                  "note": 5,
                  "watchPartyId": "wp1"
                }
                """;

        mockMvc.perform(post("/feedback/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fb1"))
                .andExpect(jsonPath("$.commentaire").value("Très bien"))
                .andExpect(jsonPath("$.note").value(5))
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.watchPartyId").value("wp1"));

        verify(feedbackService).createFeedback(any());
    }

    @Test
    void shouldGetAllFeedbacks() throws Exception {
        FeedbackResponseDTO f1 = new FeedbackResponseDTO(
                "fb1",
                "Excellent",
                5,
                "client1",
                "wp1",
                new Date()
        );

        FeedbackResponseDTO f2 = new FeedbackResponseDTO(
                "fb2",
                "Moyen",
                3,
                "client2",
                "wp2",
                new Date()
        );

        when(feedbackService.getAllFeedback()).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("fb1"))
                .andExpect(jsonPath("$[0].commentaire").value("Excellent"))
                .andExpect(jsonPath("$[0].note").value(5))
                .andExpect(jsonPath("$[1].id").value("fb2"))
                .andExpect(jsonPath("$[1].commentaire").value("Moyen"))
                .andExpect(jsonPath("$[1].note").value(3));

        verify(feedbackService).getAllFeedback();
    }

    @Test
    void shouldUpdateFeedback() throws Exception {
        FeedbackResponseDTO response = new FeedbackResponseDTO(
                "fb1",
                "Mis à jour",
                4,
                "client1",
                "wp1",
                new Date()
        );

        when(feedbackService.updateFeedback(eq("fb1"), any())).thenReturn(response);

        String requestBody = """
                {
                  "commentaire": "Mis à jour",
                  "note": 4
                }
                """;

        mockMvc.perform(put("/feedback/fb1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fb1"))
                .andExpect(jsonPath("$.commentaire").value("Mis à jour"))
                .andExpect(jsonPath("$.note").value(4))
                .andExpect(jsonPath("$.watchPartyId").value("wp1"));

        verify(feedbackService).updateFeedback(eq("fb1"), any());
    }

    @Test
    void shouldDeleteFeedback() throws Exception {
        doNothing().when(feedbackService).deleteFeedback("fb1");

        mockMvc.perform(delete("/feedback/fb1"))
                .andExpect(status().isOk());

        verify(feedbackService).deleteFeedback("fb1");
    }
}