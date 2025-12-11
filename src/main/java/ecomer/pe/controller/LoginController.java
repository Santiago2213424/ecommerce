package ecomer.pe.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/redirect")
    public String redirigirSegunRol(Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/dashboard";
        }

        return "redirect:/compras"; // Usuario normal
    }
}
