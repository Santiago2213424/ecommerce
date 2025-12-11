package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ecomer.pe.repository.ProductoRepository;
import ecomer.pe.repository.CategoriaRepository;

@Controller
public class ComprasController {

    @Autowired
    private ProductoRepository productRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/compras")
    public String tienda(Model model) {
        model.addAttribute("productos", productRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll()); // 🔹 MENÚ USARÁ ESTO
        return "compras";
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/compras";  // 🔥 Página principal
    }
    
}
