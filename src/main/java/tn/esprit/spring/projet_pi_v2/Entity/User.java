package tn.esprit.spring.projet_pi_v2.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    String id;

    String email;
    String motDePasse;
    Date dateInscription;
    Role role;
}

