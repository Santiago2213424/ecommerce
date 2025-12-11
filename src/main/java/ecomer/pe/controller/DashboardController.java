package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ecomer.pe.repository.ProductoRepository;
import ecomer.pe.repository.CategoriaRepository;
import ecomer.pe.repository.VentaRepository;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class DashboardController {

    @Autowired
    private ProductoRepository productRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    //Dashboard    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletResponse response) {

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("productsCount", productRepository.count());
        model.addAttribute("categoriesCount", categoriaRepository.count());

        LocalDate today = LocalDate.now();
        LocalDateTime inicio = today.atStartOfDay();
        LocalDateTime fin = today.atTime(23, 59, 59);

        double totalVentasDia = ventaRepository.findVentasDelDia(inicio, fin)
                .stream()
                .mapToDouble(v -> v.getTotal())
                .sum();

        model.addAttribute("ventasDia", totalVentasDia);

        return "dashboard";
    }
}
