package Spring.FX.domain;

import Spring.FX.validaciones.EmailValidator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Setter
    @Column(nullable = false)
    private String nombre;
    @Column(unique =true, nullable = false)
    private String mail;
    @Setter
    @Column(nullable = false)
    private String contrasenia;
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas = new ArrayList<>();


    public Usuario() {
    }

    public Usuario(int id, String nombre, String mail, String contrasenia) {
        this.id = id;
        setContrasenia(contrasenia);
        setNombre(nombre);
        setMail(mail);
    }

    public void setMail(String mail) {
        EmailValidator.validarEmail(mail);
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

}
