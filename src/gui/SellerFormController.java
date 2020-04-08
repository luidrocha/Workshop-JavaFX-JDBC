package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constrants;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Exceptions.ValidationException;
import model.entities.Seller;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();// Guardas os objetos que querem receber
																				// eventos

	@FXML
	private TextField txtFieldId;

	@FXML
	private TextField txtFieldName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	private Label lblMensagemErro;

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
		
		}
		
		obj.setName(txtFieldName.getText());

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

	public void setSellerService(SellerService service) {

		this.service = service;
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

	}

	private void setErrorMessage(Map<String, String> errors) {

		Set<String> fields = errors.keySet(); // Cria um set com as mensages de erro do Map

		if (fields.contains("name")) { // Verifica se existe a chave no Map

			lblMensagemErro.setText(errors.get("name")); // Seta a mensagem de erro no label.
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Inicializa as Limitacoes (Constrants)

		initializableNodes();

	}

	private void initializableNodes() {

		Constrants.setTextFieldInteger(txtFieldId);
		Constrants.setTextFieldMaxLength(txtFieldName, 30);

	}

}
