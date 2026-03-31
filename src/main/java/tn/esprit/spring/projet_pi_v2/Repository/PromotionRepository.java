package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.Promotion;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends MongoRepository<Promotion, String> {

    List<Promotion> findByClientId(String clientId);

    Optional<Promotion> findByCode(String code); // ✅ Optional au lieu de null

    List<Promotion> findByActiveTrue(); // ✅ Ajout pour récupérer promos actives
}