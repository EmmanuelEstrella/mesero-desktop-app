package visual;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainAct extends Application {

   @FXML
    private JFXHamburger drawerBtn;

    @FXML
    private JFXDrawer mainDrawer;

	StackPane rootPane;
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Welcome.class.getResource("main.fxml"));

        
        try {

			URL url = this.getClass().getResource("/visual/customStyles.css");
			if (url == null) {
				System.out.println("Resource not found. Aborting.");
				System.exit(-1);
			}
			String css = url.toExternalForm();

			rootPane = (StackPane) loader.load();
			JFXDecorator decorator = new JFXDecorator(primaryStage, rootPane);

			Scene scene = new Scene(decorator);
			scene.getStylesheets().add(css);

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
}
