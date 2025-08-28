package Spring.FX.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Ventas")
public class Venta {
    @Id
    @Column(nullable = false)
    private Integer id;
    private final Usuario  usuario;
    @Column(nullable = false)
    private static final LocalDateTime datetime = LocalDateTime.now();
    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private List<Producto> productos;

    public Venta(Usuario usuario) {
        this.usuario = usuario;
    }

    public Venta(Usuario usuario, List<Producto> productos) {
        this.usuario = usuario;
        this.productos = productos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
