package tn.esprit.spring.projet_pi_v2.service;



import tn.esprit.spring.projet_pi_v2.Entity.Abonnement;

import java.util.List;

public interface AbonnementService {

    Abonnement addAbonnement(Abonnement a);

    List<Abonnement> getAll();

    Abonnement getById(String id);

    Abonnement update(String id, Abonnement a);

    void delete(String id);
}