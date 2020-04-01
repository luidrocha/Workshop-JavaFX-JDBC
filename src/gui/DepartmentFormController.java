package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Action;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constrants;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;

	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();// Guardas os objetos que querem receber eventos
	

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

		} catch (DbException e) {

			Alerts.showAlert("Erro ao Salvar", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// Executa o metodo onDataChanged() em cada objeto cadastrado na lista
	//Classe que emite o evento
	
	private void notefyDataChangelisteners() {
	
		for (DataChangeListener listner : dataChangeListeners) {
			
			listner.onDataChanged();
		}
		
	}

	private Department getFormData() {

		Department obj = new Department();

		obj.setId(Utils.parseInteger(txtFieldId.getText()));
		obj.setName(txtFieldName.getText());
		return obj;
	}

	@FXML
	public void onCancelAction(ActionEvent event) {

		Utils.curretStage(event).close(); // Fecha a janela
	}

	public void setDepartment(Department entity) {

		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {

		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		
		dataChangeListeners.add(listener); //Adicionao o 
	}

	public void updateFormData() {

		if (entity == null) {

			throw new IllegalStateException("Entidade esta nula ! ");
		}
		txtFieldId.setText(String.valueOf(entity.getId())); // Converte o Id numerico para texto pq o txtfield trabalha
															// com texto
		txtFieldName.setText(entity.getName());

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
