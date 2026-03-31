package tn.esprit.spring.projet_pi_v2.ServiceInterface;

import tn.esprit.spring.projet_pi_v2.DTO.PostRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PostResponseDTO;
import java.util.List;

public interface IPostService {
    PostResponseDTO createPost(PostRequestDTO dto);
    List<PostResponseDTO> getAllPosts();
    PostResponseDTO getPostById(String id);
    PostResponseDTO updatePost(String id, PostRequestDTO dto);
    void deletePost(String id);
}