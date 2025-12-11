package ecomer.pe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ecomer.pe.model.Venta;
import ecomer.pe.repository.UsuarioRepository;
import ecomer.pe.repository.VentaRepository;

@Controller
public class PedidoUsuarioController {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/mis-pedidos")
    public String misPedidos(Model model, Authentication auth) {

        List<Venta> pedidos = ventaRepository.findByUsuario(auth.getName());

        model.addAttribute("pedidos", pedidos);

        return "usuario/mis_pedidos";
    }
}
