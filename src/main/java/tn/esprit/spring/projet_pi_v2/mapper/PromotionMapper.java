package tn.esprit.spring.projet_pi_v2.mapper;



import org.mapstruct.*;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionRequestDTO;
import tn.esprit.spring.projet_pi_v2.DTO.PromotionResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Promotion;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    Promotion toEntity(PromotionRequestDTO dto);

    // Entity → Response
    PromotionResponseDTO toResponseDTO(Promotion promotion);

    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePromotionFromDto(PromotionRequestDTO dto, @MappingTarget Promotion promotion);
}