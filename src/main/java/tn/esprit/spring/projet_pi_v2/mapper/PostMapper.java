package tn.esprit.spring.projet_pi_v2.mapper;

import org.mapstruct.*;
import tn.esprit.spring.projet_pi_v2.DTO.PostRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PostResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "datePublication", ignore = true)
    Post toEntity(PostRequestDTO dto);

    PostResponseDTO toResponseDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(PostRequestDTO dto, @MappingTarget Post post);
}