package pp2017.team20.server.comm;


import java.io.IOException;   
import java.util.concurrent.LinkedBlockingQueue;
import java.net.Socket;
import pp2017.team20.shared.*;

/**
 * Die Klasse ClientConnection handelt die einzelnen Threads ab und setzt die IDs
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */

public class ClientConnection extends Thread {

	/**
	 * Attributblock
	 * 
	 * @author Kong, Yuxuan, 6019218
	 * 
	 */
	public int ClientID;
	private ServerSend sendThread;
	private ServerReceive receiveThread;
	public Socket clientSocket;

	/**
	 * Der Konstruktor der Klasse ClientConnection übergibt den InputQueue und SendThread und ReceiveThread
	 * werden gestartet
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public ClientConnection(Socket clientSocket, LinkedBlockingQueue<Message> messageQueue, int count, LinkedBlockingQueue<Message> messageOutQueue) {

		this.ClientID = count;
		this.clientSocket = clientSocket;
		receiveThread = new ServerReceive(clientSocket, messageQueue, count);
		receiveThread.start();
		sendThread = new ServerSend(clientSocket, count, messageOutQueue);
		sendThread.start();
	}

	/**
	 * Stoppt das Senden der Nachrichten an den Clienten
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void stopSendThread() {
		receiveThread.clientCheck = false;
		sendThread.truth = false;
		try {
			receiveThread.input.close();
			sendThread.output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Schließen fehlgehschlagen");

		}

	}


	/**
	 * 
	 * Eine Methode, die beim Senden einer Nachricht zum Clienten aufgerufen wird
	 *
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void sendMessagetoClient(Message message) {
	try {
		
		 // Nachrichten werden in die Queue eingefuegt und Threads des Clienten 
		 //werden aufgerufen
		sendThread.outputqueue().put(message);
	} catch (InterruptedException e) {
		System.out.println("Nachricht konnte nicht in die Queue eingefuegt werden.");
	}
	}
}