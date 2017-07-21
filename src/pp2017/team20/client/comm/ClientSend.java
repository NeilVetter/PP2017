package pp2017.team20.client.comm;

import java.io.BufferedOutputStream; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Einrichten der Threads um Nachrichten zum Server zu senden
 *   
 * @author Yuxuan Kong, 6019218
 * 
 */

public class ClientSend extends Thread {
	/**
	 * Attributblock
	 * 
	 * @author Yuxuan Kong
	 */

	private ObjectOutputStream output;
	private LinkedBlockingQueue<Message> messageQueue;

	/**
	 * Der Konstruktor erstellt den Outputstream und den Send-Thread
	 * 
	 * 
	 * @author Yuxuan Kong, 6019218
	 * 
	 */
	public ClientSend(Socket clientSocket, LinkedBlockingQueue<Message> messageQueue) {

		this.messageQueue = messageQueue;
		try {
			output = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			output.flush();
			System.out.println("Client SendThread gestartet");

		} catch (IOException e) {
			
			System.out.println("Send-Thread des Clienten kann nicht gestartet werden.");
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
	 * checkMessage() prueft, ob im Stream Nachrichten für den Server da sind. Falls sich welche im Stream 
	 * befinden, dann werden diese in die Queue eingefuegt.
	 * 
	 * @author Yuxuan Kong, 6019218
	 */
	public void checkMessage() {
		if (messageQueue.isEmpty() != true)
			sendMessage();
	}

	/**
	 * Die Methode sendMessage() schreibt die Nachrichten von der Queue in einem OutputStream
	 * 
	 * @author Yuxuan Kong, 6019218
	 * 
	 */
	public void sendMessage() {

		try {
			// Nachrichten werden aus der Queue rausgenommen und in den OutputStream geschrieben
			output.writeObject(messageQueue.poll());
			output.flush();
			output.reset();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Nachricht konnte nicht gesendet!");
		}

	}

}
