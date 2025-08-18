package Spring.FX.controllers;

import Spring.FX.domain.Usuario;
import Spring.FX.services.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private TextField nombreField;

    private final UsuarioService usuarioService;

    public RegisterController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @FXML
    private void handleRegister() {
        try {
            // Validaciones básicas
            if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
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
            errorLabel.setText("Registro exitoso");
            errorLabel.setStyle("-fx-text-fill: green;");

            // Limpiar campos después de registro exitoso
            emailField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
