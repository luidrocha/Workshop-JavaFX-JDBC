package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartmentService service; // Cria um Dependencia

	private ObservableList<Department> obsList; // Usada para carregar os departamentos e sera associada a TableView

	@FXML
	private TableView<Department> tableViewDepartment; // Define um objeto do tipo TableView<Department>. Apos declarar
														// devese criar um metodo para que funcione.

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Define um objeto do tipo TableColumn<Department, Integer>
															// , onde integer e o tipo do campo

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNovo;

	@FXML
	public void btNovoAction(ActionEvent event) {

		Department obj = new Department();

		Stage parentStage = Utils.curretStage(event);

		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		InicializeNode();

	}

	public void setDepartmentService(DepartmentService service) {

		this.service = service;

	}

	// Carrega os Departamentos MOCK

	public void updateTableView() {

		if (service == null) {

			throw new IllegalStateException(" Service esta null ");
		}

		List<Department> lista = service.findAll();

		obsList = FXCollections.observableArrayList(lista); // Carrega a lista no objeto ObsList
		tableViewDepartment.setItems(obsList); // Carrega a lista na TableViewDepartment
		initEditButtons(); // Acrescenta a opcao Edit em cada linha da tabela
		initRemoveButtons();
	}

	public void InicializeNode() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padrao do JavaFX para iniciar as colunas
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));

		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega a referencia da Janela getWindow() e super classe
																// do Stage por isso o DowCast
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Ajusta a Table View de acordo com o
																				// Stage, janela.

	}

	// Cria uma referencia para o stage, janela que criou a janela de dialogo. Quand
	// secria uma janela tem que informar
	// ostage da janela que criou a janela de dialog

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Carrega a view

			DepartmentFormController controller = loader.getController(); // Pega o controller da tela

			controller.setDepartment(obj);

			controller.setDepartmentService(new DepartmentService()); // intancia a Dependencia

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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.curretStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
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

	private void removeEntity(Department obj) {

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
				
				e.printStackTrace();
				Alerts.showAlert("Erro ao deletar obj", null, e.getMessage(), AlertType.ERROR);
			}

		}

	}
}
