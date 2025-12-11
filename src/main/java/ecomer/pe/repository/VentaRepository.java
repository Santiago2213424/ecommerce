package ecomer.pe.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecomer.pe.dto.ProductosMasVendidosDTO;
import ecomer.pe.dto.VentasPorCategoriaDTO;
import ecomer.pe.dto.VentasPorMesDTO;
import ecomer.pe.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Ventas del día
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    List<Venta> findVentasDelDia(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> findByUsuario(String usuario);


    // ============================
    // 1️⃣ VENTAS POR MES
    // ============================
    @Query("""
        SELECT new ecomer.pe.dto.VentasPorMesDTO(
            MONTH(v.fecha),
            YEAR(v.fecha),
            SUM(v.total)
        )
        FROM Venta v
        GROUP BY YEAR(v.fecha), MONTH(v.fecha)
        ORDER BY YEAR(v.fecha), MONTH(v.fecha)
    """)
    List<VentasPorMesDTO> obtenerVentasPorMes();


    // ============================
    // 2️⃣ VENTAS POR CATEGORÍA
    // ============================
    @Query("""
        SELECT new ecomer.pe.dto.VentasPorCategoriaDTO(
            p.categoria.nombre,
            SUM(d.subtotal)
        )
        FROM VentaDetalle d
        JOIN d.producto p
        GROUP BY p.categoria.nombre
    """)
    List<VentasPorCategoriaDTO> obtenerVentasPorCategoria();


    // ============================
    // 3️⃣ PRODUCTOS MÁS VENDIDOS
    // ============================
    @Query("""
        SELECT new ecomer.pe.dto.ProductosMasVendidosDTO(
            p.nombre,
            SUM(d.cantidad)
        )
        FROM VentaDetalle d
        JOIN d.producto p
        GROUP BY p.nombre
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<ProductosMasVendidosDTO> obtenerProductosMasVendidos();
}
