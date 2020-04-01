package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constrants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	private Department entity;

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
	public void onBtSaveAction() {

		System.out.println(" nBtSaveAction() ");

	}

	@FXML
	public void onCancelAction() {

		System.out.println(" onCancelAction() ");
	}
	
	public void setDepartment(Department entity) {
		
		this.entity=entity;
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
