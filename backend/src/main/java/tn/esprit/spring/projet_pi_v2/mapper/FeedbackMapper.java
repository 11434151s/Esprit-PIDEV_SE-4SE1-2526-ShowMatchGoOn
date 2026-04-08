package tn.esprit.spring.projet_pi_v2.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import tn.esprit.spring.projet_pi_v2.DTORanim.FeedbackResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.Feedback;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FeedbackMapper {

    FeedbackResponseDTO toResponseDTO(Feedback feedback);
}