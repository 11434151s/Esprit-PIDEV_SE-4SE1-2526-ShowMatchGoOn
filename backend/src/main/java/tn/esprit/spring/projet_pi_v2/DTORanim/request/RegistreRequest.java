package tn.esprit.spring.projet_pi_v2.DTORanim.request;

import tn.esprit.spring.projet_pi_v2.Entity.Role;

public record RegistreRequest(
        String email,
        String password,
        Role role
) {
}
