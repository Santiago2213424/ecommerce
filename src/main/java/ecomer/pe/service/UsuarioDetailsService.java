package ecomer.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ecomer.pe.model.Usuario;
import ecomer.pe.repository.UsuarioRepository;
import ecomer.pe.security.UsuarioDetails;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        
        Usuario u = repo.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado: " + correo));

        return new UsuarioDetails(u);

    }
}
