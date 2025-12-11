package ecomer.pe.service;

import java.io.OutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecomer.pe.model.Venta;
import ecomer.pe.repository.VentaRepository;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class PdfService {

    @Autowired
    private VentaRepository ventaRepo;

    public void generarReporte(OutputStream out, String filter) throws IOException {

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        document.setMargins(20, 20, 20, 20);

        // 🔹 Título
        document.add(
            new Paragraph("📄 Reporte de Ventas")
            .setFontSize(20)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20)
        );

        // 📌 NUEVAS COLUMNAS → Se añadió DNI / Referencia / Número Yape
        float[] columnSizes = new float[]{
            1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 3f, 2f
        };

        Table table = new Table(UnitValue.createPercentArray(columnSizes))
                .setWidth(UnitValue.createPercentValue(100));

        // Encabezados actualizados
        String[] headers = {
            "ID", "Fecha", "Total",
            "DNI", "Modalidad",
            "Dirección", "Referencia",
            "N° Yape", "Productos",
            "Estado"
        };

        for (String h : headers) {
            table.addHeaderCell(
                new Cell()
                    .add(new Paragraph(h).setBold().setFontSize(10))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setFontColor(ColorConstants.WHITE)
                    .setPadding(5)
                    .setTextAlignment(TextAlignment.CENTER)
            );
        }

        // 📌 Obtener todas las ventas
        List<Venta> ventas = ventaRepo.findAll();

        // 🔍 FILTRO ACTUALIZADO CON LOS NUEVOS CAMPOS
        if (filter != null && !filter.isBlank()) {
            String f = filter.toLowerCase();

            ventas = ventas.stream()
                    .filter(v ->
                        (v.getFecha() != null && v.getFecha().format(formatter).toLowerCase().contains(f)) ||
                        String.valueOf(v.getTotal()).contains(f) ||
                        (v.getDni() != null && v.getDni().toLowerCase().contains(f)) ||
                        (v.getReferencia() != null && v.getReferencia().toLowerCase().contains(f)) ||
                        (v.getNumero() != null && v.getNumero().toLowerCase().contains(f)) ||
                        (v.getDireccionEntrega() != null && v.getDireccionEntrega().toLowerCase().contains(f)) ||
                        (v.getModalidadEntrega() != null && v.getModalidadEntrega().toLowerCase().contains(f))
                    )
                    .toList();
        }

        // 📌 Rellenar tabla
        for (Venta v : ventas) {

            String productos = v.getDetalles().stream()
                    .map(d -> d.getProducto().getNombre() + " - Cant: " + d.getCantidad())
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("SIN PRODUCTOS");

            table.addCell(new Paragraph(String.valueOf(v.getId())));
            table.addCell(new Paragraph(v.getFecha() != null ? v.getFecha().format(formatter) : "N/A"));
            table.addCell(new Paragraph("S/ " + v.getTotal()));

            table.addCell(new Paragraph(v.getDni() != null ? v.getDni() : "-"));
            table.addCell(new Paragraph(v.getModalidadEntrega() != null ? v.getModalidadEntrega() : "-"));

            table.addCell(new Paragraph(v.getDireccionEntrega() != null ? v.getDireccionEntrega() : "-"));
            table.addCell(new Paragraph(v.getReferencia() != null ? v.getReferencia() : "-"));
            table.addCell(new Paragraph(v.getNumero() != null ? v.getNumero() : "-"));

            table.addCell(new Paragraph(productos));

            table.addCell(new Paragraph(
                v.getEstado() != null ? v.getEstado().toString() : "SIN ESTADO"
            ));
        }

        document.add(table);
        document.close();
    }
}
