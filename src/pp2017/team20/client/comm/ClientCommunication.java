package pp2017.team20.client.comm;


import java.io.IOException;  
import java.util.concurrent.LinkedBlockingQueue;
import java.net.InetAddress;
import java.net.Socket;

import pp2017.team20.shared.Message;

/**
 * Die Klasse ClientCommunication dient dazu, dass der Client mit dem Server 
 * kommunizieren kann.
 * 
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */
public class ClientCommunication extends Thread {
	/**
	 * Attributblock
	 */
	public static LinkedBlockingQueue<Message> outputMessage = new LinkedBlockingQueue<Message>();
	public static LinkedBlockingQueue<Message> inputMessage = new LinkedBlockingQueue<Message>();
	public Socket clientSocket;

	public boolean connectServer() {
		boolean successful = false;
		try {
			// Socket wird erstellt
			InetAddress serverAddress = InetAddress.getByName("localhost");
			clientSocket = new Socket(serverAddress, 2000);
			System.out.println("Client wurde jetzt gestartet!");

			// Threads werden erstellt und nun gestartet
			ClientSend sendThread = new ClientSend(clientSocket, outputMessage);
			sendThread.start();
			ClientReceive receiveThread = new ClientReceive(clientSocket, inputMessage);
			receiveThread.start();
			Thread.sleep(50);

			successful = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitte die richtige Serveradresse eingeben");
			successful = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Verbinden Sie sich bitte erneut.");
			successful = false;
		}

		return successful;
	}

	@Override
	public void run() {
		
		while (true) {
			
			try {
				// alle 30 Sekunden
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * Diese Methode empfaengt Nachrichten vom Server
	 * 
	 * 
	 * @author Kong, Yuxuan,  6019218
	 */
	public Message receiveMessage() {

		Message newMessage = null;
		try {
			while (!inputMessage.isEmpty()) {
				newMessage = inputMessage.take();
			}
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gelesen werden " + e);
		}
		return newMessage;

	}
	
	
	/**
	 * Diese Methode sendet Nachrichten vom Clienten zum Server
	 * 
	 * @author Kong, Yuxuan, 6019218
	 * 
	 */
	public void sendMessage(Message message) {

		try {
			outputMessage.put(message);
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte leider nicht gesendet werden" + e);
		}
	}

}
