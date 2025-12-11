package ecomer.pe.dto;

public class VentasPorMesDTO {
    private int mes;
    private int anio;
    private double total;

    public VentasPorMesDTO(int mes, int anio, double total) {
        this.mes = mes;
        this.anio = anio;
        this.total = total;
    }

    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public double getTotal() { return total; }
}
