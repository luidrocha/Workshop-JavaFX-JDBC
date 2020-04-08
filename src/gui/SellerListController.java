package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service; // Cria um Dependencia

	private ObservableList<Seller> obsList; // Usada para carregar os departamentos e sera associada a TableView

	@FXML
	private TableView<Seller> tableViewSeller; // Define um objeto do tipo TableView<Seller>. Apos declarar
												// devese criar um metodo para que funcione.

	@FXML
	private TableColumn<Seller, Integer> tableColumnId; // Define um objeto do tipo TableColumn<Seller, Integer>
														// , onde integer e o tipo do campo
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	

	@FXML
	private Button btNovo;

	@FXML
	public void btNovoAction(ActionEvent event) {

		Seller obj = new Seller();

		Stage parentStage = Utils.curretStage(event);

		//createDialogForm(obj, "/gui/SellerList.fxml", parentStage);
		
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		InicializeNode();

	}

	public void setSellerService(SellerService service) {

		this.service = service;

	}

	// Carrega os Departamentos MOCK

	public void updateTableView() {

		if (service == null) {

			throw new IllegalStateException(" Service esta null ");
		}

		List<Seller> lista = service.findAll();

		obsList = FXCollections.observableArrayList(lista); // Carrega a lista no objeto ObsList
		tableViewSeller.setItems(obsList); // Carrega a lista na TableViewSeller
		initEditButtons(); // Acrescenta a opcao Edit em cada linha da tabela
		initRemoveButtons();
	}

	public void InicializeNode() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padrao do JavaFX para iniciar as colunas
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy"); //Formata a data no grid.
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2); // Formata o salario

		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega a referencia da Janela getWindow() e super classe
																// do Stage por isso o DowCast
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); // Ajusta a Table View de acordo com o
																			// Stage, janela.

	}

	// Cria uma referencia para o stage, janela que criou a janela de dialogo. Quand
	// secria uma janela tem que informar
	// ostage da janela que criou a janela de dialog

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Carrega a view

			SellerFormController controller = loader.getController(); // Pega o controller da tela

			controller.setSeller(obj);

			controller.setSellerService(new SellerService()); // intancia a Dependencia

			controller.subscribeDataChangeListener(this);

			controller.updateFormData();

			Stage dialogStage = new Stage(); // Cria um novo estage para janela nova

			dialogStage.setTitle("Entre com o Dados do Departamento");

			dialogStage.setScene(new Scene(pane)); // Seta uma nova scene que e a view carregada

			dialogStage.setResizable(false); // Faz com que a janela nao possa ser redimenssionada.

			dialogStage.initOwner(parentStage); // Informa o Stage Pai dessa janela

			dialogStage.initModality(Modality.WINDOW_MODAL); // Define a janela como MODAL.
			dialogStage.showAndWait();

		}

		catch (IOException e) {

			Alerts.showAlert("IO Exception", "Error", e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {

		updateTableView();

	}

	// Monta os botoes do grid para Editar as informacaoes

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.curretStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {

		// Guarda a opcao de confirmacao ou canelamento

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Confirma Exclusao");

		if (result.get() == ButtonType.OK) {

			if (service == null) {

				throw new IllegalStateException("Servico nao foi instanciado estado e Null !");

			}

			try {

				service.remove(obj);
				updateTableView();

			}

			catch (DbIntegrityException e) {

				Alerts.showAlert("Erro ao deletar obj", null, e.getMessage(), AlertType.ERROR);
			}

		}

	}
}
