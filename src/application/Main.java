package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		
			// Instancia um objeto loader para manipular a tela
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
		// Guarda a tela no obj parent 
			Parent parent = loader.load();

			Scene scene = new Scene(parent);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Sample JavaFX Aplication");
			
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
