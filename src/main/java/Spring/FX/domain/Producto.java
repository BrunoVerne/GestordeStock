package Spring.FX.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private float precio;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String nombre;
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

    public void setId(Integer codigo) {
        this.id = codigo;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getId() {  // Cambiado de long a Integer
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public float getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }


}
