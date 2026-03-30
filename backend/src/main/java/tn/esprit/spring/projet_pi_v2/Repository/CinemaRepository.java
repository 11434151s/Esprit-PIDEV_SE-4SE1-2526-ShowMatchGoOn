package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.projet_pi_v2.Entity.Cinema;

@Repository
public interface CinemaRepository extends MongoRepository<Cinema, String> {
}
