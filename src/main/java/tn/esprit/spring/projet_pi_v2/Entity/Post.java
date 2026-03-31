package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id
    private String id;

    private String titre;
    private String contenu;
    private Date datePublication;

    // ✅ Ajout : lien vers l'auteur
    private String authorId;
    private String authorUsername;
}
