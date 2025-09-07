package Spring.FX.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VentaProducto> productos = new ArrayList<>();

    public Venta() {}

    public Venta(Usuario usuario) {
        this.usuario = usuario;
    }

    // 🔹 Método helper para agregar productos con cantidad
    public void agregarProducto(Producto producto, int cantidad) {
        VentaProducto vp = new VentaProducto(this, producto, cantidad);
        this.productos.add(vp);
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public List<VentaProducto> getProductos() { return productos; }
    public void setProductos(List<VentaProducto> productos) { this.productos = productos; }

    // 🔹 Métodos calculados
    public Double getMontoTotal() {
        return productos.stream()
                .mapToDouble(vp -> vp.getProducto().getPrecio() * vp.getCantidad())
                .sum();
    }

    public Integer getCantidadProductos() {
        return productos.stream()
                .mapToInt(VentaProducto::getCantidad)
                .sum();
    }

    public String getNombreUsuario() {
        return usuario != null ? usuario.getNombre() : "N/A";
    }
}
