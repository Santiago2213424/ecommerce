package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ecomer.pe.model.Rol;
import ecomer.pe.model.Usuario;
import ecomer.pe.repository.UsuarioRepository;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String registroForm() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String correo,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // --- VALIDACIONES FRONTEND REFLEJADAS EN BACKEND ---
        
        // Nombre / Apellido vacíos
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            model.addAttribute("error", "El nombre y apellido son obligatorios.");
            return "registro";
        }

        // Correo válido
        if (!correo.contains("@") || !correo.contains(".")) {
            model.addAttribute("error", "Debe ingresar un correo válido.");
            return "registro";
        }

        // ✔ CORREO YA REGISTRADO
        if (usuarioRepo.findByCorreo(correo).isPresent()) {
            model.addAttribute("error", "Este correo ya está registrado.");
            return "registro";
        }

        // ✔ VALIDACIÓN DE CONTRASEÑA SEGÚN TU FORMULARIO
        // Debe tener:
        // - mínimo 7 caracteres
        // - al menos 1 mayúscula
        // - al menos 1 número
        if (!password.matches("(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{7,}")) {
            model.addAttribute("error",
                    "La contraseña debe tener mínimo 7 caracteres, 1 mayúscula y 1 número.");
            return "registro";
        }

        // Confirmar contraseña
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "registro";
        }

        // --- CREAR USUARIO ---
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setCorreo(correo);
        u.setUsername(correo);      // login con el correo
        u.setPassword(passwordEncoder.encode(password));
        u.setRol(Rol.USER);
        u.setActivo(true);

        usuarioRepo.save(u);

        model.addAttribute("success", "Cuenta creada con éxito. Ahora puedes iniciar sesión.");
        return "login";
    }
}
