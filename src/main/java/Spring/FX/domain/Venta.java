package Spring.FX.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private  LocalDateTime fechaVenta = LocalDateTime.now();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "venta_productos",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )    private List<Producto> productos;

    public Venta() {
        this.fechaVenta = LocalDateTime.now();
    }

    public Venta(Usuario usuario) {
        this();
        this.usuario = usuario;
    }

    public Venta(Usuario usuario, List<Producto> productos) {
        this();
        this.usuario = usuario;
        this.productos = productos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Double getMontoTotal() {
        if (productos == null) return 0.0;
        return productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();
    }

    public Integer getCantidadProductos() {
        return productos != null ? productos.size() : 0;
    }

    public String getNombreUsuario() {
        return usuario != null ? usuario.getNombre() : "N/A";
    }
}


