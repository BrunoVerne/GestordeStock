package Spring.FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "Spring.FX")
public class ControlDelNegocioApplication extends Application {

    // Contexto de Spring que se asignará desde el launcher
    private static ConfigurableApplicationContext springContext;

    private Parent root;

    // Setter para asignar el contexto desde el launcher
    public static void setSpringContext(ConfigurableApplicationContext context) {
        springContext = new SpringApplicationBuilder(ControlDelNegocioApplication.class)
                .web(WebApplicationType.NONE) // ⚡ Esto indica que no es web
                .run();
    }

    // Getter para usarlo en los controllers
    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void init() throws Exception {
        if (springContext == null) {
            springContext = new SpringApplicationBuilder(ControlDelNegocioApplication.class)
                    .web(WebApplicationType.NONE) // no arranca web server
                    .run();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        root = loader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Cerrar Spring context al cerrar la app
        if (springContext != null) springContext.close();
    }
}