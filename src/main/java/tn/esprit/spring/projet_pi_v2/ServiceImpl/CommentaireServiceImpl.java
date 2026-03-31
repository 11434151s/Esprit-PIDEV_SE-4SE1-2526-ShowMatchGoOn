package tn.esprit.spring.projet_pi_v2.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Commentaire;
import tn.esprit.spring.projet_pi_v2.Entity.User;
import tn.esprit.spring.projet_pi_v2.Repository.CommentaireRepository;
import tn.esprit.spring.projet_pi_v2.Repository.UserRepository;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.CommentaireService;
import tn.esprit.spring.projet_pi_v2.mapper.CommentaireMapper;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentaireServiceImpl implements CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final CommentaireMapper commentaireMapper;
    private final UserRepository userRepository; // ✅ Ajout

    @Override
    public CommentaireResponseDTO create(CommentaireRequestDTO dto) {
        // ✅ Récupérer l'auteur depuis le token JWT
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Commentaire commentaire = commentaireMapper.toEntity(dto);
        commentaire.setDateCommentaire(new Date());
        commentaire.setAuthorId(author.getId());           // ✅
        commentaire.setAuthorUsername(author.getUsername()); // ✅

        return commentaireMapper.toResponseDTO(commentaireRepository.save(commentaire));
    }

    @Override
    public List<CommentaireResponseDTO> getAll() {
        return commentaireRepository.findAll()
                .stream()
                .map(commentaireMapper::toResponseDTO)
                .toList();
    }

    // ✅ Nouveau
    @Override
    public CommentaireResponseDTO getById(String id) {
        Commentaire c = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));
        return commentaireMapper.toResponseDTO(c);
    }

    // ✅ Nouveau — très utile pour afficher les commentaires d'un post
    @Override
    public List<CommentaireResponseDTO> getByPostId(String postId) {
        return commentaireRepository.findByPostId(postId)
                .stream()
                .map(commentaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public CommentaireResponseDTO update(String id, CommentaireRequestDTO dto) {
        // ✅ Vérifier que c'est bien l'auteur
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));

        if (!commentaire.getAuthorUsername().equals(
                userRepository.findByEmail(email)
                        .map(User::getUsername)
                        .orElse(""))) {
            throw new RuntimeException("Vous ne pouvez modifier que vos propres commentaires");
        }

        commentaire.setContenu(dto.getContenu());
        return commentaireMapper.toResponseDTO(commentaireRepository.save(commentaire));
    }

    @Override
    public void delete(String id) {
        commentaireRepository.deleteById(id);
    }
}