package tn.esprit.spring.projet_pi_v2.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestSecureController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, secured world!";
    }
}


