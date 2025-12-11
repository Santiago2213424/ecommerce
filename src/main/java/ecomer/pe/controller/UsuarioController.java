package ecomer.pe.controller;

import ecomer.pe.model.Rol;
import ecomer.pe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios") // 🔥 Agrupa todas las rutas
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 📌 LISTAR USUARIOS
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios";
    }

    // 🔥 CAMBIAR ROL
    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam String nuevoRol) {
        usuarioService.cambiarRol(id, Rol.valueOf(nuevoRol));
        return "redirect:/usuarios";
    }

    // ⛔ INHABILITAR USUARIO
    @PostMapping("/inhabilitar/{id}")
    public String inhabilitar(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, false);
        return "redirect:/usuarios";
    }

    // ✅ HABILITAR USUARIO
    @PostMapping("/habilitar/{id}")
    public String habilitar(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, true);
        return "redirect:/usuarios";
    }

    // ❌ ELIMINAR USUARIO DEFINITIVAMENTE
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

}
