package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "contenus")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Contenu {

    @Id
    String id;

    String titre;
    String description;
    String genre;
    int duree;
    Date dateSortie;

    List<String> seanceIds;
    List<String> notificationIds;
}

