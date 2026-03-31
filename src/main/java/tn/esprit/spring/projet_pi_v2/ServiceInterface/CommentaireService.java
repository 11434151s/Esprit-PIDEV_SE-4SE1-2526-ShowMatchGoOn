package tn.esprit.spring.projet_pi_v2.ServiceInterface;

import tn.esprit.spring.projet_pi_v2.DTO.CommentaireRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireResponseDTO;
import java.util.List;

public interface CommentaireService {

    CommentaireResponseDTO create(CommentaireRequestDTO dto);
    List<CommentaireResponseDTO> getAll();
    CommentaireResponseDTO getById(String id);
    List<CommentaireResponseDTO> getByPostId(String postId);
    CommentaireResponseDTO update(String id, CommentaireRequestDTO dto);
    void delete(String id);
}