package ecomer.pe.service;

import java.util.List;
import ecomer.pe.model.Producto;
import jakarta.servlet.http.HttpSession;

public interface ProductoService {
    List<Producto> findAll();
    Producto save(Producto p);
    void delete(Long id);
    Producto findById(Long id);
}


