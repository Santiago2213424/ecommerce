package ecomer.pe.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ecomer.pe.model.Usuario;

import java.util.Collection;
import java.util.List;

public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario){
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // 🔥 Aquí obtenemos el nombre real del usuario
    public String getNombre() {
        return usuario.getNombre();
    }
    public String getApellido() { return usuario.getApellido(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 🔥 Convertimos tu enum Rol en un "ROLE_xxx" para Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo(); // el usuario inicia sesión con correo
    }
    
    public String getNombreCompleto() {
        String nom = usuario.getNombre();
        String ape = usuario.getApellido();

        return capitalize(nom) + " " + capitalize(ape);
    }

    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0,1).toUpperCase() + texto.substring(1).toLowerCase();
    }


    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return usuario.isActivo(); }
}
