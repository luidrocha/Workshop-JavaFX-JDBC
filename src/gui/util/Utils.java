package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage curretStage(ActionEvent event) {
		
		
		return (Stage) ((Node)event.getSource()).getScene().getWindow(); //getScene e superclasse de stage, por isso o dowCast.
		
		
	}
	

}
