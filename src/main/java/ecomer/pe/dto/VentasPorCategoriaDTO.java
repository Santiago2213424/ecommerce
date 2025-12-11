package ecomer.pe.dto;

public class VentasPorCategoriaDTO {

    private String categoria;
    private double total;

    public VentasPorCategoriaDTO(String categoria, double total) {
        this.categoria = categoria;
        this.total = total;
    }

    public String getCategoria() { return categoria; }
    public double getTotal() { return total; }
}
