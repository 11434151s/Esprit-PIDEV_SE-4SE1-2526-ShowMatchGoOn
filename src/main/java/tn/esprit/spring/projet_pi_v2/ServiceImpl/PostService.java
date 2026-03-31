package tn.esprit.spring.projet_pi_v2.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tn.esprit.spring.projet_pi_v2.DTO.PostRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PostResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Post;
import tn.esprit.spring.projet_pi_v2.Entity.User;
import tn.esprit.spring.projet_pi_v2.Repository.PostRepository;
import tn.esprit.spring.projet_pi_v2.Repository.UserRepository;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IPostService;
import tn.esprit.spring.projet_pi_v2.mapper.PostMapper;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User author = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Post post = postMapper.toEntity(dto);
        post.setDatePublication(new Date());
        post.setAuthorId(author.getId());
        post.setAuthorUsername(author.getEmail());

        return postMapper.toResponseDTO(postRepository.save(post));
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PostResponseDTO getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post introuvable"));
        return postMapper.toResponseDTO(post);
    }

    @Override
    public PostResponseDTO updatePost(String id, PostRequestDTO dto) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post introuvable"));

        if (!post.getAuthorUsername().equals(username)) {
            throw new RuntimeException("Vous ne pouvez modifier que vos propres posts");
        }

        postMapper.updatePostFromDto(dto, post);
        return postMapper.toResponseDTO(postRepository.save(post));
    }

    @Override
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}