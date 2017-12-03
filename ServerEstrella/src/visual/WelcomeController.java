package visual;


import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class WelcomeController {
	
	@FXML
	private JFXButton initBtn;
	

	public void loadPage(ActionEvent event) {
		System.out.println("Hola");
		
		try {
			Stage stage = (Stage) initBtn.getScene().getWindow();
			new MainAct().start(new Stage());
			stage.close();
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	

}
