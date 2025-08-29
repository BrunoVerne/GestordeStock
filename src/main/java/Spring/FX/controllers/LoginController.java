package Spring.FX.controllers;

import Spring.FX.ControlDelNegocioApplication;
import Spring.FX.domain.Usuario;
import Spring.FX.services.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    // SOLO ESTOS CAMPOS (los que están en LoginView.fxml)
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private void handleLogin() {
        try {
            validarCampos();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (usuarioService.autenticar(email, password)) {
                Usuario usuario = usuarioService.findByMail(email)
                        .orElseThrow(() -> new Exception("Usuario no encontrado"));

                cargarVistaVentas(usuario);  // ← CAMBIADO: Ahora carga ventas en lugar de productos
                cerrarVentanaActual();
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
            limpiarCampoContrasenia();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterView.fxml"));
            loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Usuario");

        } catch (Exception e) {
            mostrarError("No se pudo cargar la vista de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void validarCampos() throws Exception {
        if (emailField.getText().isBlank() || passwordField.getText().isBlank()) {
            throw new Exception("Email y contraseña son requeridos");
        }
    }

    private void cargarVistaVentas(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VentasView.fxml"));
            loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            VentasController ventasController = loader.getController();
            ventasController.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Gestión - Ventas");
            stage.show();

        } catch (Exception e) {
            mostrarError("Error al cargar la vista de ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void limpiarCampoContrasenia() {
        passwordField.clear();
        passwordField.requestFocus();
    }

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }
}