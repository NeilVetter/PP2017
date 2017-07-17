package pp2017.team20.client.comm;

import java.io.BufferedInputStream; 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Die Threads zum Erhalten von Nachrichten
 * 
 * @author Samet, Koruk, 5869110
 * 
 */

public class ClientEmpfangen extends Thread {
	/**
	 * Attributeblock
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	
	private ObjectInputStream in;
	private BlockingQueue<Message> blockQ;
	private boolean serverisok = true;

	/**
	 * Konstruktor fuer die Klasse, wo die Queue uebergeben wird und Stream
	 * erstellt
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	public ClientEmpfangen(Socket clientSocket, BlockingQueue<Message> blockQ) {
		
		this.blockQ = blockQ;
		try {
			System.out.println("Client ReceiveThread gestartet");
			in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client Receivethread nicht gestartet.");
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

		while (serverisok) {
			checkMes();
		}
	}

	/**
	 * Methode, um zu pruefen, falls Nachricht im Stream ist und wenn ja, in die
	 * Queue einfuegen
	 * 
	 * @author Samet, Koruk, 5869110
	 */

	public void checkMes() {
		Message msg = null;
		try {
			while ((msg = (Message) in.readObject()) != null) {
				blockQ.add(msg);
			}

		} catch (IOException e) {
			System.out.println("Server nicht mehr erreichbar bitte einmal Neustarten " + e);
			serverisok = false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Server nicht erreichbar.");
		}

	}

}
