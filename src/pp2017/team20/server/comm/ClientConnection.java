package pp2017.team20.server.comm;

import java.io.IOException; 
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.*;

/**
 * Klasse fuer die einzelnen Clients, die in einzelnen Threads gehandelt werden
 * und es werden ID`s gesetzt
 * 
 * @author Koruk, Samet, 5869110
 * 
 */

public class ClientConnection extends Thread {

	/**
	 * Attributblock
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public int ClientID;
	private ServerSenden sendThread;
	private ServerEmpfangen receiveThread;
	public Socket clientSocket;

	/**
	 * Konstruktor fuer die Klasse, wo die inputqueue uebergeben wird ausserdem
	 * werden die Send- und Receive Threads gestartet
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public ClientConnection(Socket clientSocket, LinkedBlockingQueue<Message> blockQ, int count, LinkedBlockingQueue<Message> blockQout) {

		this.ClientID = count;
		this.clientSocket = clientSocket;
		sendThread = new ServerSenden(clientSocket, count, blockQout);
		sendThread.start();
		receiveThread = new ServerEmpfangen(clientSocket, blockQ, count);
		receiveThread.start();

	}

	/**
	 * Methode, die aufgerufen wird, wenn man eine Nachricht versenden will (auf
	 * Server Seite)
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public void sendMessagetoClient(Message neueNachricht) {
		try {
			/**
			 * entsprechender Thread des Clients wird aufgerufen und Nachricht
			 * in Queue eingefuegt
			 */
			sendThread.outputqueue().put(neueNachricht);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("<ServerKomm> Nachricht konnte nicht in Queue eingefuegt werden.");
		}
	}
	/**
	 * Methode, um Nachricht nicht mehr an den Client zu senden
	 * 
	 * @author Koruk, Samet, 5869110
	 */
	public void stopSendThread() {
		sendThread.truth = false;
		receiveThread.clientisok = false;
		try {
			sendThread.out.close();
			receiveThread.in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Closen fehlgehschlagen");

		}

	}
}
