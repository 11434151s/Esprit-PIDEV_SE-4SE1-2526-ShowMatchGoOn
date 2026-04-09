package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "clients")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User {

    String pseudo;
    String preferences;

    String abonnementId;
    List<String> feedbackIds;
    List<String> promotionIds;
    List<String> commentaireIds;
    List<String> watchPartyIds;
    String profilId;
}

