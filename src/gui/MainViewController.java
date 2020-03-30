package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerActive() {

		System.out.println("onMenuItemSellerActive");
	}

	@FXML
	public void onMenuItemDepartmentActive() {

		loadView("/gui/DepartmentList.fxml");
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	// Syncronied garante que nao sera interrompido e gerar um comportamento
	// inesperado
	// visto que as telas graficas sao mult-thred.

	public synchronized void loadView(String absoluteName) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			VBox newVbox = loader.load();

			// pega a sena principal da classe Main

			Scene mainScene = Main.mainScene();
			// Pega o primeiro elemento da janela principal .
			// ((ScrollPane) mainScene.getRoot()) faz o castepara ScrollPane primeiro
			// elemento da pagina scena. .getContent(); pega a area conteudo

			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // Converte todo conteudo para VBox.

			// Cria um objeto Nodo (Elemento) e pega o primeiro filho da posicao Zero.
			Node mainMenu = mainVbox.getChildren().get(0);

			mainVbox.getChildren().clear(); // Apaga os filhos
			mainVbox.getChildren().add(mainMenu); // Adiciona o menu Preservado
			mainVbox.getChildren().addAll(newVbox.getChildren()); // Adiciona os novos fihos do VBox About

		} catch (IOException e) {

			Alerts.showAlert("IOException", "Error load View", e.getMessage(), AlertType.ERROR);

		}

	}

}
