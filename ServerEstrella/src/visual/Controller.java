package visual;

import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import logica.Order;
import logica.StatusMessage;
import logica.WaiterMachine;

public class Controller implements Initializable {
	
	@FXML
    private JFXHamburger drawerBtn;

    @FXML
    private JFXDrawer mainDrawer;
    
    @FXML
    private AnchorPane ordersPane;
	@FXML
	private StackPane rootStackPane;
	@FXML
	private AnchorPane noOrdersPane;

	@FXML
	private AnchorPane waitersPane;
	@FXML
	private AnchorPane nowaitersPane;

	@FXML
	private JFXMasonryPane masonPane;

	@FXML
	private JFXMasonryPane waitersMasonPane;

	@FXML
	private ScrollPane scrollPane;

	private int cardCount = 0;

	private Order tempOrder;

	private ArrayList<WaiterMachine> waiters;

	private DatagramSocket socketForClients;
	private DatagramSocket socketForPis;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		try {
			socketForClients = new DatagramSocket(9876);
			socketForPis = new DatagramSocket(9870);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder()
				.create();
		waiters = new ArrayList<>();

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
					waitForClientOrders();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
        Runnable r2 = new Runnable() {
            public void run() {
                try {
					waitForWaitersMessages();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };

        new Thread(r).start();
        new Thread(r2).start();
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
					findWaiter(String.valueOf(tempOrder.getTableId()),box);
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
					findWaiter(String.valueOf(order.getTableId()),box);
				}
			});

