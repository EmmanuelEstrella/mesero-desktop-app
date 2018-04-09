package visual;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logica.Order;

public class Controller implements Initializable {
	
	@FXML
    private JFXHamburger drawerBtn;

    @FXML
    private JFXDrawer mainDrawer;
    
    @FXML
    private AnchorPane ordersPane;
	@FXML
	private AnchorPane noOrdersPane;

	@FXML
	private AnchorPane waitersPane;

	@FXML
	private JFXMasonryPane masonPane;

	@FXML
	private ScrollPane scrollPane;

	private int cardCount = 0;

	private Order tempOrder;
   


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder()
				.create();

		tempOrder =  gson.fromJson("{\"items\":[\"Hamburguesa\",\"Pizza\"],\"tableId\":2}",Order.class);
		
//		 try {
//			 	FXMLLoader loader = new FXMLLoader();
//		        loader.setLocation(Controller.class.getResource("SidePanelContent.fxml"));
//	            VBox box = loader.load();
//	            mainDrawer.setSidePane(box);
//	        } catch (IOException ex) {
//	            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//
//	        }
//
//		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(drawerBtn);
//        transition.setRate(-1);
//        drawerBtn.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
//            transition.setRate(transition.getRate()*-1);
//            transition.play();
//            System.out.println("FLOF");
//            if(mainDrawer.isShown())
//            {
//            	mainDrawer.close();
//            }else
//            	mainDrawer.open();
//        });
        
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
		Platform.runLater(() -> scrollPane.requestLayout());

		JFXScrollPane.smoothScrolling(scrollPane);
	}

	public void addCard(ActionEvent e){
		System.out.println("CLICKED");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller.class.getResource("orderCard.fxml"));
			AnchorPane box = loader.load();
			Text t = (Text) box.lookup("#textId");
			JFXListView l = (JFXListView) box.lookup("#itemList");
			t.setText("Orden de la Mesa: " + tempOrder.getTableId());

			for(int i = 0; i < tempOrder.getItems().size(); i++){
				l.getItems().add(tempOrder.getItems().get(i));
			}

			JFXButton sendBtn = (JFXButton) box.lookup("#sendBtn");
			sendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("SEND CLICKED");
					int value = cardCount;
					sendDatatoPi(String.valueOf(tempOrder.getTableId()),box);
				}
			});

//			for(int i = 0; i < 100; i++){
//				Label lbl = new Label();
//				lbl.setPrefSize(r.nextInt(200),r.nextInt(200));
//				lbl.setStyle("-fx-background-color : rgb(" + r.nextInt(255) + "," + r.nextInt(255) + "," + r.nextInt(255) + ")");
//				masonPane.getChildren().add(lbl);
//			}
			//box.setPrefHeight(r.nextInt(200));
			JFXDepthManager.setDepth(box, 1);
			masonPane.getChildren().add(box);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		updatePanes();

	}

	public void displayNewCard(Order order){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller.class.getResource("orderCard.fxml"));
			AnchorPane box = loader.load();
			Text t = (Text) box.lookup("#textId");
			JFXListView l = (JFXListView) box.lookup("#itemList");
			t.setText("Orden de la Mesa: " + order.getTableId());
			for(int i = 0; i < order.getItems().size(); i++){
				l.getItems().add(order.getItems().get(i));
			}
//			for(int i = 0; i < 100; i++){
//				Label lbl = new Label();
//				lbl.setPrefSize(r.nextInt(200),r.nextInt(200));
//				lbl.setStyle("-fx-background-color : rgb(" + r.nextInt(255) + "," + r.nextInt(255) + "," + r.nextInt(255) + ")");
//				masonPane.getChildren().add(lbl);
//			}
			//box.setPrefHeight(r.nextInt(200));
			JFXButton sendBtn = (JFXButton) box.lookup("#sendBtn");
			sendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("SEND CLICKED");
					int value = cardCount;
					sendDatatoPi(String.valueOf(order.getTableId()),box);
				}
			});

			masonPane.getChildren().add(box);
			cardCount++;

		}catch (IOException e1){
			e1.printStackTrace();
		}

		updatePanes();

	}
	public void updatePanes(){
		if(masonPane.getChildren().size() > 0){
			noOrdersPane.setVisible(false);
		}else{
			noOrdersPane.setVisible(true);
		}
	}
	public void sendDatatoPi(String data, AnchorPane box){
		int index = masonPane.getChildren().indexOf(box);
		masonPane.getChildren().remove(index);
		updatePanes();
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					sendDataToPITask(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	public  void sendDataToPITask(String data) throws IOException{
		System.out.println("DATA TO SEND TO PI: " + data);
		DatagramSocket serverSocket = new DatagramSocket(9878);
		InetAddress IPAddress = null;
		IPAddress = InetAddress.getByName("192.168.0.109");//direccion de la pi
		int port = 9876;
		byte[] sendData;

		sendData = data.getBytes();
		DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
		serverSocket.close();
		System.out.println("DATA SENT TO PI");
	}
	private void runYourBackgroundTaskHere() throws IOException {
		// TODO Auto-generated method stub

		DatagramSocket serverSocket = new DatagramSocket(9876);

		 while(true)
			{
			   byte[] receiveData = new byte[1024];
			   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			   serverSocket.receive(receivePacket);

				//Parsing del JSON a objeto
			   String sentence = new String( receivePacket.getData());
			   JsonReader reader = new JsonReader(new StringReader(sentence));
			   reader.setLenient(true);
			   Gson gson = new GsonBuilder()
						.create();
			   System.out.println("RECEIVED: " + sentence);
			   Order order =  gson.fromJson(reader,Order.class);
			   System.out.println("TABLE: " + order.getTableId());
			   System.out.println("ITEMS: " + order.getItems());
			   ///////////////////////////////////////////////
			   Platform.runLater(new Runnable() { // El thread de la visual
					@Override
					public void run() {
						displayNewCard(order);
					}
				});
				
			}

	}

	
	

}
