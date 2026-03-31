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

import tn.esprit.spring.projet_pi_v2.DTO.PostRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PostResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Post;
import tn.esprit.spring.projet_pi_v2.Entity.User;
import tn.esprit.spring.projet_pi_v2.Repository.PostRepository;
import tn.esprit.spring.projet_pi_v2.Repository.UserRepository;
import tn.esprit.spring.projet_pi_v2.ServiceImpl.PostService;
import tn.esprit.spring.projet_pi_v2.mapper.PostMapper;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private PostMapper postMapper;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks private PostService postService;

    private User fakeUser;
    private Post fakePost;
    private PostRequestDTO fakeRequest;
    private PostResponseDTO fakeResponse;

    @BeforeEach
    void setUp() {
        fakeUser = new User();
        fakeUser.setId("user-1");
        fakeUser.setEmail("test@test.com");
        fakeUser.setUsername("testuser");

        fakePost = new Post();
        fakePost.setId("post-1");
        fakePost.setTitre("Test titre");
        fakePost.setContenu("Test contenu valide");
        fakePost.setAuthorUsername("testuser");

        fakeRequest = new PostRequestDTO();
        fakeRequest.setTitre("Test titre");
        fakeRequest.setContenu("Test contenu valide");

        fakeResponse = new PostResponseDTO();
        fakeResponse.setId("post-1");
        fakeResponse.setTitre("Test titre");

        // Mock SecurityContext
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
    }

    // ✅ Test 1 — createPost
    @Test
    void createPost_ShouldReturnPostResponse() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(fakeUser));
        when(postMapper.toEntity(fakeRequest)).thenReturn(fakePost);
        when(postRepository.save(any(Post.class))).thenReturn(fakePost);
        when(postMapper.toResponseDTO(fakePost)).thenReturn(fakeResponse);

        PostResponseDTO result = postService.createPost(fakeRequest);

        assertNotNull(result);
        assertEquals("post-1", result.getId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    // ✅ Test 2 — getAllPosts
    @Test
    void getAllPosts_ShouldReturnList() {
        when(postRepository.findAll()).thenReturn(List.of(fakePost));
        when(postMapper.toResponseDTO(fakePost)).thenReturn(fakeResponse);

        List<PostResponseDTO> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository, times(1)).findAll();
    }

    // ✅ Test 3 — getPostById trouvé
    @Test
    void getPostById_ShouldReturnPost_WhenExists() {
        when(postRepository.findById("post-1")).thenReturn(Optional.of(fakePost));
        when(postMapper.toResponseDTO(fakePost)).thenReturn(fakeResponse);

        PostResponseDTO result = postService.getPostById("post-1");

        assertNotNull(result);
        assertEquals("post-1", result.getId());
    }

    // ✅ Test 4 — getPostById non trouvé
    @Test
    void getPostById_ShouldThrow_WhenNotFound() {
        when(postRepository.findById("fake-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> postService.getPostById("fake-id"));
    }

    // ✅ Test 5 — deletePost
    @Test
    void deletePost_ShouldCallRepository() {
        doNothing().when(postRepository).deleteById("post-1");

        postService.deletePost("post-1");

        verify(postRepository, times(1)).deleteById("post-1");
    }

    // ✅ Test 6 — updatePost mauvais auteur
    @Test
    void updatePost_ShouldThrow_WhenNotAuthor() {
        fakePost.setAuthorUsername("autreuser"); // ← pas le bon auteur
        when(postRepository.findById("post-1")).thenReturn(Optional.of(fakePost));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(fakeUser));

        assertThrows(RuntimeException.class,
                () -> postService.updatePost("post-1", fakeRequest));
    }
}
