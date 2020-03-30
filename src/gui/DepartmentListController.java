package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service; // Cria um Dependencia
	
	private ObservableList<Department> obsList; // Usada para carregar os departamentos e sera associada a TableView

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
	
	public void setDepartmentService(DepartmentService service) {
		
		this.service = service;
		
	}
	
	// Carrega os Departamentos MOCK 
	
	public void updateTableView() {
		
		if (service == null) {
			
			throw new IllegalStateException(" Service esta null ") ;	
		}
		
		List<Department> lista = service.findAll();
		
		obsList = FXCollections.observableArrayList(lista); // Carrega a lista no objeto ObsList
		tableViewDepartment.setItems(obsList); //Carrega a lista na TableViewDepartment
	}

	public void InicializeNode() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padrao do JavaFX para iniciar as colunas
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega a referencia da Janela getWindow() e super classe do Stage por isso o DowCast
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Ajusta a Table View de acordo com o Stage, janela.

	}
	
	

}
