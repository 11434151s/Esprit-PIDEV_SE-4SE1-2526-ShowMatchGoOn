package tn.esprit.spring.showmatchgoon.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notifications")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Notification {

    @Id
    String id;

    String message;
    Date dateEnvoi;
    boolean lu;

    NotificationType type;
    String contenuId;
}

