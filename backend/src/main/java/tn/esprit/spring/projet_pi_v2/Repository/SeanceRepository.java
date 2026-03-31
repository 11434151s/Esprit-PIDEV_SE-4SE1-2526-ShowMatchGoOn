package tn.esprit.spring.projet_pi_v2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.projet_pi_v2.Entity.Seance;

import java.util.List;

@Repository
public interface SeanceRepository extends MongoRepository<Seance, String> {

    List<Seance> findByCinemaId(String cinemaId);
}
