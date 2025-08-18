package Spring.FX.controllers;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import Spring.FX.services.ProductoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductoController {
    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private Label mensajeLabel;

    @Autowired
    private ProductoService productoService;

    private Usuario usuario;
    private Integer usuarioId;

    @FXML
    public void initialize() {
        configurarColumnas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Formatear precio como moneda
        colPrecio.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", precio));
                }
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario.getCodigo();
        cargarProductos();
    }

    public void cargarProductosPorUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            if (usuario == null) {
                throw new IllegalStateException("Usuario no está autenticado");
            }

            // El servicio devuelve un Optional<List<Producto>>
            Optional<List<Producto>> productosOpt = productoService.findByUsuarioId(usuario.getCodigo());

            if (productosOpt.isPresent()) {
                List<Producto> productos = productosOpt.get();
                productosTable.setItems(FXCollections.observableArrayList(productos));
                mensajeLabel.setText("Cargados " + productos.size() + " productos");
            } else {
                productosTable.setItems(FXCollections.observableArrayList()); // lista vacía
                mensajeLabel.setText("No se encontraron productos para este usuario");
            }

        } catch (Exception e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void nuevoProducto() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setUsuario(usuario); // Establece el usuario asociado
        mostrarDialogoProducto(nuevoProducto);
    }

    @FXML
    private void editarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarDialogoProducto(seleccionado);
        } else {
            mostrarError("Seleccione un producto para editar");
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            if (confirmarEliminacion()) {
                try {
                    productoService.borrarProducto(seleccionado.getCodigo());
                    cargarProductos();
                    mostrarMensaje("Producto eliminado correctamente");
                } catch (Exception e) {
                    mostrarError("Error al eliminar producto: " + e.getMessage());
                }
            }
        } else {
            mostrarError("Seleccione un producto para eliminar");
        }
    }

    private void mostrarDialogoProducto(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductoDialogo.fxml"));
            DialogPane dialogPane = loader.load();

            ProductoDialogoController controller = loader.getController();
            controller.setProducto(producto);
            controller.setUsuario(usuario);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(producto.getCodigo() == null ? "Nuevo Producto" : "Editar Producto");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Producto productoGuardado = controller.getProducto();
                if (producto.getCodigo() == null) {
                    productoService.createProducto(productoGuardado);
                    mostrarMensaje("Producto creado correctamente");
                } else {
                    productoService.actualizarProducto(productoGuardado.getCodigo(), productoGuardado);
                    mostrarMensaje("Producto actualizado correctamente");
                }
                cargarProductos();
            }
        } catch (Exception e) {
            mostrarError("Error al abrir diálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean confirmarEliminacion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este producto?");
        alert.setContentText("Esta acción no se puede deshacer");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void mostrarMensaje(String mensaje) {
        mensajeLabel.setStyle("-fx-text-fill: green;");
        mensajeLabel.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        mensajeLabel.setStyle("-fx-text-fill: red;");
        mensajeLabel.setText(mensaje);
    }
}