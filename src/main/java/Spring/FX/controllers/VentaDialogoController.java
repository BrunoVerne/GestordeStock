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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    private Usuario usuario;
    private Stage dialogStage;
    private final ObservableList<Producto> productosSeleccionados = FXCollections.observableArrayList();

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        productosSeleccionados.clear();
        productoComboBox.getSelectionModel().clearSelection();
        totalLabel.setText("Total: $0.00");

        // Opcional: agregar listener para limpiar cuando se cierra la ventana
        dialogStage.setOnShown(event -> {
            productosSeleccionados.clear();
            actualizarTotal();
        });
    }

    @FXML
    private void initialize() {
        configurarControles();
        cargarProductos();
    }

    private void configurarControles() {
        productosListView.setItems(productosSeleccionados);

        productosSeleccionados.addListener((javafx.collections.ListChangeListener.Change<? extends Producto> change) -> {
            actualizarTotal();
        });
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
            productosSeleccionados.add(producto);
        }
    }

    @FXML
    private void removerProducto() {
        Producto producto = productosListView.getSelectionModel().getSelectedItem();
        if (producto != null) {
            productosSeleccionados.remove(producto);
        }
    }

    private void actualizarTotal() {
        double total = productosSeleccionados.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    private void crearVenta() {
        try {
            Venta venta = new Venta(usuario, new ArrayList<>(productosSeleccionados));
            ventaService.createVenta(venta);
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