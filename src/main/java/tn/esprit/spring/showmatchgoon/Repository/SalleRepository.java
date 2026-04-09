package tn.esprit.spring.showmatchgoon.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.showmatchgoon.Entity.Salle;

@Repository
public interface SalleRepository extends MongoRepository<Salle, String> {
}
