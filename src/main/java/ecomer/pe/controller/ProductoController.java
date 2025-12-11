package ecomer.pe.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecomer.pe.model.Categoria;
import ecomer.pe.model.Producto;
import ecomer.pe.repository.CategoriaRepository;
import ecomer.pe.repository.ProductoRepository;
import ecomer.pe.service.ProductoService;

@Controller
public class ProductoController {

    private final ProductoRepository productRepository;
    
    @Autowired
    private ProductoService service;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private String uploadDir = "src/main/resources/static/uploads/";

    ProductoController(ProductoRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping({"/products"})
    public String listProducts(Model model){
    	model.addAttribute("products", service.findAll() != null ? service.findAll() : new ArrayList<>());
        return "products/list";
    }

    @GetMapping("/products/new")
    public String showForm(Model model){
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "products/new";
    }

    @PostMapping("/products/save")
    public String saveProduct(
        @RequestParam("nombre") String nombre,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("precio") Double precio,
        @RequestParam("cantidad") int cantidad,
        @RequestParam("categoriaId") Long categoriaId,
        @RequestParam("imagen") MultipartFile imagen,
        RedirectAttributes redirectAttrs
    ) throws IOException {

        // Validar si ya existe producto con ese nombre
        if (productRepository.findByNombreIgnoreCase(nombre).isPresent()) {
            redirectAttrs.addFlashAttribute("errorMessage", "Ya existe un producto con ese nombre");
            return "redirect:/products/new";
        }

        String carpetaUploads = "uploads/";
        Path ruta = Paths.get(carpetaUploads);
        if (!Files.exists(ruta)) Files.createDirectories(ruta);

        String nombreArchivo = imagen.getOriginalFilename();
        Path rutaArchivo = ruta.resolve(nombreArchivo);
        Files.write(rutaArchivo, imagen.getBytes());

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCantidad(cantidad);
        p.setImagen(nombreArchivo);

        Categoria cat = categoriaRepository.findById(categoriaId).orElse(null);
        p.setCategoria(cat);

        productRepository.save(p);
        redirectAttrs.addFlashAttribute("successMessage", "Producto creado correctamente");

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        Producto p = productRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/products";

        model.addAttribute("product", p);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "products/edit";
    }

    @PostMapping("/products/update")
    public String updateProduct(
        @RequestParam("id") Long id,
        @RequestParam("nombre") String nombre,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("precio") Double precio,
        @RequestParam("cantidad") int cantidad,
        @RequestParam("categoriaId") Long categoriaId,
        @RequestParam("imagen") MultipartFile imagen,
        RedirectAttributes redirectAttrs
    ) throws IOException {

        Producto p = productRepository.findById(id).orElse(null);
        if (p == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Producto no encontrado");
            return "redirect:/products";
        }

        // Validar nombre duplicado (excluyendo el producto actual)
        boolean nombreDuplicado = productRepository.findByNombreIgnoreCase(nombre)
                .filter(prod -> !prod.getId().equals(id))
                .isPresent();

        if (nombreDuplicado) {
            redirectAttrs.addFlashAttribute("errorMessage", "Ya existe otro producto con ese nombre");
            return "redirect:/products/edit/" + id;
        }

        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCantidad(cantidad);

        Categoria cat = categoriaRepository.findById(categoriaId).orElse(null);
        p.setCategoria(cat);

        if (!imagen.isEmpty()) {
            String carpetaUploads = "uploads/";
            Path ruta = Paths.get(carpetaUploads);
            if (!Files.exists(ruta)) Files.createDirectories(ruta);
            String nombreArchivo = imagen.getOriginalFilename();
            Files.write(ruta.resolve(nombreArchivo), imagen.getBytes());
            p.setImagen(nombreArchivo);
        }

        productRepository.save(p);
        redirectAttrs.addFlashAttribute("successMessage", "Producto actualizado correctamente");

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        service.delete(id);
        redirectAttrs.addFlashAttribute("successMessage", "Producto eliminado correctamente");
        return "redirect:/products";
    }
    @GetMapping("/listaproductos/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
    	Producto producto = service.findById(id);
        model.addAttribute("producto", producto);
        return "producto_detalle";
    }
}
