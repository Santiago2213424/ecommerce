package ecomer.pe.service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecomer.pe.model.Venta;
import ecomer.pe.repository.VentaRepository;

@Service
public class ExcelService {

    @Autowired
    private VentaRepository ventaRepo;

    public void exportarVentas(OutputStream out, String filter) throws IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Ventas");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Venta> ventas = ventaRepo.findAll();

        // 🔎 FILTRO COMPLETO
        if (filter != null && !filter.isBlank()) {
            String f = filter.toLowerCase();

            ventas = ventas.stream()
                    .filter(v ->
                        (v.getFecha() != null && v.getFecha().format(formatter).toLowerCase().contains(f)) ||
                        String.valueOf(v.getId()).contains(f) ||
                        String.valueOf(v.getTotal()).contains(f) ||
                        (v.getDni() != null && v.getDni().toLowerCase().contains(f)) ||
                        (v.getModalidadEntrega() != null && v.getModalidadEntrega().toLowerCase().contains(f)) ||
                        (v.getDireccionEntrega() != null && v.getDireccionEntrega().toLowerCase().contains(f)) ||
                        (v.getReferencia() != null && v.getReferencia().toLowerCase().contains(f)) ||
                        (v.getNumero() != null && v.getNumero().toLowerCase().contains(f)) ||
                        (v.getEstado() != null && v.getEstado().toString().toLowerCase().contains(f))
                    )
                    .toList();
        }

        // HEADER
        int rowIdx = 0;
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Fecha");
        header.createCell(2).setCellValue("Total");
        header.createCell(3).setCellValue("DNI");
        header.createCell(4).setCellValue("Modalidad");
        header.createCell(5).setCellValue("Dirección");
        header.createCell(6).setCellValue("Referencia");
        header.createCell(7).setCellValue("N° Yape");
        header.createCell(8).setCellValue("Productos");
        header.createCell(9).setCellValue("Estado");

        // DATA
        for (Venta v : ventas) {
            Row row = sheet.createRow(rowIdx++);

            String productos = (v.getDetalles() != null)
                    ? v.getDetalles().stream()
                    .map(d -> d.getProducto().getNombre() + " x" + d.getCantidad())
                    .collect(Collectors.joining(", "))
                    : "Sin productos";

            row.createCell(0).setCellValue(v.getId());
            row.createCell(1).setCellValue(v.getFecha() != null ? v.getFecha().format(formatter) : "-");
            row.createCell(2).setCellValue(v.getTotal());

            row.createCell(3).setCellValue(v.getDni() != null ? v.getDni() : "-");
            row.createCell(4).setCellValue(v.getModalidadEntrega() != null ? v.getModalidadEntrega() : "-");
            row.createCell(5).setCellValue(v.getDireccionEntrega() != null ? v.getDireccionEntrega() : "-");
            row.createCell(6).setCellValue(v.getReferencia() != null ? v.getReferencia() : "-");
            row.createCell(7).setCellValue(v.getNumero() != null ? v.getNumero() : "-");

            row.createCell(8).setCellValue(productos);
            row.createCell(9).setCellValue(v.getEstado() != null ? v.getEstado().toString() : "-");
        }

        wb.write(out);
        wb.close();
    }
}
