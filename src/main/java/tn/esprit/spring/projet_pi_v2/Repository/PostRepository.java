package tn.esprit.spring.projet_pi_v2.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
}