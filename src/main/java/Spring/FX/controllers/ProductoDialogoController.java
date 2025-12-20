package Spring.FX.controllers;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Controller
public class ProductoDialogoController {

    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField cantidadField;

    private Producto producto;
    /**
     * -- SETTER --
     * Asignar el usuario que creó/edita el producto
     */
    @Setter
    private Usuario usuario;

    public void setProducto(Producto producto) {
        this.producto = producto;

        if (producto != null) {
            nombreField.setText(
                    producto.getNombre() != null ? producto.getNombre() : ""
            );

            precioField.setText(
                    producto.getPrecio() != null
                            ? producto.getPrecio().toString()
                            : "0"
            );

            cantidadField.setText(
                    String.valueOf(producto.getCantidad())
            );
        } else {
            // Caso NUEVO producto
            nombreField.setText("");
            precioField.setText("0");
            cantidadField.setText("0");
        }
    }

    /**
     * Construye el objeto Producto desde los campos del formulario.
     * No guarda en la base de datos, solo devuelve el objeto listo.
     */
    public Producto getProductoFromFields() {
        if (producto == null) {
            producto = new Producto();
        }

        String nombre = nombreField.getText().trim();
        String precioStr = precioField.getText().trim();
        String cantidadStr = cantidadField.getText().trim();


        producto.setNombre(nombre);

        try {
            float precio = Float.parseFloat(precioStr);
            producto.setPrecio(precio);
        } catch (NumberFormatException e) {
            producto.setPrecio(0.0f);
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            producto.setCantidad(cantidad);
        } catch (NumberFormatException e) {
            producto.setCantidad(0);
        }

        producto.setUsuario(usuario);
        System.out.println("DEBUG: Usuario asignado - ID: " + (usuario != null ? usuario.getId() : "null"));

        return this.producto;
    }

    public boolean validarCampos() {
        String nombre = nombreField.getText().trim();
        String precio = precioField.getText().trim();
        String cantidad = cantidadField.getText().trim();

        StringBuilder errores = new StringBuilder();

        if (nombre.isEmpty()) errores.append("El nombre es obligatorio.\n");
        try {
            Float.parseFloat(precio);
        } catch (NumberFormatException e) {
            errores.append("Precio inválido.\n");
        }
        try {
            Integer.parseInt(cantidad);
        } catch (NumberFormatException e) {
            errores.append("Cantidad inválida.\n");
        }

        if (!errores.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Corrige los siguientes errores:");
            alert.setContentText(errores.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public boolean isNuevoProducto() {
        return producto == null || producto.getId() == null;
    }
}
