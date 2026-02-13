package dio.spring_security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to my Spring Boot 3 API (Java 22)";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('USERS', 'MANAGERS')") // Segurança extra direto no método
    public String users() {
        return "Authorized user content";
    }

    @GetMapping("/managers")
    @PreAuthorize("hasRole('MANAGERS')")
    public String managers() {
        return "Authorized manager content";
    }
}