package pp2017.team20.server.comm;

import java.io.BufferedOutputStream; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Die Threads zum Senden von Nachrichten (vom Server an den Client)
 * 
 * @author Koruk, Samet, 5869110
 * 
 */

public class ServerSenden extends Thread {

	/**
	 * Attributblock
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public Socket clientSocket;
	public ObjectOutputStream out;
	private LinkedBlockingQueue<Message> outputqueue = new LinkedBlockingQueue<Message>();
	public boolean truth = true;

	public ServerSenden(Socket clientSocket, int count, LinkedBlockingQueue<Message> blockQ) {

		this.outputqueue = blockQ;

		try {
			out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			out.flush();
			System.out.println("Sendthread gestartet!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sendthread konnte nicht gestartet werden");
		}
	}

	@Override
	public void run() {
		while (truth) {
			checkMes();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Methode, die ueberprueft, ob Queue leer ist und wenn nicht dann senden
	 * der Nachricht
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public void checkMes() {
		if (outputqueue.isEmpty() != true)
			sendMes();
	}

	/**
	 * Methode, die Nachrichten aus der Queue in Stream schreibt
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public void sendMes() {

		try {
			// zieht sich die Nachricht aus der Queue und schickt Sie weiter
			out.writeObject(outputqueue.poll());
			out.flush();
			// out.reset();

		} catch (IOException e) {
			System.out.println("Die Nachricht konnte nicht gesendet werden" + e);
		}

	}

	public LinkedBlockingQueue<Message> outputqueue() {

		return outputqueue;
	}

}
