package Spring.FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ControlDelNegocioApplication extends Application {

    private static ConfigurableApplicationContext springContext;
    private Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(ControlDelNegocioApplication.class);
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
        springContext.close();
    }
}
