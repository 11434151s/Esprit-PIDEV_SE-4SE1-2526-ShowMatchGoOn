package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "commentaires")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Commentaire {

    @Id
    String id;

    String contenu;
    Date dateCommentaire;

    String postId;
    String clientId;
}

