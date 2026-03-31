package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.Commentaire;
import java.util.List;

public interface CommentaireRepository extends MongoRepository<Commentaire, String> {

    List<Commentaire> findByPostId(String postId);

    void deleteByPostId(String postId);
}
