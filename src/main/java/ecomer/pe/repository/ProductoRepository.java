package ecomer.pe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ecomer.pe.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	long countByCategoriaId(Long categoriaId);
	// Método para validar si existe producto por nombre
    Optional<Producto> findByNombreIgnoreCase(String nombre);
}


