package ecomer.pe.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import ecomer.pe.model.*;
import ecomer.pe.service.ProductoService;
import ecomer.pe.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", carrito.stream().mapToDouble(CartItem::getSubtotal).sum());
        return "carrito/carrito";
    }

    @PostMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {

        Producto producto = productoService.findById(id);
        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        if (producto.getCantidad() < cantidad) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Stock insuficiente. Disponible: " + producto.getCantidad());
            return redirigir(auth);
        }

        Optional<CartItem> existente = carrito.stream()
                .filter(c -> c.getProducto().getId().equals(id))
                .findFirst();

        if (existente.isPresent()) {
            int nuevaCantidad = existente.get().getCantidad() + cantidad;

            if (nuevaCantidad > producto.getCantidad()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Stock insuficiente.");
                return redirigir(auth);
            }
            existente.get().setCantidad(nuevaCantidad);
        } else {
            carrito.add(new CartItem(producto, cantidad));
        }

        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("successMessage", "Producto agregado al carrito");

        return redirigir(auth);
    }

    private String redirigir(Authentication auth) {
        return (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                ? "redirect:/dashboard"
                : "redirect:/compras";
    }

    @GetMapping("/pago")
    public String pago(HttpSession session, Model model, RedirectAttributes redirect) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            redirect.addFlashAttribute("errorMessage", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", carrito.stream().mapToDouble(CartItem::getSubtotal).sum());
        return "carrito/pago";
    }

    @PostMapping("/pago/confirmar")
    public String confirmarPago(@RequestParam("modalidad") String modalidad,
                                @RequestParam(value="direccion", required=false) String direccion,
                                @RequestParam(value="referencia", required=false) String referencia,
                                @RequestParam("metodo") String metodo,
                                @RequestParam("dni") String dni,
                                @RequestParam("numero") String numero,
                                Authentication auth,
                                HttpSession session,
                                RedirectAttributes redirect) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            redirect.addFlashAttribute("errorMessage", "No hay productos para procesar.");
            return "redirect:/carrito";
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(carrito.stream().mapToDouble(CartItem::getSubtotal).sum());
        venta.setModalidadEntrega(modalidad);
        venta.setDireccionEntrega(direccion);
        venta.setReferencia(referencia);
        venta.setMetodoPago(metodo);
        venta.setDni(dni);
        venta.setUsuario(auth.getName());
        venta.setNumero(numero); 
        venta.setEstado(EstadoVenta.TIENDA);

        venta = ventaRepository.save(venta);

        for (CartItem item : carrito) {
            VentaDetalle det = new VentaDetalle();
            det.setVenta(venta);
            det.setProducto(item.getProducto());
            det.setCantidad(item.getCantidad());
            det.setSubtotal(item.getSubtotal());
            ventaDetalleRepository.save(det);

            Producto p = item.getProducto();
            p.setCantidad(p.getCantidad() - item.getCantidad());
            productoRepository.save(p);
        }

        session.removeAttribute("carrito");
        redirect.addFlashAttribute("successMessage", "Compra realizada con éxito!");
        return "redirect:/dashboard";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id,
                                     HttpSession session,
                                     Authentication auth,
                                     RedirectAttributes redirect) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            redirect.addFlashAttribute("errorMessage", "El carrito está vacío.");
            return "redirect:/carrito";
        }

        // eliminar el producto
        carrito.removeIf(item -> item.getProducto().getId().equals(id));

        session.setAttribute("carrito", carrito);

        redirect.addFlashAttribute("successMessage", "Producto eliminado del carrito.");

        // redirigir según tipo de usuario
        return (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                ? "redirect:/carrito"
                : "redirect:/carrito-usuario";
    }

}
