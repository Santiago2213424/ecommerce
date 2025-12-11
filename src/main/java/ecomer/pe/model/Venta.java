package ecomer.pe.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Double total;

    private String modalidadEntrega;
    private String direccionEntrega;
    private String referencia;

    private String usuario;
    private String metodoPago;

    private String dni;

    // 🔥 AHORA ESTE ES EL NÚMERO GENERAL DEL CLIENTE
    private String numero;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VentaDetalle> detalles;

    public Venta() {}

    public Venta(Long id, LocalDateTime fecha, Double total, String modalidadEntrega,
                 String direccionEntrega, String referencia, String usuario,
                 String metodoPago, String dni, String numero, EstadoVenta estado,
                 List<VentaDetalle> detalles) {

        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.modalidadEntrega = modalidadEntrega;
        this.direccionEntrega = direccionEntrega;
        this.referencia = referencia;
        this.usuario = usuario;
        this.metodoPago = metodoPago;
        this.dni = dni;
        this.numero = numero;
        this.estado = estado;
        this.detalles = detalles;
    }

    // GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getModalidadEntrega() { return modalidadEntrega; }
    public void setModalidadEntrega(String modalidadEntrega) { this.modalidadEntrega = modalidadEntrega; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    // 🔥 NUEVO CAMPO
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public EstadoVenta getEstado() { return estado; }
    public void setEstado(EstadoVenta estado) { this.estado = estado; }

    public List<VentaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalle> detalles) { this.detalles = detalles; }
}
