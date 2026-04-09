package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "cinemas")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Cinema {

    @Id
    String id;

    String nom;


    String adresse;

    String ville;


}

