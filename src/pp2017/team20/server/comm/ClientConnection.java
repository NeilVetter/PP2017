package pp2017.team20.server.comm;

import java.io.IOException; 
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.*;



public class ClientConnection extends Thread {


	public int ClientID;
	private ServerSenden sendThread;
	private ServerEmpfangen receiveThread;
	public Socket clientSocket;

	/**
	 * Konstruktor fuer die Klasse, wo die inputqueue uebergeben wird ausserdem
	 * werden die Send- und Receive Threads gestartet
	 * 
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
