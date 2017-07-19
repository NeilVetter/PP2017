package pp2017.team20.server.comm;

import java.io.BufferedInputStream; 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Die Threads zum Erhalten von Nachrichten
 * 
 * @author Koruk, Samet, 5869110
 * 
 */

public class ServerEmpfangen extends Thread {

	public ObjectInputStream in;
	private BlockingQueue<Message> blockQ;
	public boolean clientisok = true;
	private int count;

	/**
	 * Konstruktor
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public ServerEmpfangen(Socket clientSocket, BlockingQueue<Message> blockQ, int count) {
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
			checkMes();
		}
	}

	/**
	 * Methode die ueberprueft, wenn eine Nachricht im Stream ist, diese in die
	 * Queue zu schicken
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public void checkMes() {
		Message msg = null;
		try {

			while ((msg = (Message) in.readObject()) != null) {
				blockQ.add(msg);

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
