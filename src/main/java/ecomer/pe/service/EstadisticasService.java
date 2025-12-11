package ecomer.pe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ecomer.pe.dto.*;
import ecomer.pe.repository.VentaRepository;

@Service
public class EstadisticasService {

    private final VentaRepository ventaRepository;

    public EstadisticasService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<VentasPorMesDTO> ventasPorMes() {
        return ventaRepository.obtenerVentasPorMes();
    }

    public List<VentasPorCategoriaDTO> ventasPorCategoria() {
        return ventaRepository.obtenerVentasPorCategoria();
    }

    public List<ProductosMasVendidosDTO> productosMasVendidos() {
        return ventaRepository.obtenerProductosMasVendidos();
    }
}
