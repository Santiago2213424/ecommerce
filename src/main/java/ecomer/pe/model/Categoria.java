package ecomer.pe.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Producto> products;

    public Categoria() {}

    public Categoria(String nombre){
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
