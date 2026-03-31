package tn.esprit.spring.projet_pi_v2.DTO;

import tn.esprit.spring.projet_pi_v2.Entity.Role;

public record RegistreRequest(
        String email,
        String username,
        String password,
        Role role
) {
}
