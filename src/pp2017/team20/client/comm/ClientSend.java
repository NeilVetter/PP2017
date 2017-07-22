package pp2017.team20.client.comm;


import java.io.BufferedOutputStream;  
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Die Klasse ClientSend ist verantwortlich für das Senden der Nachrichten zum Server
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */

public class ClientSend extends Thread {
	/**
	 * 
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */

	private ObjectOutputStream output;
	private LinkedBlockingQueue<Message> messageQueue;

	/**
	 * Der Konstruktor erstellt Send-Thread und den Outputstreams
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public ClientSend(Socket clientSocket, LinkedBlockingQueue<Message> messageQueue) {

		// Socket
		this.messageQueue = messageQueue;
		try {
			output = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			output.flush();
			System.out.println("SendThread des Clienten wurde gestartet");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sendthread des Clienten konnte nicht gestartet werden.");
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
		while (true) {
			checkMessage();
		}
	}

	/**
	 * Diese Methode überprüft ob Nachrichten in der Queue sind und falls ja,
	 * dann wird sendMessage aufgerufen
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void checkMessage() {
		if (messageQueue.isEmpty() != true)
			sendMessage();
	}

	/**
	 * Die Methode zieht Nachrichten aus der Queue und schreibt in den OutputStream
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void sendMessage() {

		try {
			// zieht sich die Nachricht aus der Queue und schickt Sie weiter
			output.writeObject(messageQueue.poll());
			output.flush();
			output.reset();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Nachrichten konnten nicht gesendet werden!");
		}

	}

}
