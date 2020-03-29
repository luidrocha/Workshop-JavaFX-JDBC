package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	// Atributo para pegar a scena principal. 
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		
		
		
		try {

			// Instancia um objeto loader para manipular a tela

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			// Guarda a tela no obj parent

			// Parent parent = loader.load(); conteinar substituido 
			ScrollPane scrollPane = loader.load();

			// Ajusta largura e altura
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			mainScene = new Scene(scrollPane);

			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX Aplication");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	//Retorna a scena Principal
	public static Scene mainScene() {
		
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
