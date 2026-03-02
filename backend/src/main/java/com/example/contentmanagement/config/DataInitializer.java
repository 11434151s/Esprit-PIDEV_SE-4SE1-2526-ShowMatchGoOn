package com.example.contentmanagement.config;

import com.example.contentmanagement.entity.Role;
import com.example.contentmanagement.entity.User;
import com.example.contentmanagement.repository.RoleRepository;
import com.example.contentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            Role userRole = roleRepository.save(Role.builder().name("ROLE_USER").build());

            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@example.com")
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.save(admin);

                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .email("user@example.com")
                        .roles(Set.of(userRole))
                        .build();
                userRepository.save(user);
            }
        }
    }
}
