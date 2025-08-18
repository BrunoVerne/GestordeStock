package Spring.FX.controllers;


import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class ProductoDialogoController {
    @FXML private TextField idField;
    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField cantidadField;

    private Producto producto;
    private Usuario usuario;

    public void setProducto(Producto producto) {
        this.producto = producto;
        nombreField.setText(producto.getNombre());
        precioField.setText(String.valueOf(producto.getPrecio()));
        cantidadField.setText(String.valueOf(producto.getCantidad()));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        producto.setNombre(nombreField.getText());
        producto.setPrecio(Float.parseFloat(precioField.getText()));
        producto.setCantidad(Integer.parseInt(cantidadField.getText()));
        return producto;
    }
}