package tn.esprit.spring.showmatchgoon.DTOSarah.response;

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
