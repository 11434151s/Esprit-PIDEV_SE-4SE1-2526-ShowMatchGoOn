package tn.esprit.spring.projet_pi_v2.service;

import tn.esprit.spring.projet_pi_v2.Entity.Fidelity;
import java.util.List;

public interface FidelityService {

    Fidelity addFidelity(Fidelity f);

    List<Fidelity> getAll();

    Fidelity getById(String id);

    Fidelity update(String id, Fidelity f);

    void delete(String id);
}
