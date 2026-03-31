package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.*;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.CommentaireResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Commentaire;

@Mapper(componentModel = "spring")
public interface CommentaireMapper {

    // ✅ INSTANCE supprimé — Spring l'injecte automatiquement
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCommentaire", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "authorUsername", ignore = true)
    Commentaire toEntity(CommentaireRequestDTO dto);

    CommentaireResponseDTO toResponseDTO(Commentaire commentaire);
}