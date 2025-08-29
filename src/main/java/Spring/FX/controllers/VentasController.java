
    package Spring.FX.controllers;

import Spring.FX.domain.Usuario;
import Spring.FX.domain.Venta;
import Spring.FX.services.VentaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

    @Controller
    public class VentasController {

        @FXML
        private TableView<Venta> ventasTable;
        @FXML private TableColumn<Venta, String> colFecha;
        @FXML private TableColumn<Venta, Double> colMonto;
        @FXML private TableColumn<Venta, Integer> colCantidadProductos;
        @FXML private TableColumn<Venta, String> colUsuario;
        @FXML private Label mensajeLabel;

        @Autowired
        private VentaService ventaService;

        private Usuario usuario;

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
            cargarVentas();
        }

        @FXML
        private void initialize() {
            configurarColumnas();
        }

        private void configurarColumnas() {
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaVenta"));
            colMonto.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
            colCantidadProductos.setCellValueFactory(new PropertyValueFactory<>("cantidadProductos"));
            colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        }

        private void cargarVentas() {
            try {
                List<Venta> ventas = ventaService.getAllVentas();
                ventasTable.getItems().setAll(ventas);
                mensajeLabel.setText("Ventas cargadas: " + ventas.size());
            } catch (Exception e) {
                mensajeLabel.setText("Error: " + e.getMessage());
            }
        }

        @FXML
        private void nuevaVenta() {
            mensajeLabel.setText("Nueva venta - Funcionalidad en desarrollo");
        }

        @FXML
        private void gestionarStock() {
            try {
                // Abrir ventana de productos
                ProductoController.mostrarVentanaProductos(usuario);
            } catch (Exception e) {
                mensajeLabel.setText("Error al abrir productos: " + e.getMessage());
            }
        }
    }

