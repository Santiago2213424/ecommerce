package ecomer.pe.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecomer.pe.model.Usuario;
import ecomer.pe.model.Rol;
import ecomer.pe.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public void cambiarRol(Long id, Rol nuevoRol) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setRol(nuevoRol);
        usuarioRepository.save(u);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setActivo(activo);
        usuarioRepository.save(u);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
