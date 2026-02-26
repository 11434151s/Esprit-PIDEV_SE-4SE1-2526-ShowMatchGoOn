package tn.esprit.spring.projet_pi_v2.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor @NoArgsConstructor
public class Serie extends Contenu {

    int nombreSaisons;
    List<String> episodeIds;
}

