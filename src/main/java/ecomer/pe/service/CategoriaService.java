package ecomer.pe.service;

import java.util.List;
import ecomer.pe.model.Categoria;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria save(Categoria categoria);
    void delete(Long id);
    Categoria findById(Long id);
    boolean canDelete(Long id);
    boolean existsByNombre(String nombre);
}
