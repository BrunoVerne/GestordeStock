package Spring.FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

@SpringBootApplication
public class ControlDelNegocioApplication extends Application {

    @Getter
    private static ConfigurableApplicationContext springContext;
    private static Scene mainScene;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(ControlDelNegocioApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/LoginView.fxml")
        );
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();

        mainScene = new Scene(root);

        mainScene.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/styles/theme.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/styles/forms.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/styles/tables.css")).toExternalForm()
        );

        stage.setScene(mainScene);
        stage.setTitle("Gestion de Ventas y Stock");
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    ControlDelNegocioApplication.class.getResource("/" + fxml)
            );
            loader.setControllerFactory(springContext::getBean);

            Parent root = loader.load();
            mainScene.setRoot(root);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar " + fxml, e);
        }
    }

}
