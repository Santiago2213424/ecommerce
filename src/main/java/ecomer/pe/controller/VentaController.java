package ecomer.pe.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ecomer.pe.model.EstadoVenta;
import ecomer.pe.model.Venta;
import ecomer.pe.repository.VentaRepository;
import ecomer.pe.service.ExcelService;
import ecomer.pe.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PdfService pdfService;


    // 🔹 LISTAR VENTAS (Con filtro opcional)
    @GetMapping
    public String listarVentas(@RequestParam(required = false) String filter, Model model) {

        List<Venta> ventas;

        if (filter != null && !filter.trim().isEmpty()) {
            String filtroLower = filter.toLowerCase();

            ventas = ventaRepository.findAll().stream()
                    .filter(v ->
                        String.valueOf(v.getId()).contains(filtroLower)
                        || (v.getFecha() != null
                            && v.getFecha().toString().toLowerCase().contains(filtroLower))
                        || (v.getModalidadEntrega() != null
                            && v.getModalidadEntrega().toLowerCase().contains(filtroLower))
                        || (v.getDireccionEntrega() != null
                            && v.getDireccionEntrega().toLowerCase().contains(filtroLower))
                        || (v.getEstado() != null
                            && v.getEstado().toString().toLowerCase().contains(filtroLower))
                    )
                    .toList();

        } else {
            ventas = ventaRepository.findAll();
        }

        model.addAttribute("ventas", ventas);
        model.addAttribute("filter", filter);

        return "ventas/ventas-reporte";
    }


    // 🔹 ACTUALIZAR ESTADO
    @PostMapping("/estado/{id}")
    public String actualizarEstado(@PathVariable Long id, @RequestParam("estado") EstadoVenta estado) {
        Venta venta = ventaRepository.findById(id).orElseThrow();
        venta.setEstado(estado);
        ventaRepository.save(venta);
        return "redirect:/ventas";
    }


    // 🔹 EXPORTAR A EXCEL (filtrado opcional)
    @GetMapping("/export/excel")
    public void exportExcel(
            @RequestParam(required = false) String filter,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ventas.xlsx");

        excelService.exportarVentas(response.getOutputStream(), filter);
    }


    // 🔹 EXPORTAR A PDF (filtrado opcional)
    @GetMapping("/export/pdf")
    public void exportPdf(
            @RequestParam(required = false) String filter,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ventas.pdf");

        pdfService.generarReporte(response.getOutputStream(), filter);
    }
}
