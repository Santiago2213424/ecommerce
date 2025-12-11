package ecomer.pe.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecomer.pe.model.*;
import ecomer.pe.service.ProductoService;
import ecomer.pe.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/carrito-usuario")
public class CarritoUsuarioController {

    @Autowired private ProductoService productoService;
    @Autowired private VentaRepository ventaRepository;
    @Autowired private VentaDetalleRepository ventaDetalleRepository;
    @Autowired private ProductoRepository productoRepository;

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carritoUsuario");
        if (carrito == null) carrito = new ArrayList<>();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", carrito.stream().mapToDouble(CartItem::getSubtotal).sum());

        return "carrito/usuario_carrito";
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session,
                          RedirectAttributes redirect) {

        Producto p = productoService.findById(id);
        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carritoUsuario");
        if (carrito == null) carrito = new ArrayList<>();

        if (p.getCantidad() < cantidad) {
            redirect.addFlashAttribute("errorMessage", "Stock insuficiente.");
            return "redirect:/compras";
        }

        Optional<CartItem> existente = carrito.stream()
                .filter(c -> c.getProducto().getId().equals(id))
                .findFirst();

        if (existente.isPresent()) {
            int nuevaCantidad = existente.get().getCantidad() + cantidad;

            if (nuevaCantidad > p.getCantidad()) {
                redirect.addFlashAttribute("errorMessage", "No hay suficiente stock");
                return "redirect:/compras";
            }

            existente.get().setCantidad(nuevaCantidad);

        } else {
            carrito.add(new CartItem(p, cantidad));
        }

        session.setAttribute("carritoUsuario", carrito);
        redirect.addFlashAttribute("successMessage", "Producto agregado");

        return "redirect:/carrito-usuario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carritoUsuario");

        if (carrito != null)
            carrito.removeIf(c -> c.getProducto().getId().equals(id));

        return "redirect:/carrito-usuario";
    }

    @GetMapping("/pago")
    public String pago(Model model, HttpSession session) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carritoUsuario");

        if (carrito == null || carrito.isEmpty())
            return "redirect:/carrito-usuario";

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", carrito.stream().mapToDouble(CartItem::getSubtotal).sum());
        return "carrito/usuario_pago";
    }

    // 🚀 MÉTODO NUEVO QUE SOLUCIONA EL ERROR
    @PostMapping("/pago/confirmar")
    public String confirmarPagoUsuario(@RequestParam("modalidad") String modalidad,
                                       @RequestParam(value="direccion", required=false) String direccion,
                                       @RequestParam(value="referencia", required=false) String referencia,
                                       @RequestParam("metodo") String metodo,
                                       @RequestParam("dni") String dni,
                                       @RequestParam(value="numero", required=false) String numero,
                                       Authentication auth,   // ⬅️ AGREGA ESTO TAMBIÉN
                                       HttpSession session,
                                       RedirectAttributes redirect) {

        List<CartItem> carrito = (List<CartItem>) session.getAttribute("carritoUsuario");

        if (carrito == null || carrito.isEmpty()) {
            redirect.addFlashAttribute("errorMessage", "No hay productos.");
            return "redirect:/carrito-usuario";
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(carrito.stream().mapToDouble(CartItem::getSubtotal).sum());
        venta.setModalidadEntrega(modalidad);
        venta.setDireccionEntrega(direccion);
        venta.setReferencia(referencia);
        venta.setMetodoPago(metodo);
        venta.setDni(dni);
        venta.setNumero(numero);
        venta.setEstado(EstadoVenta.TIENDA);

        // 🔥 AQUÍ VA LA LÍNEA QUE NECESITAS
        venta.setUsuario(auth.getName());

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

        session.removeAttribute("carritoUsuario");
        redirect.addFlashAttribute("successMessage", "Compra realizada con éxito!");

        return "redirect:/compras";
    }
}
