package ecomer.pe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ecomer.pe.dto.*;
import ecomer.pe.repository.ProductoRepository;
import ecomer.pe.repository.UsuarioRepository;
import ecomer.pe.service.EstadisticasService;

@Controller
public class EstadisticasController {

    private final EstadisticasService estadisticasService;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public EstadisticasController(EstadisticasService estadisticasService,
                                  UsuarioRepository usuarioRepository,
                                  ProductoRepository productoRepository) {
        this.estadisticasService = estadisticasService;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {

        // DATOS DE ESTADÍSTICAS
        model.addAttribute("ventasMes", estadisticasService.ventasPorMes());
        model.addAttribute("productosMasVendidos", estadisticasService.productosMasVendidos());
        model.addAttribute("ventasPorCategoria", estadisticasService.ventasPorCategoria());

        // 🔹 NUEVOS DATOS PARA LAS TARJETAS
        model.addAttribute("usuariosTotales", usuarioRepository.count());
        model.addAttribute("productosTotales", productoRepository.count());

        // Si quieres: cantidad de meses con ventas
        model.addAttribute("mesesConVentas", estadisticasService.ventasPorMes().size());

        // Si quieres: productos con movimiento
        model.addAttribute("productosConMovimiento", estadisticasService.productosMasVendidos().size());

        return "estadisticas";
    }
}

