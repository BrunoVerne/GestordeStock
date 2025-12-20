package Spring.FX.controllers;

import Spring.FX.ControlDelNegocioApplication;
import Spring.FX.domain.Usuario;
import Spring.FX.services.UsuarioService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
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
            if (nombreField.getText().isEmpty()
                    || emailField.getText().isEmpty()
                    || passwordField.getText().isEmpty()
                    || confirmPasswordField.getText().isEmpty()) {
                throw new Exception("Todos los campos son obligatorios");
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                throw new Exception("Las contraseñas no coinciden");
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombreField.getText());
            nuevoUsuario.setMail(emailField.getText());
            nuevoUsuario.setContrasenia(passwordField.getText());

            usuarioService.createUsuario(nuevoUsuario);

            errorLabel.setText("Registro exitoso. Redirigiendo al login...");
            errorLabel.setStyle("-fx-text-fill: green;");

            // ⏱ Delay correcto en JavaFX
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> volverALogin());
            pause.play();

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }



    @FXML
    private void volverALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/LoginView.fxml")
            );
            loader.setControllerFactory(
                    ControlDelNegocioApplication.getSpringContext()::getBean
            );

            Parent root = loader.load();
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Volver a Inicio");

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista de login", e);
        }
    }




}