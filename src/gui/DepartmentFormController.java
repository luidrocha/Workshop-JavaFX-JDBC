package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constrants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// Inicializa as Limitacoes (Constrants)
		
		initializableNodes();
		
	}
	
		private void  initializableNodes() {
			
			Constrants.setTextFieldInteger(txtFieldId);
			Constrants.setTextFieldMaxLength(txtFieldName, 30);
			
			
	
		
	}

}
