package visual;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Controller implements Initializable {
	
	@FXML
    private JFXHamburger drawerBtn;

    @FXML
    private JFXDrawer mainDrawer;
    
    @FXML
    private AnchorPane root;
    
   


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		 try {
			 	FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(Controller.class.getResource("SidePanelContent.fxml"));
	            VBox box = loader.load();
	            mainDrawer.setSidePane(box);
	        } catch (IOException ex) {
	            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
	        	
	        }
	        
		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(drawerBtn);
        transition.setRate(-1);
        drawerBtn.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            System.out.println("FLOF");
            if(mainDrawer.isShown())
            {
            	mainDrawer.close();
            }else
            	mainDrawer.open();
        });
        
        Runnable r = new Runnable() {
            public void run() {
                try {
					runYourBackgroundTaskHere();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };

        new Thread(r).start();
		
	}
        
		private void runYourBackgroundTaskHere() throws IOException {
			// TODO Auto-generated method stub
			 DatagramSocket serverSocket = new DatagramSocket(9876);
		        
	         
	         while(true)
	            {
	        	   byte[] receiveData = new byte[1024];
	               DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	               serverSocket.receive(receivePacket);
	               
	               String sentence = new String( receivePacket.getData());
	               System.out.println("RECEIVED: " + sentence);
	               
	               InetAddress IPAddress = null;
	               IPAddress = InetAddress.getByName("192.168.0.109");
	               int port = 9876;
	               byte[] sendData = receiveData;
	               String capitalizedSentence = sentence.toUpperCase();
	               sendData = capitalizedSentence.getBytes();
	               DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length, IPAddress, port);
	               serverSocket.send(sendPacket);
	               
	                
	     

	            }
			
		}
    
	
	

}
