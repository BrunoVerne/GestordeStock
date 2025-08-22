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
public class RegisterController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private void handleRegister() {
        try {
            // Validaciones completas
            if (nombreField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
                throw new Exception("Todos los campos son obligatorios");
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                throw new Exception("Las contraseñas no coinciden");
            }

            // Crear usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombreField.getText());
            nuevoUsuario.setMail(emailField.getText());
            nuevoUsuario.setContrasenia(passwordField.getText());

            usuarioService.createUsuario(nuevoUsuario);

            // Mensaje de éxito
            errorLabel.setText("Registro exitoso. Redirigiendo al login...");
            errorLabel.setStyle("-fx-text-fill: green;");

            // Volver al login después de 2 segundos
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            volverALogin();
                        }
                    },
                    2000
            );

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleVolverALogin() {
        volverALogin();
    }

    private void volverALogin() {
        try {
            javafx.application.Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                    loader.setControllerFactory(ControlDelNegocioApplication.getSpringContext()::getBean);
                    Parent root = loader.load();

                    Stage stage = (Stage) nombreField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Inicio de Sesión");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}