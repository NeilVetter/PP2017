package pp2017.team20.client.comm;

import java.io.BufferedOutputStream; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Die Threads zum Senden von Nachrichten
 * 
 * @author Samet, Koruk, 5869110
 * 
 */

public class ClientSenden extends Thread {
	/**
	 * Attributblock
	 * 
	 * @author Samet, Koruk, 5869110
	 */

	private ObjectOutputStream out;
	private LinkedBlockingQueue<Message> blockQ;

	/**
	 * Konsturktor zum Erstellen vom Send-Thread und des Streams
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	public ClientSenden(Socket clientSocket, LinkedBlockingQueue<Message> blockQ) {

		// Socket
		this.blockQ = blockQ;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			out.flush();
			System.out.println("Client SendThread gestartet");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client Sendthread nicht gestartet.");
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
			checkMes();
		}
	}

	/**
	 * Methode, zum Ueberpruefen, ob Nachrichten in der Queue sind und falls ja,
	 * dann sendMes aufrufen
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	public void checkMes() {
		if (blockQ.isEmpty() != true)
			sendMes();
	}

	/**
	 * Methode die, die Nachrichten aus der Queue nimmt und in den Stream
	 * schreibt
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	public void sendMes() {

		try {
			// zieht sich die Nachricht aus der Queue und schickt Sie weiter
			out.writeObject(blockQ.poll());
			out.flush();
			out.reset();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Nachricht konnte nicht gesendet!");
		}

	}

}
