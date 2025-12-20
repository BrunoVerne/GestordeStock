package Spring.FX.controllers;

import Spring.FX.ControlDelNegocioApplication;
import Spring.FX.domain.Usuario;
import Spring.FX.services.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private void handleLogin() {
        try {
            Usuario usuario = usuarioService.autenticar(
                    emailField.getText().toLowerCase().trim(),
                    passwordField.getText().trim()
            );

            cargarVistaVentas(usuario);

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            passwordField.clear();
        }
    }

    private void cargarVistaVentas(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/VentasView.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();

            VentasController ventasController = loader.getController();
            ventasController.setUsuario(usuario);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista de ventas", e);
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/RegisterView.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista de registro", e);
        }
    }
}
