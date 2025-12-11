package ecomer.pe.dto;

public class ProductosMasVendidosDTO {

    private String producto;
    private long cantidad;

    public ProductosMasVendidosDTO(String producto, long cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getProducto() { return producto; }
    public long getCantidad() { return cantidad; }
}