			masonPane.getChildren().add(box);
			cardCount++;

		}catch (IOException e1){
			e1.printStackTrace();
		}

		updatePanes();

	}

	public void displayNewWaiterCard(int updatedPosition){
		try{
			WaiterMachine waiter = waiters.get(updatedPosition);

			AnchorPane box = waitersMasonPane.getChildren().size() > 0 ?
					(AnchorPane) waitersMasonPane.getChildren().get(updatedPosition) : null;

			boolean isNew = false;
			if(box == null){
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Controller.class.getResource("orderCard.fxml"));
				box = loader.load();
				isNew = true;
			}

			box.setId("waiter-"+waiter.getId());
			Text title = (Text) box.lookup("#textId");
			JFXListView list = (JFXListView) box.lookup("#itemList");
			title.setText("Mesero: " + waiter.getName());
			list.getItems().clear();
			list.getItems().add( "Estado: " + waiter.getStatus());
			list.getItems().add( "IP: " + waiter.getIpAddress().toString());



			if(isNew){
				JFXButton sendBtn = (JFXButton) box.lookup("#sendBtn");
				sendBtn.setVisible(false);
				waitersMasonPane.getChildren().add(box);
			}


		}catch (IOException e1){
			e1.printStackTrace();
		}

		updatePanes();

	}

	/**
	 * Metodo utilizado para actualizar el mensaje en la pantalla en caso de que haya o no nuevas ordenes.
	 */
	public void updatePanes(){

		noOrdersPane.setVisible( masonPane.getChildren().size() <= 0 );
		nowaitersPane.setVisible( waitersMasonPane.getChildren().size() <= 0);
	}

	/**
	 * Metodo utilizado para ver si hay PI o Meseros disponible, si no hay muestra (AUN NO SE HACE) un mensaje al usuario.
	 * @param data
	 * @param box
	 */
	public void findWaiter(String data, AnchorPane box){
		for (WaiterMachine waiter : waiters) {
			if (waiter.getStatus().equals("INIT") || waiter.getStatus().equals("AVAILABLE")) {
				sendDatatoPi(data, box, waiter);
				return;
			}
		}
		System.out.println("NO WAITER FOUND");
		displayErrorMessage("No hay meseros disponible en este momento. Verifique el estado de sus meseros en la pestaña de 'Meseros'");
		// si eso recorrio y no encontro hay que mostrar un mensaje diciendo que no hay maquinas disponibles
		///////////////////////////
	}
	public void displayErrorMessage(String errorMessage){

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller.class.getResource("ErrorDialog.fxml"));
			StackPane content = loader.load();
			Text errorText = (Text) content.lookup("#errorText");
			errorText.setText(errorMessage);
			JFXButton closeBtn = (JFXButton) content.lookup("#closeBtn");
			JFXDialog dialog = new JFXDialog();
			closeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					dialog.close();
				}
			});
			dialog.setDialogContainer(rootStackPane);
			dialog.setContent(content);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo utilizado para remover la card de orden que se ve a enviar y ademas crea el nuevo hilo para enviar el mensaje
	 * hacia la PI.
	 * @param data
	 * @param box
	 * @param waiter
	 */
	public void sendDatatoPi(String data, AnchorPane box, final WaiterMachine waiter){
		int index = masonPane.getChildren().indexOf(box);
		masonPane.getChildren().remove(index);
		updatePanes();

		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					sendDataToPITask(data, waiter);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	/**
	 * Metodo utilizado para enviar mensaje de ordenes hacia la pi. (Este metodo se llama desde un Hilo nuevo)
	 * @param data
	 * @param waiter el objeto de la PI o el Mesero
	 * @throws IOException
	 */
	public  void sendDataToPITask(String data, WaiterMachine waiter) throws IOException{
		System.out.println("DATA TO SEND TO PI: " + data);

		InetAddress IPAddress = waiter.getIpAddress(); //direccion de la pi seleccionada

		//puerto al que escucha la pi;
		int port = waiter.getListeningPort();
		byte[] sendData;

		sendData = data.getBytes();
		DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length, IPAddress, port);
		socketForPis.send(sendPacket);
		System.out.println("DATA SENT TO PI");
	}

	/**
	 * Metodo utilizado por un hilo para esperar por los mensajes / ordenes de los clientes.
	 * @throws IOException
	 */
	private void waitForClientOrders() throws IOException {
		// TODO Auto-generated method stub


		 while(true)
			{
			   byte[] receiveData = new byte[1024];
			   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socketForClients.receive(receivePacket);

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

	/**
	 * Metodo utilizado por un hilo para esperar por los mensajes de estato de los Waiter o PI.
	 * @throws IOException
	 */
	private void waitForWaitersMessages() throws IOException {
		// TODO Auto-generated method stub

		System.out.println("Waiting for pi messages");
		while(true)
		{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socketForPis.receive(receivePacket);


			//Parsing del JSON a objeto
			String sentence = new String( receivePacket.getData());
			JsonReader reader = new JsonReader(new StringReader(sentence));
			reader.setLenient(true);
			Gson gson = new GsonBuilder()
					.create();
			System.out.println("RECEIVED_UPDATE: " + sentence);
			StatusMessage message =  gson.fromJson(reader,StatusMessage.class);
			System.out.println("FROM IP: " + receivePacket.getAddress().toString());
			System.out.println("FROM: " + message.getMachineName());

			System.out.println("STATUS: " + message.getStatus());
			final int positionUpdated = updateOrCreateMachine(message, receivePacket.getAddress());


			///////////////////////////////////////////////
			Platform.runLater(new Runnable() { // El thread de la visual
				@Override
				public void run() {
					displayNewWaiterCard(positionUpdated);
				}
			});

		}

	}

	/**
	 * Metodo utilizado para agregar un nuevo Waiter o PI a la lista de Waiters para que se puedan enviar ordenes.
	 * @param message
	 * @param addr
	 * @return int position
	 */
	public int updateOrCreateMachine(StatusMessage message, InetAddress addr){
		int position = -1;
		for( int i = 0; i < waiters.size() ; i++ ){
			if( waiters.get(i).getId() == message.getMachineId() ){
				waiters.get(i).setName(message.getMachineName());
				waiters.get(i).setStatus(message.getStatus());
				waiters.get(i).setIpAddress(addr);
				position = i;
			}
		}

		if( position == -1){
			WaiterMachine newMachine = new WaiterMachine(
					message.getStatus(),
					message.getMachineName(),
					message.getMachineId(),
					addr,
					message.getMachinePort()
			);
			waiters.add(newMachine);
			return waiters.size() - 1;

		}

		return position;
	}

	
	

}
