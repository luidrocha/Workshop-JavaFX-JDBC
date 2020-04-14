package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constrants;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.Exceptions.ValidationException;
import model.entities.Department;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();// Guardas os objetos que querem receber
																				// eventos

	@FXML
	private TextField txtFieldId;

	@FXML
	private TextField txtFieldName;

	@FXML
	private TextField txtFieldEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtFieldBaseSalay;

	@FXML
	private ComboBox<Department> comboDepartments;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	// @FXML
	// private Label lblMensagemErro;

	@FXML
	private Label lblErrorName;

	@FXML
	private Label lblErrorEmail;

	@FXML
	private Label lblErrorBithDate;

	@FXML
	private Label lblErrorBaseSalary;

	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {

			throw new IllegalStateException(" Entidade nulla !!");
		}

		if (service == null) {

			throw new IllegalStateException(" Servico esta nullo ");
		}

		try {

			entity = getFormData();
			service.saveorUpdate(entity);
			notefyDataChangelisteners();
			Utils.curretStage(event).close(); // Fecha a janela

		} catch (ValidationException e) { // Se houver excecao no campo
			setErrorMessage(e.getErrors()); // pega o erro no Map
		} catch (DbException e) {

			Alerts.showAlert("Erro ao Salvar", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// Executa o metodo onDataChanged() em cada objeto cadastrado na lista
	// Classe que emite o evento

	private void notefyDataChangelisteners() {

		for (DataChangeListener listner : dataChangeListeners) {

			listner.onDataChanged();
		}

	}

	private Seller getFormData() {

		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.parseInteger(txtFieldId.getText()));

		// Trim() Elimina espaco do inicio e do fim,
		if (txtFieldName.getText() == null || txtFieldName.getText().trim().equals(" ")) {

			exception.addError("name", "Campo nao pode estar vazio"); // Adiciona a excecao no Map de errors.

		} else {

			obj.setName(txtFieldName.getText());

		}

		if (txtFieldEmail.getText() == null || txtFieldEmail.getText().trim().equals(" ")) {

			exception.addError("email", "Campo nao pode estar vazio"); // Adiciona a excecao no Map de errors.

		} else {

			obj.setEmail(txtFieldEmail.getText());

		}

//		 Pegando a data/valor que esta no DatePicker
//
//		 Variavel instant independe da localidade do usuário. Ja o
//		 atStartOfDay(ZoneId.systemDefault())); converte a data

		if (dpBirthDate.getValue() == null) {
			// "brithDate" refere-se ao campo

			exception.addError("birthDate", "Data de aniversário não pode ser nula ");

		} else {

			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));

			obj.setBirthDate(Date.from(instant)); // Converte pro formato Date

		}

		if (txtFieldBaseSalay.getText() == null || txtFieldBaseSalay.getText().trim().equals("")) {

			// baseSalary chave do MAP
			exception.addError("baseSalary", "Campo nao pode estar vazio"); // Adiciona a excecao no Map de errors.

		} else {

			obj.setBaseSalary(Utils.parseDouble(txtFieldBaseSalay.getText()));

		}
		
		obj.setDepartment(comboDepartments.getValue());
		
		if (exception.getErrors().size() > 0) {

			throw exception;
		}

		return obj;
	}

	@FXML
	public void onCancelAction(ActionEvent event) {

		Utils.curretStage(event).close(); // Fecha a janela
	}

	public void setSeller(Seller entity) {

		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {

		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListeners.add(listener); // Adicionao o obj que deseja escutar as mudancas
	}

	public void updateFormData() {

		if (entity == null) {

			throw new IllegalStateException("Entidade esta nula ! ");
		}
		txtFieldId.setText(String.valueOf(entity.getId())); // Converte o Id numerico para texto pq o txtfield trabalha
															// com texto
		txtFieldName.setText(entity.getName());
		txtFieldEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtFieldBaseSalay.setText(String.format("%.2f", entity.getBaseSalary()));

		// Converte a data pra data local da maquina ,ZoneId.systemDefault()) pega o
		// formato systema

		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

		// Se for um seller novo , carrega o primeiro Department da base

		if (entity.getDepartment() == null) {

			comboDepartments.getSelectionModel().selectFirst();
		} else {

			comboDepartments.setValue(entity.getDepartment()); // Carrega o departamento da entidade na comboBox
		}

	}

	// Preenche o comboBox
	public void loadAssociatedObjects() {

		if (departmentService == null) {

			throw new IllegalStateException(" DepartmentService	 esta null !!");
		}

		List<Department> list = departmentService.findAll(); // Pega os departments no base
		obsList = FXCollections.observableArrayList(list); // Seta a lista no obsList
		comboDepartments.setItems(obsList); // Adiciona a obslist no Combo

	}

	// Seta as mensagem de erro em cada label do campo

	private void setErrorMessage(Map<String, String> errors) {

		Set<String> fields = errors.keySet(); // Cria um set com as mensages de erro do Map
		
		
//		/Subsituido por operador Ternario Condicional
/*
 * 
 * 
		if (fields.contains("name")) { // Verifica se existe a chave no Map

			lblErrorName.setText(errors.get("name")); // Seta a mensagem de erro no label.
		} else {

			lblErrorName.setText(" ");
//		}
 * 
 * **/
 
		
		
		// Pode-se trocar o IF por operador condicional TERNARIO, pra reduzir o código.
		
		lblErrorName.setText(fields.contains("name")? errors.get("name"): ""); // ? equivale a então e : senão
		lblErrorEmail.setText(fields.contains("email")? errors.get("email"): "");
		lblErrorBithDate.setText(fields.contains("birthDate")? errors.get("birthDate"): "");
		lblErrorBaseSalary.setText(fields.contains("baseSalary")? errors.get("baseSalary"): "");
		
	/* 	

		Substituido por condição Ternaria acima. Reduzindo o código.
		
		if (fields.contains("email")) { // Verifica se existe a chave no Map

			lblErrorEmail.setText(errors.get("email")); // Seta a mensagem de erro no label.
		} else {

			lblErrorEmail.setText(" ");
		}

		if (fields.contains("baseSalary")) { // Verifica se existe a chave no Map

			lblErrorBaseSalary.setText(errors.get("baseSalary")); // Seta a mensagem de erro no label.
		} else {

			lblErrorBaseSalary.setText(" ");
		}

		if (fields.contains("birthDate")) { // Verifica se existe a chave no Map birthDate

			lblErrorBithDate.setText(errors.get("birthDate")); // Seta a mensagem de erro no label.
		} else {

			lblErrorBithDate.setText("");
		}
		
		**/

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboDepartments.setCellFactory(factory); // comboBox
		comboDepartments.setButtonCell(factory.call(null));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Inicializa as Limitacoes (Constrants)

		initializableNodes();

	}

	private void initializableNodes() {

		Constrants.setTextFieldInteger(txtFieldId);
		Constrants.setTextFieldMaxLength(txtFieldName, 70);
		Constrants.setTextFieldMaxLength(txtFieldEmail, 30);
		Constrants.setTextFieldDouble(txtFieldBaseSalay);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();

	}

}
