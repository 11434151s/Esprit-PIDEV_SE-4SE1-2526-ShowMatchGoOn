package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "feedbacks")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    String id;

    int note;
    String commentaire;
    Date dateFeedback;

    String clientId;
    String watchPartyId;

    int likes = 0;
    int dislikes = 0;
    List<String> likedByUserIds = new ArrayList<>();
    List<String> dislikedByUserIds = new ArrayList<>();
}