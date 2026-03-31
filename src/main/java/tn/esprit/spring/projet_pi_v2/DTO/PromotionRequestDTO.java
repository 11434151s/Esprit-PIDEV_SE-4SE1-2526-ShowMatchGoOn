package tn.esprit.spring.projet_pi_v2.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class PromotionRequestDTO {

    @NotBlank(message = "Le code promotion est obligatoire")
    @Size(min = 3, max = 20, message = "Le code doit contenir entre 3 et 20 caractères")
    private String code;

    @Min(value = 1, message = "La réduction doit être au minimum 1%")
    @Max(value = 100, message = "La réduction ne peut pas dépasser 100%")
    private double pourcentageReduction;

    @Future(message = "La date d'expiration doit être dans le futur")
    @NotNull(message = "La date d'expiration est obligatoire")
    private Date dateExpiration;

    // ✅ Optionnel : null = promo globale, non-null = promo ciblée
    private String clientId;
}