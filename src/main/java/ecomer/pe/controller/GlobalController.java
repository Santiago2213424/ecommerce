package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ecomer.pe.model.Usuario;
import ecomer.pe.service.UsuarioInfo;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UsuarioInfo usuarioInfo;

    @ModelAttribute("usuarioSesion")
    public Usuario usuarioSesion(Authentication auth) {
        return usuarioInfo.getUsuarioLogeado(auth);
    }
}
