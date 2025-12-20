package Spring.FX.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Table(name = "productos")
public class Producto {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private Float precio;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String nombre;
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    public Producto() {
    }

    public Producto(Usuario usuario, float precio, int cantidad, String nombre) {
        this.usuario = usuario;
        setPrecio(precio);
        setCantidad(cantidad);
        setNombre(nombre);
    }




    public void setPrecio(float precio){
        if(precio <= 0){
            throw new RuntimeException("El precio debe ser positivo");
        }
        this.precio = precio;
    }

    public void setNombre(String nombre){
        if(nombre.isBlank()){
            throw new RuntimeException("El nombre no puede estar vacio");

        }
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad){
        if(cantidad < 0){
            throw new RuntimeException("La cantidad no puede ser menor a 0");

        }
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }


}
