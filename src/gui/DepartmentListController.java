package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML
	private TableView<Department> tableViewDepartment; // Define um objeto do tipo TableView<Department>. Apos declarar
														// devese criar um metodo para que funcione.

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Define um objeto do tipo TableColumn<Department, Integer>
															// , onde integer e o tipo do campo

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNovo;

	@FXML
	public void btNovoAction() {

		System.out.println("  btNovoAction() ");

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		InicializeNode();

	}

	public void InicializeNode() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padrao do JavaFX para iniciar as colunas
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega a referencia da Janela getWindow() e super classe do Stage por isso o DowCast
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Ajusta a Table View de acordo com o Stage, janela.

	}

}
