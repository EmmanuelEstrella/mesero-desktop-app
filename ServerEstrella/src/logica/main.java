package logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class main {

	public static void main(String[] args) throws IOException {
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
	
