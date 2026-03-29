package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.Fidelity;

public interface FidelityRepository extends MongoRepository<Fidelity, String> {
}
