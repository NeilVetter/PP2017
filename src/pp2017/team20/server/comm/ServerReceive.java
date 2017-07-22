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
 * @author Kong, Yuxuan, 6019218
 * 
 */

public class ServerReceive extends Thread {

	public ObjectInputStream input;
	private BlockingQueue<Message> messageQueue;
	public boolean clientCheck = true;
	private int count;

	/**
	 * Konstruktor
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public ServerReceive(Socket clientSocket, BlockingQueue<Message> messageQueue, int count) {
		this.messageQueue = messageQueue;
		this.count = count;
		try {
			input = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			System.out.println("Receivethread wurde gestartet");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Receivethread konnte nicht gestartet werden.");
		}

	}

	@Override
	public void run() {

		while (clientCheck) {
			messageCheck();
		}
	}

	/**
	 * 
	 * Falls eine Nachricht im Stream ist, wird diese in die Queue geschickt.
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void messageCheck() {
		Message msg = null;
		try {

			while ((msg = (Message) input.readObject()) != null) {
				messageQueue.add(msg);

			}

		} catch (IOException e) {
			System.out.println("Achtung: Der Client mit der folgenden ID " + count + " ist nicht mehr da, die Verbindung wurde abgebrochen");

			clientCheck = false;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("CLient konnte nicht erreicht werden.");
		}

	}

}
