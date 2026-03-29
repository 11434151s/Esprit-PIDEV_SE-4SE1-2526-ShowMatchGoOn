package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

@Document(collection = "abonnements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abonnement {

    @Id
    private String id;

    @NotNull(message = "Le type d'abonnement est obligatoire")
    private AbonnementType type;

    @Positive(message = "Le prix doit être positif")
    private double prix;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 500, message = "La description doit contenir entre 10 et 500 caractères")
    private String description;
}