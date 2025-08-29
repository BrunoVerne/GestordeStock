package Spring.FX.controllers;

import Spring.FX.ControlDelNegocioApplication;
import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import Spring.FX.services.ProductoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductoController {
    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Float> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private Label mensajeLabel;

    @Autowired
    private ProductoService productoService;

    private Usuario usuario;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            Optional<List<Producto>> productosOpt = productoService.findByUsuarioId(usuario.getId());
            if (productosOpt.isPresent()) {
                productosTable.setItems(FXCollections.observableArrayList(productosOpt.get()));
                mensajeLabel.setText("Productos cargados: " + productosOpt.get().size());
            } else {
                productosTable.setItems(FXCollections.observableArrayList());
                mensajeLabel.setText("No hay productos");
            }
        } catch (Exception e) {
            mensajeLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void nuevoProducto() {
        try {
            mostrarDialogoProducto(new Producto());
        } catch (Exception e) {
            mensajeLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void editarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                mostrarDialogoProducto(seleccionado);
            } catch (Exception e) {
                mensajeLabel.setText("Error: " + e.getMessage());
            }
        } else {
            mensajeLabel.setText("Seleccione un producto");
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                productoService.borrarProducto(seleccionado.getCodigo());
                cargarProductos();
                mensajeLabel.setText("Producto eliminado");
            } catch (Exception e) {
                mensajeLabel.setText("Error: " + e.getMessage());
            }
        } else {
            mensajeLabel.setText("Seleccione un producto");
        }
    }

    private void mostrarDialogoProducto(Producto producto) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductoDialogo.fxml"));
        loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
        DialogPane dialogPane = loader.load();

        ProductoDialogoController controller = loader.getController();
        controller.setProducto(producto);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(producto.getCodigo() == null ? "Nuevo Producto" : "Editar Producto");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Producto productoActualizado = controller.getProductoFromFields();
            productoActualizado.setUsuario(usuario);

            if (producto.getCodigo() == null) {
                productoService.createProducto(productoActualizado);
                mensajeLabel.setText("Producto creado");
            } else {
                productoService.actualizarProducto(producto.getCodigo(), productoActualizado);
                mensajeLabel.setText("Producto actualizado");
            }
            cargarProductos();
        }
    }

    // Método estático para abrir desde SalesController
    public static void mostrarVentanaProductos(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(ProductoController.class.getResource("/ProductoView.fxml"));
            loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            ProductoController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Stock");
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Error al abrir productos: " + e.getMessage());
        }
    }
}