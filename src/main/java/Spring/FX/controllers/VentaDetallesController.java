package Spring.FX.controllers;
import Spring.FX.domain.Venta;
import Spring.FX.domain.VentaProducto;
import Spring.FX.services.ProductoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.time.format.DateTimeFormatter;

@Controller
public class VentaDetallesController {

    @FXML private Label lblId;
    @FXML private Label lblFecha;
    @FXML private Label lblUsuario;
    @FXML private Label lblTotal;
    @FXML private Label lblTotalProductos;

    @FXML private TableView<VentaProducto> tableProductos;
    @FXML private TableColumn<VentaProducto, String> colProducto;
    @FXML private TableColumn<VentaProducto, Integer> colCantidad;
    @FXML private TableColumn<VentaProducto, Double> colPrecioUnitario;
    @FXML private TableColumn<VentaProducto, Double> colSubtotal;

    @Setter
    private Stage dialogStage;
    private Venta venta;

    public void setVenta(Venta venta) {
        this.venta = venta;
        cargarDatosVenta();
    }

    private void cargarDatosVenta() {
        if (venta != null) {
            lblId.setText("Venta #" + venta.getId());
            lblFecha.setText(
                    venta.getFechaVenta()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            lblUsuario.setText(venta.getNombreUsuario());
            lblTotal.setText(String.format("$%.2f", venta.getMontoTotal()));
            lblTotalProductos.setText(
                    venta.getCantidadProductos().toString()
            );

            configurarTablaProductos();
            tableProductos.getItems().setAll(venta.getProductos());
        }
    }

    private void configurarTablaProductos() {

        colProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNombreProducto()
                )
        );

        colCantidad.setCellValueFactory(
                new PropertyValueFactory<>("cantidad")
        );

        colPrecioUnitario.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getPrecioUnitario()
                ).asObject()
        );

        colSubtotal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getPrecioUnitario()
                                * cellData.getValue().getCantidad()
                ).asObject()
        );
    }

    @FXML
    private void handleCerrar() {
        dialogStage.close();
    }
}
