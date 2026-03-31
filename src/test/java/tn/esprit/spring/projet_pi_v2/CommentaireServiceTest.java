package tn.esprit.spring.projet_pi_v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import tn.esprit.spring.projet_pi_v2.DTO.CommentaireRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Commentaire;
import tn.esprit.spring.projet_pi_v2.Entity.User;
import tn.esprit.spring.projet_pi_v2.Repository.CommentaireRepository;
import tn.esprit.spring.projet_pi_v2.Repository.UserRepository;
import tn.esprit.spring.projet_pi_v2.ServiceImpl.CommentaireServiceImpl;
import tn.esprit.spring.projet_pi_v2.mapper.CommentaireMapper;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentaireServiceTest {

    @Mock private CommentaireRepository commentaireRepository;
    @Mock private UserRepository userRepository;
    @Mock private CommentaireMapper commentaireMapper;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks private CommentaireServiceImpl commentaireService;

    private User fakeUser;
    private Commentaire fakeCommentaire;
    private CommentaireRequestDTO fakeRequest;
    private CommentaireResponseDTO fakeResponse;

    @BeforeEach
    void setUp() {
        fakeUser = new User();
        fakeUser.setId("user-1");
        fakeUser.setEmail("test@test.com");
        fakeUser.setUsername("testuser");

        fakeCommentaire = new Commentaire();
        fakeCommentaire.setId("com-1");
        fakeCommentaire.setContenu("Super commentaire !");
        fakeCommentaire.setPostId("post-1");
        fakeCommentaire.setAuthorUsername("testuser");

        fakeRequest = new CommentaireRequestDTO();
        fakeRequest.setContenu("Super commentaire !");
        fakeRequest.setPostId("post-1");

        fakeResponse = new CommentaireResponseDTO();
        fakeResponse.setId("com-1");
        fakeResponse.setContenu("Super commentaire !");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
    }

    // ✅ Test 1 — create
    @Test
    void create_ShouldReturnCommentaire() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(fakeUser));
        when(commentaireMapper.toEntity(fakeRequest)).thenReturn(fakeCommentaire);
        when(commentaireRepository.save(any(Commentaire.class))).thenReturn(fakeCommentaire);
        when(commentaireMapper.toResponseDTO(fakeCommentaire)).thenReturn(fakeResponse);

        CommentaireResponseDTO result = commentaireService.create(fakeRequest);

        assertNotNull(result);
        assertEquals("com-1", result.getId());
        verify(commentaireRepository, times(1)).save(any(Commentaire.class));
    }

    // ✅ Test 2 — getAll
    @Test
    void getAll_ShouldReturnList() {
        when(commentaireRepository.findAll()).thenReturn(List.of(fakeCommentaire));
        when(commentaireMapper.toResponseDTO(fakeCommentaire)).thenReturn(fakeResponse);

        List<CommentaireResponseDTO> result = commentaireService.getAll();

        assertEquals(1, result.size());
        verify(commentaireRepository, times(1)).findAll();
    }

    // ✅ Test 3 — getByPostId
    @Test
    void getByPostId_ShouldReturnCommentaires() {
        when(commentaireRepository.findByPostId("post-1"))
                .thenReturn(List.of(fakeCommentaire));
        when(commentaireMapper.toResponseDTO(fakeCommentaire)).thenReturn(fakeResponse);

        List<CommentaireResponseDTO> result = commentaireService.getByPostId("post-1");

        assertEquals(1, result.size());
    }

    // ✅ Test 4 — delete
    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(commentaireRepository).deleteById("com-1");

        commentaireService.delete("com-1");

        verify(commentaireRepository, times(1)).deleteById("com-1");
    }

    // ✅ Test 5 — getById non trouvé
    @Test
    void getById_ShouldThrow_WhenNotFound() {
        when(commentaireRepository.findById("fake")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> commentaireService.getById("fake"));
    }
}