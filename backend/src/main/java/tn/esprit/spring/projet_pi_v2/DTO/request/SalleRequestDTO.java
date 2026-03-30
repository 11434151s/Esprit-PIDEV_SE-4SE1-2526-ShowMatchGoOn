package tn.esprit.spring.projet_pi_v2.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalleRequestDTO {

    @NotBlank(message = "Le nom de la salle est obligatoire")
    private String name;

    @NotNull(message = "La capacité est obligatoire")
    @Positive(message = "La capacité doit être strictement positive")
    private Integer capacity;
}
