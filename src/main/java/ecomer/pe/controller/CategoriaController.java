package ecomer.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecomer.pe.model.Categoria;
import ecomer.pe.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("categorias", service.findAll());
        return "categorias/list";
    }

    @GetMapping("/new")
    public String form() {
        return "categorias/new";
    }

    @PostMapping("/save")
    public String save(@RequestParam("nombre") String nombre, RedirectAttributes redirectAttrs) {
        // Validar si ya existe la categoría
        if (service.existsByNombre(nombre)) {
            redirectAttrs.addFlashAttribute("errorMessage", "Ya existe una categoría con ese nombre");
            return "redirect:/categorias/new";
        }

        Categoria c = new Categoria(nombre);
        service.save(c);
        redirectAttrs.addFlashAttribute("successMessage", "Categoría creada correctamente");
        return "redirect:/categorias";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs){
        if (service.canDelete(id)) {
            service.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "Categoría eliminada correctamente");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "Esta categoría no se puede eliminar porque tiene productos");
        }
        return "redirect:/categorias";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Categoria c = service.findById(id);
        model.addAttribute("categoria", c);
        return "categorias/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id, @RequestParam String nombre, RedirectAttributes redirectAttrs) {
        Categoria c = service.findById(id);
        if (c != null) {
            c.setNombre(nombre);
            service.save(c);
            redirectAttrs.addFlashAttribute("successMessage", "Categoría actualizada correctamente");
        }
        return "redirect:/categorias";
    }
}
