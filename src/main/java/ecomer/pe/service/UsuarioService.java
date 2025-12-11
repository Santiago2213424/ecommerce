package ecomer.pe.service;

import java.util.List;
import ecomer.pe.model.Usuario;
import ecomer.pe.model.Rol;

public interface UsuarioService {

    List<Usuario> listarTodos();

    void cambiarRol(Long id, Rol nuevoRol);

    void cambiarEstado(Long id, boolean activo);

    void eliminarUsuario(Long id);
}
