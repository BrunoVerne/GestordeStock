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
import javafx.stage.Modality;
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


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarProductos();
    }


    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
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
                productoService.borrarProducto(seleccionado.getId());
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
        controller.setUsuario(usuario);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(producto.getId() == null ? "Nuevo Producto" : "Editar Producto");

        // ✅ CONVERTIR EL DIÁLOGO A MODAL PARA QUE showAndWait() FUNCIONE MEJOR
        dialog.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            try {
                Producto productoActualizado = controller.getProductoFromFields();
                productoActualizado.setUsuario(usuario);

                if (producto.getId() == null) {
                    productoService.createProducto(productoActualizado);
                    mensajeLabel.setText("Producto creado");
                } else {
                    productoService.actualizarProducto(producto.getId(), productoActualizado);
                    mensajeLabel.setText("Producto actualizado");
                }
                cargarProductos();
            } catch (Exception e) {
                mensajeLabel.setText("Error: " + e.getMessage());
                mostrarDialogoProducto(producto);
            }
        }
    }


    @FXML
    private void volverAVentas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/VentasView.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();

            VentasController controller = loader.getController();
            controller.setUsuario(usuario); // 👈 MISMO USUARIO

            Stage stage = (Stage) productosTable.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            throw new RuntimeException("Error al volver a ventas", e);
        }
    }



}