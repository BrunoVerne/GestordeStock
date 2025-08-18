package Spring.FX.domain;

import Spring.FX.validaciones.EmailValidator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String nombre;
    @Column(unique =true, nullable = false)
    private String mail;
    @Column(nullable = false)
    private String contrasenia;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

    public int getCodigo() {
        return id;
    }

    public Usuario() {
    }

    public Usuario(int id, String nombre, String mail, String contrasenia) {
        this.id = id;
        setContrasenia(contrasenia);
        setNombre(nombre);
        setMail(mail);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        EmailValidator.validarEmail(mail);
        this.mail = mail;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
