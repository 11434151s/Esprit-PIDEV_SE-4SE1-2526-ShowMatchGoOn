package tn.esprit.spring.showmatchgoon.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.showmatchgoon.Entity.Seance;

import java.util.List;

@Repository
public interface SeanceRepository extends MongoRepository<Seance, String> {

    List<Seance> findByCinemaId(String cinemaId);
}
