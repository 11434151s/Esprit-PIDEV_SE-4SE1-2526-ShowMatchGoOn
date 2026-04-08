package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {

    void deleteByWatchPartyId(String watchPartyId);

        boolean existsByWatchPartyIdAndClientId(String watchPartyId, String clientId);
    List<Feedback> findByWatchPartyId(String watchPartyId);

}