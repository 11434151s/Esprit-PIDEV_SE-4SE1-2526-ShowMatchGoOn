package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "watchparties")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WatchParty {

    @Id
    String id;

    String titre;
    Date dateCreation;
    String statut;

    String clientId; //host
    String adminId;
    String contenuId;

    List<String> participantIds = new ArrayList<>();
    ;
    List<String> reservationIds = new ArrayList<>();
    ;
}

