package ecomer.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import ecomer.pe.model.Usuario;
import ecomer.pe.repository.UsuarioRepository;

@Component
public class UsuarioInfo {

    @Autowired
    private UsuarioRepository repo;

    public Usuario getUsuarioLogeado(Authentication auth) {
        if (auth == null) {
            return null;
        }
        return repo.findByCorreo(auth.getName()).orElse(null);
    }
}
