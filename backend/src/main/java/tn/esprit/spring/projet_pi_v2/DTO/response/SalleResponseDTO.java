package tn.esprit.spring.projet_pi_v2.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalleResponseDTO {

    private String id;
    private String name;
    private Integer capacity;
}
