package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ecomer.pe.model.Usuario;
import ecomer.pe.repository.UsuarioRepository;
import ecomer.pe.security.UsuarioDetails;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/perfil")
    public String perfilUsuario(Model model, Authentication auth) {

        UsuarioDetails userDetails = (UsuarioDetails) auth.getPrincipal();
        Usuario usuario = userDetails.getUsuario(); // obtener usuario real

        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @GetMapping("/perfil/cambiar-password")
    public String cambiarPasswordForm() {
        return "cambiar-password";
    }

    @PostMapping("/perfil/cambiar-password")
    public String cambiarPasswordProcesar(
            @RequestParam String actual,
            @RequestParam String nueva,
            @RequestParam String confirmar,
            Authentication auth,
            Model model) {

        // Obtener usuario REAL desde UsuarioDetails
        UsuarioDetails userDetails = (UsuarioDetails) auth.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        // Verificar contraseña actual (encriptada)
        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            model.addAttribute("error", "La contraseña actual no es correcta.");
            return "cambiar-password";
        }

        // Confirmación
        if (!nueva.equals(confirmar)) {
            model.addAttribute("error", "Las nuevas contraseñas no coinciden.");
            return "cambiar-password";
        }

        // Guardar contraseña nueva encriptada
        usuario.setPassword(passwordEncoder.encode(nueva));
        usuarioRepository.save(usuario);

        model.addAttribute("success", "Contraseña actualizada correctamente.");

        return "cambiar-password";
    }
}
