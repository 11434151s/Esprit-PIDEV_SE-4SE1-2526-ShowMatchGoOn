package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "abonnements")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Abonnement {

    @Id
    String id;

    Date dateDebut;
    Date dateFin;
    double prix;

    AbonnementType type;
    String clientId;
}

