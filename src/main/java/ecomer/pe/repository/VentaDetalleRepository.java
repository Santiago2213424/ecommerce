package ecomer.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecomer.pe.model.VentaDetalle;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
	
}