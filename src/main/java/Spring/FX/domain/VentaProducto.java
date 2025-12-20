package Spring.FX.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "venta_producto")
public class VentaProducto {

    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;


    @Column(nullable = false)
    String nombreProducto;

    @Column(nullable = false)
    Float precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    public VentaProducto() {}


    public VentaProducto(Venta venta, String nombreProducto, Float precioUnitario, Integer cantidad) {
        this.venta = venta;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public void setProductoId(Integer id) {
        
    }


}
