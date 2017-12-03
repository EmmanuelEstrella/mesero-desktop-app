package visual;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Welcome extends Application implements Initializable {
	
	
	@FXML
	AnchorPane tt;
	 
	@Override
	public void start(Stage primaryStage) {
		
		
		 FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Welcome.class.getResource("welcome.fxml"));
         try {
			tt = (AnchorPane) loader.load();
			Scene scene = new Scene(tt);
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.setScene(scene);
            primaryStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

	public static void main(String[] args) {
		launch(args);
	}




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
	}
}
