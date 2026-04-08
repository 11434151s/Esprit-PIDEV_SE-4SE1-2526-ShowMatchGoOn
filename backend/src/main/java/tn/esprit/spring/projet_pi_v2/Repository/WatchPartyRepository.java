package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.projet_pi_v2.Entity.WatchParty;

public interface WatchPartyRepository extends MongoRepository<WatchParty, String> {
}