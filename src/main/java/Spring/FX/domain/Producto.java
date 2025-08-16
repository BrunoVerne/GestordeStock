package Spring.FX.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "productos")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long codigo;
    @Column(nullable = false)
    private float precio;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String nombre;



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
        if(this.cantidad < 0){
            throw new RuntimeException("La cantidad no puede ser menor a 0");

        }
        this.cantidad = cantidad;
    }


}
