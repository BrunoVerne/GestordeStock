package Spring.FX.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Getter
    @Column(nullable = false)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Getter
    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<VentaProducto> productos = new ArrayList<>();

    public Venta() {}

    public Venta(Usuario usuario) {
        this.usuario = usuario;
    }

    // ✅ NUEVO: snapshot histórico
    public void agregarProducto(
            Integer productoId,
            String nombreProducto,
            Float precioUnitario,
            int cantidad
    ) {
        VentaProducto vp = new VentaProducto(
                this,
                nombreProducto,
                precioUnitario,
                cantidad
        );
        this.productos.add(vp);
    }

    // ✅ ya no depende de Producto
    public Double getMontoTotal() {
        return productos.stream()
                .mapToDouble(vp -> vp.getPrecioUnitario() * vp.getCantidad())
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

    public Usuario getUsuario() {
        return usuario;
    }

}
