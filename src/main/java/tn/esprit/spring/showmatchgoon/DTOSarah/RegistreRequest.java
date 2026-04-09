package tn.esprit.spring.showmatchgoon.DTOSarah;

import tn.esprit.spring.showmatchgoon.Entity.Role;

public record RegistreRequest(
        String email,
        String password,
        Role role
) {
}
