package Spring.FX.controllers;

import Spring.FX.ControlDelNegocioApplication;
import Spring.FX.domain.Usuario;
import Spring.FX.domain.Venta;
import Spring.FX.services.VentaService;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class VentasController {

    @FXML
    private TableView<Venta> ventasTable;

    @FXML
    private TableColumn<Venta, String> colFecha;

    @FXML
    private TableColumn<Venta, Double> colMonto;

    @FXML
    private TableColumn<Venta, Integer> colCantidadProductos;

    @FXML
    private Label mensajeLabel;

    @Autowired
    private VentaService ventaService;

    private Usuario usuario;

    // =========================
    // Inicialización de la tabla
    // =========================
    @FXML
    public void initialize() {
        // Se ejecuta SOLO UNA VEZ cuando se carga el FXML

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        colFecha.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getFechaVenta().format(f)
                )
        );
        colMonto.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getMontoTotal())
        );

        colCantidadProductos.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getProductos().size())
        );

        ventasTable.setPlaceholder(
                new Label("No hay ventas registradas.")
        );


        ventasTable.setRowFactory(tv -> {
            TableRow<Venta> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()
                        && event.getClickCount() == 2
                        && event.getButton() == MouseButton.PRIMARY) {

                    abrirDetalleVenta();
                }
            });

            return row;
        });
    }


    // =========================
    // Usuario autenticado
    // =========================
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.printf("usuario en ventas: " + usuario);
        cargarVentas();
    }

    // =========================
    // Cargar ventas
    // =========================
    private void cargarVentas() {

        if (usuario == null) {
            mensajeLabel.setText("Usuario no inicializado.");
            return;
        }

        try {
            Optional<List<Venta>> ventasOpt =
                    ventaService.findByUsuarioId(usuario.getId());

            List<Venta> ventas = ventasOpt.orElse(Collections.emptyList());

            ventasTable.getItems().setAll(ventas);

            mensajeLabel.setText(
                    ventas.isEmpty()
                            ? "No hay ventas registradas."
                            : "Ventas cargadas: " + ventas.size()
            );

        } catch (Exception e) {
            mensajeLabel.setText("Error al cargar ventas.");
            e.printStackTrace();
        }
    }

    // =========================
    // Nueva venta
    // =========================
    @FXML
    private void nuevaVenta() {

        if (usuario == null) {
            throw new IllegalStateException("Usuario no inicializado al crear venta");
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/VentaDialogo.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();

            VentaDialogoController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initOwner(ventasTable.getScene().getWindow());

            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            cargarVentas();

        } catch (Exception e) {
            throw new RuntimeException("Error al abrir diálogo de nueva venta", e);
        }
    }

    // =========================
    // Gestionar stock
    // =========================
    @FXML
    private void gestionarStock() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ProductoView.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();

            ProductoController controller = loader.getController();
            controller.setUsuario(usuario);
            // usamos la MISMA scene
            Stage stage = (Stage) ventasTable.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            throw new RuntimeException("Error al abrir productos", e);
        }
    }





    @FXML
    private void eliminarVenta() {
        Venta seleccionado = ventasTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                ventaService.borrarVenta(seleccionado.getId());
                cargarVentas();
                mensajeLabel.setText("Venta eliminada");
            } catch (Exception e) {
                mensajeLabel.setText("Error: " + e.getMessage());
            }
        } else {
            mensajeLabel.setText("Seleccione una venta");
        }
    }





    @FXML
    private void abrirDetalleVenta() {

        Venta seleccionada = ventasTable
                .getSelectionModel()
                .getSelectedItem();

        if (seleccionada == null) {
            mensajeLabel.setText("Seleccione una venta para ver el detalle.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/VentaDetalles.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();

            VentaDetallesController controller = loader.getController();
            controller.setVenta(seleccionada);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initOwner(ventasTable.getScene().getWindow());

            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al abrir detalle de venta", e);
        }
    }




}

