package ecomer.pe.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ecomer.pe.model.Producto;
import ecomer.pe.repository.ProductoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repo;

    public List<Producto> findAll() {
        return repo.findAll(); // carga productos con categorías
    }

    @Override
    public Producto save(Producto p) {
        return repo.save(p);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Producto findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
