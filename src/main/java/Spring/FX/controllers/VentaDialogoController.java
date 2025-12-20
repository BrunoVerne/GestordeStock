package Spring.FX.controllers;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import Spring.FX.domain.Venta;
import Spring.FX.domain.VentaProducto;
import Spring.FX.services.ProductoService;
import Spring.FX.services.VentaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VentaDialogoController {

    @FXML
    private ComboBox<Producto> productoComboBox;
    @FXML
    private ListView<VentaProducto> productosListView;
    @FXML
    private Label totalLabel;
    @FXML
    private Button agregarButton;
    @FXML
    private Button removerButton;
    @FXML
    private Button crearVentaButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private Spinner<Integer> cantidadSpinner;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    private Usuario usuario;
    private Stage dialogStage;

    private final ObservableList<VentaProducto> productosSeleccionados =
            FXCollections.observableArrayList();
    private final Map<Producto, Integer> cantidadesSeleccionadas = new HashMap<>();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        // Ahora sí cargar productos porque ya tengo el usuario
        List<Producto> productos = productoService.findByUsuarioId(usuario.getId())
                .orElse(Collections.emptyList());
        productoComboBox.getItems().setAll(productos);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        productosSeleccionados.clear();
        productoComboBox.getSelectionModel().clearSelection();
        totalLabel.setText("Total: $0.00");

        dialogStage.setOnShown(event -> {
            productosSeleccionados.clear();
            cantidadesSeleccionadas.clear();
            actualizarTotal();
        });
    }

    @FXML
    public void initialize() {
        configurarCantidadSpinner();

        productosListView.setItems(productosSeleccionados);

        productosListView.setCellFactory(lv -> new ListCell<VentaProducto>() {
            @Override
            protected void updateItem(VentaProducto item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getNombreProducto()
                                    + "  x" + item.getCantidad()
                                    + "  ($" + String.format("%.2f",
                                    item.getPrecioUnitario() * item.getCantidad()) + ")"
                    );
                }
            }
        });

        productoComboBox.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto producto) {
                if (producto == null) return "";
                return producto.getNombre() + " - $" + producto.getPrecio();
            }

            @Override
            public Producto fromString(String string) {
                return null; // no se usa
            }
        });
    }



    private void configurarCantidadSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        cantidadSpinner.setValueFactory(valueFactory);
    }



    @FXML
    private void agregarProducto() {

        Producto producto = productoComboBox.getValue();
        int cantidad = cantidadSpinner.getValue();

        if (producto == null || cantidad <= 0) return;

        VentaProducto vp = new VentaProducto();
        vp.setProductoId(producto.getId());
        vp.setNombreProducto(producto.getNombre());
        vp.setPrecioUnitario(producto.getPrecio());
        vp.setCantidad(cantidad);

        productosSeleccionados.add(vp);
        actualizarTotal();
    }


    @FXML
    private void removerProducto() {
        VentaProducto seleccionado =
                productosListView.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            productosSeleccionados.remove(seleccionado);
            actualizarTotal();
        }
    }



    private void actualizarTotal() {
        double total = 0;

        for (VentaProducto vp : productosSeleccionados) {
            total += vp.getPrecioUnitario() * vp.getCantidad();
        }

        totalLabel.setText("Total: $" + total);
    }


    @FXML
    private void crearVenta() {

        Venta venta = new Venta(usuario);

        for (VentaProducto vp : productosSeleccionados) {
            venta.getProductos().add(vp);
            vp.setVenta(venta);
        }

        ventaService.createVenta(venta);
        dialogStage.close();
    }

    @FXML
    private void cancelar() {
        dialogStage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
