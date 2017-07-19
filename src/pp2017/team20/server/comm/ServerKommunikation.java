package pp2017.team20.server.comm;

/**
 *   Klasse, um den Austausch mit dem Server zu gestalten
 * @author Koruk, Samet, 5869110
 */

import java.io.IOException; 

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;
/**
 * Klasse, um den Austausch mit den Server zu gestalten
 * 
 * 
 */
public class ServerKommunikation extends Thread {
	/**
	 * Attributblock
	 * 
	 */
	public static LinkedBlockingQueue<Message> inputqueue = new LinkedBlockingQueue<Message>();
	public static LinkedBlockingQueue<Message> outputqueue = new LinkedBlockingQueue<Message>();
	public static ArrayList<ClientConnection> ClientConnections = new ArrayList<ClientConnection>();
	public int count = 0;

	private ServerSocket newSocket;

	/**
	 * Konstruktor fuer die Klasse
	 * 
	 * 
	 */
	public ServerKommunikation() {

		try {

			/**
			 * hier wird der ServerSocket eingerichtet mit dem Port ueber den er
			 * erreichbar ist
			 */

			newSocket = new ServerSocket(2000);
			System.out.println("Server gestartet!");

		} catch (IOException e) {

			System.out.println("Server konnte nicht gestartet werden" + e);
		}
		this.start();
	}

	@Override
	public void run() {
		while (true) {

			// durchlaufen der while-Schleife: horcht nach Verbindungen

			Socket clientSocket;
			try {

				clientSocket = newSocket.accept();
				// akzeptiert diese, wenn ein neuer Client da ist
				ClientConnection newClient = new ClientConnection(clientSocket, inputqueue, count, outputqueue);
				newClient.start();
				System.out.println("Es hat sich der Client mit der ID: " + count + " angemeldet");

			} catch (IOException e) {
				System.out.println("Verbindung zu Client fehlgeschlagen" + e);

			}

		}

	}

	/**
	 * Methode fuer das Senden von Nachrichten an die Clients
	 * 
	 */
	public void sendeNachricht(Message neueNachricht) {
		try {
			outputqueue.put(neueNachricht);
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gesendet werden" + e);
		}
	}

	/**
	 * Methode, um Nachrichten aus der Queue von den Clients an Server zu senden
	 * 
	 */
	public Message erhalteNachricht() {

		Message neueNachricht = null;

		try {
			neueNachricht = inputqueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 System.out.println(neueNachricht);

		return neueNachricht;

	}

}
