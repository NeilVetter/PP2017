package pp2017.team20.client.comm;


import java.io.BufferedInputStream;  
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import pp2017.team20.shared.Message;

/**
 * Erstellen von Threads um Nachrichten vom Server empfangen zu koennen
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */

public class ClientReceive extends Thread {
	/**
	 * Attributeblock
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	
	private ObjectInputStream input;
	private BlockingQueue<Message> messageQueue;
	private boolean serverCheck = true;

	/**
	 * In dem Konstruktor dieser Klasse wird die Queue uebergeben und der Stream
	 * erstellt
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public ClientReceive(Socket clientSocket, BlockingQueue<Message> messageQueue) {
		
		this.messageQueue = messageQueue;
		try {
			System.out.println("ReceiveThread des Clienten wurde gestartet");
			input = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Receivethread des Clienten konnte nicht gestartet.");
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		while (serverCheck) {
			checkMessage();
		}
	}

	/**
	 * Diese Methode prueft, ob Nachrichten im Inputstream sind. Falls ja, dann werden diese 
	 * in die Queue eingefuegt.
	 * 
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */

	public void checkMessage() {
		Message message = null;
		try {
			while ((message = (Message) input.readObject()) != null) {
				messageQueue.add(message);
			}

		} catch (IOException e) {
			System.out.println("Server ist nicht ERREICHBAR! Überprüfen Sie bitte die Verbidnung " + e);
			serverCheck = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Server ist nicht erreichbar.");
		}

	}

}
