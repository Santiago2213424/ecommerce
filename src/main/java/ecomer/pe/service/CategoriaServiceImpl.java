package ecomer.pe.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ecomer.pe.model.Categoria;
import ecomer.pe.repository.CategoriaRepository;
import ecomer.pe.repository.ProductoRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository repo;
    
    @Autowired
    private ProductoRepository productRepo;

    @Override
    public List<Categoria> findAll() {
        return repo.findAll();
    }

    @Override
    public Categoria save(Categoria categoria) {
        return repo.save(categoria);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Categoria findById(Long id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public boolean canDelete(Long id) {
        return productRepo.countByCategoriaId(id) == 0;
    }
    @Override
    public boolean existsByNombre(String nombre) {
        return repo.findByNombreIgnoreCase(nombre).isPresent();
    }
}
