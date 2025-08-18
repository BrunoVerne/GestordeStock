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
    // Componentes de la vista
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    // Dependencias
    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private void handleLogin() {
        try {
            validarCampos();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            // 1. Autenticar usuario
            if (usuarioService.autenticar(email, password)) {
                // 2. Obtener usuario completo
                Usuario usuario = usuarioService.findByMail(email)
                        .orElseThrow(() -> new Exception("Usuario no encontrado"));

                // 3. Cargar vista principal
                cargarVistaProductos(usuario);
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
            limpiarCampoContrasenia();
        }
    }

    private void validarCampos() throws Exception {
        if (emailField.getText().isBlank() || passwordField.getText().isBlank()) {
            throw new Exception("Email y contraseña son requeridos");
        }
    }

    private void cargarVistaProductos(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductoView.fxml"));
            loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            ProductoController productoController = loader.getController();
            productoController.setUsuario(usuario);  // ahora sí se pasa

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Usuario");
        } catch (Exception e) {
            mostrarError("No se pudo cargar la vista de registro");
        }
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void limpiarCampoContrasenia() {
        passwordField.clear();
        passwordField.requestFocus();
    }
}