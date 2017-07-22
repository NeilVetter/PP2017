package pp2017.team20.server.comm;


import java.io.IOException; 
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;
/**
 * Klasse, um mit dem Server zu kommunizieren
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */
public class ServerCommunication extends Thread {
	/**
	 * Attributblock
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public static LinkedBlockingQueue<Message> inputMessage = new LinkedBlockingQueue<Message>();
	public static LinkedBlockingQueue<Message> outputMessage = new LinkedBlockingQueue<Message>();
	public static ArrayList<ClientConnection> ClientConnections = new ArrayList<ClientConnection>();
	public int count = 0;

	private ServerSocket newSocket;

	/**
	 * Konstruktor fuer die Klasse
	 * 
	 * @author Kong, Yuxuan, 6019218
	 * 
	 */
	public ServerCommunication() {

		try {

			
			 // hier wird der ServerSocket eingerichtet mit dem Port ueber den er
			 // erreichbar ist
			newSocket = new ServerSocket(2000);
			System.out.println("Server wurde gestartet!");

		} catch (IOException e) {

			System.out.println("Server konnte nicht gestartet werden" + e);
		}
		this.start();
	}

	@Override
	public void run() {
		while (true) {

			// While-Schleife: sucht nach Verbindungen
			Socket clientSocket;
			try {

				clientSocket = newSocket.accept();
				ClientConnection newClient = new ClientConnection(clientSocket, inputMessage, count, outputMessage);
				newClient.start();
				System.out.println("Client mit der folgenden ID: " + count + " angemeldet");

			} catch (IOException e) {
				System.out.println("Verbindung zum Client fehlgeschlagen" + e);

			}

		}

	}

	/**
	 * Methode, die das Senden der Nachrichten an den Clienten ermöglicht
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void sendMessage(Message message) {
		try {
			outputMessage.put(message);
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht an den Clienten gesendet werden" + e);
		}
	}

	/**
	 * Methode, die es erlaubt, Nachrichten vom Clienten zu erhalten
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public Message receiveMessage() {

		Message message = null;

		try {
			message = inputMessage.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return message;

	}

}
