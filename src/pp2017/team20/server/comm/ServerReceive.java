package pp2017.team20.server.comm;

import java.io.BufferedInputStream; 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Threads um Nachrichten vom Clienten zu erhalten
 * 
 * @author Yuxuan Kong, 6019218
 * 
 */

public class ServerReceive extends Thread {

	public ObjectInputStream in;
	private BlockingQueue<Message> blockQ;
	public boolean clientisok = true;
	private int count;

	/**
	 * Konstruktor
	 * 
	 * @author Yuxuan Kong, 6019218
	 */
	public ServerReceive(Socket clientSocket, BlockingQueue<Message> blockQ, int count) {
		this.blockQ = blockQ;
		this.count = count;
		try {
			in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			System.out.println("Receivethread gestartet");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Receivethread nicht gestartet.");
		}

	}

	@Override
	public void run() {

		while (clientisok) {
			checkMessage();
		}
	}

	/**
	 * Methode die ueberprueft, wenn eine Nachricht im Stream ist, diese in die
	 * Queue zu schicken
	 * 
	 * @author Yuxuan Kong, 6019218
	 */
	public void checkMessage() {
		Message message = null;
		try {

			while ((message = (Message) in.readObject()) != null) {
				blockQ.add(message);

			}

		} catch (IOException e) {
			System.out.println("Client mit der ID " + count + " ist nicht mehr da, die Verbindung wurde abgebrochen");

			clientisok = false;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("CLient nicht erreichbar.");
		}

	}

}
