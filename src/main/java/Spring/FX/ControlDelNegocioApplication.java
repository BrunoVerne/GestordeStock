package Spring.FX;

import javafx.fxml.FXMLLoader;
import org.springframework.boot.SpringApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControlDelNegocioApplication extends Application {

	public static void main(String[] args) {
		System.out.print("springboot inicado");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		var context = SpringApplication.run(ControlDelNegocioApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControlDelNegocioApplication.fxml"));
		stage.show();
	}


}
