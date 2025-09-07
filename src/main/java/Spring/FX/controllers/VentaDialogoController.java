package Spring.FX.controllers;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import Spring.FX.domain.Venta;
import Spring.FX.services.ProductoService;
import Spring.FX.services.VentaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VentaDialogoController {

    @FXML
    private ComboBox<Producto> productoComboBox;
    @FXML
    private ListView<Producto> productosListView;
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

    private final ObservableList<Producto> productosSeleccionados = FXCollections.observableArrayList();
    private final Map<Producto, Integer> cantidadesSeleccionadas = new HashMap<>();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
    private void initialize() {
        configurarListView();
        cargarProductos();
        configurarCantidadSpinner();
    }

    private void configurarListView() {
        productosListView.setItems(productosSeleccionados);
        productosListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int cantidad = cantidadesSeleccionadas.getOrDefault(item, 1);
                    setText(item.getNombre() + " x" + cantidad + " - $" + (item.getPrecio() * cantidad));
                }
            }
        });
    }

    private void configurarCantidadSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        cantidadSpinner.setValueFactory(valueFactory);
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoService.getAllProductos();
            productoComboBox.setItems(FXCollections.observableArrayList(productos));
        } catch (Exception e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }

    @FXML
    private void agregarProducto() {
        Producto producto = productoComboBox.getSelectionModel().getSelectedItem();
        if (producto != null) {
            int cantidad = cantidadSpinner.getValue();
            cantidadesSeleccionadas.merge(producto, cantidad, Integer::sum);
            if (!productosSeleccionados.contains(producto)) {
                productosSeleccionados.add(producto);
            }
            productosListView.refresh();
            actualizarTotal();
        }
    }

    @FXML
    private void removerProducto() {
        Producto producto = productosListView.getSelectionModel().getSelectedItem();
        if (producto != null) {
            productosSeleccionados.remove(producto);
            cantidadesSeleccionadas.remove(producto);
            actualizarTotal();
        }
    }

    private void actualizarTotal() {
        double total = cantidadesSeleccionadas.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrecio() * e.getValue())
                .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    private void crearVenta() {
        try {
            if (productosSeleccionados.isEmpty()) {
                mostrarError("Debe seleccionar al menos un producto");
                return;
            }

            Venta venta = new Venta(usuario);

            for (Producto producto : productosSeleccionados) {
                int cantidadVendida = cantidadesSeleccionadas.get(producto);

                // Agregar producto a la venta
                venta.agregarProducto(producto, cantidadVendida);

                // Actualizar stock
                int nuevaCantidad = producto.getCantidad() - cantidadVendida;
                if (nuevaCantidad < 0) {
                    throw new RuntimeException("Cantidad insuficiente para: " + producto.getNombre());
                }
                producto.setCantidad(nuevaCantidad);
                productoService.actualizarProducto(producto.getId(), producto);
            }

            ventaService.createVenta(venta);

            // Limpiar
            productosSeleccionados.clear();
            cantidadesSeleccionadas.clear();
            dialogStage.close();

        } catch (Exception e) {
            mostrarError("Error al crear venta: " + e.getMessage());
        }
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
