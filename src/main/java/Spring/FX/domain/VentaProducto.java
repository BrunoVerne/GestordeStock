package Spring.FX.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "venta_producto")
public class VentaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    public VentaProducto() {}

    public VentaProducto(Venta venta, Producto producto, Integer cantidad) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Venta getVenta() { return venta; }

    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }

    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
