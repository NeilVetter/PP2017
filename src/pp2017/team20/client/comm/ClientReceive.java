package pp2017.team20.client.comm;

import java.io.BufferedInputStream;   
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.net.Socket;

import pp2017.team20.shared.Message;

/**
 * Einrichten der Threads um Nachrichten vom Server zu empfangen
 * 
 * @author Yuxuan Kong, 6019218
 * 
 */

public class ClientReceive extends Thread {
	/**
	 * Attribut-Block
	 * 
	 * @author Yuxuan Kong, 6019218
	 */
	
	private ObjectInputStream input;
	private BlockingQueue<Message> messageQueue;
	private boolean serverCheck = true;

	/**
	 * Konstruktor fuer die Klasse. Dort wird die Queue uebergeben und ein InputStream wird erstellt
	 * 
	 * 
	 * @author Yuxuan Kong, 6019218
	 */
	public ClientReceive(Socket clientSocket, BlockingQueue<Message> messageQueue) {
		
		this.messageQueue = messageQueue;
		try {
			System.out.println("Receiver-Thread für den Clienten gestartet");
			input = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Receiver-Thread für den Clienten konnte nicht gestartet werden");
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
	 * checkMessage() prueft, ob im Stream Nachrichten vom Server da sind. Falls sich welche im Stream 
	 * befinden, dann werden diese in die Queue eingefuegt.
	 * 
	 * @author Yuxuan Kong, 6019218
	 */

	public void checkMessage() {
		Message message = null;
		try {
			while ((message = (Message) input.readObject()) != null) {
				messageQueue.add(message);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Server nicht erreichbar.");
		} catch (IOException e) {
		System.out.println("Server ist nicht mehr ERREICHBAR " + e);
		serverCheck = false;
		}
	}

}
