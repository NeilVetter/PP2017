package pp2017.team20.client.comm;

import java.io.IOException; 
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Herzschlagnachricht;
import pp2017.team20.shared.Message;

/**
 * Klasse fuer den Client, die in einzelnen Threads gehandelt wird
 * 
 * @author Koruk, Samet, 5869110
 * 
 */
public class ClientKommunikation extends Thread {
	/**
	 * Attributblock
	 */
	public static LinkedBlockingQueue<Message> outputqueue = new LinkedBlockingQueue<Message>();
	public static LinkedBlockingQueue<Message> inputqueue = new LinkedBlockingQueue<Message>();
	public Socket clientSocket;

	public boolean connectToServer() {
		boolean successful = false;
		try {
			// Socket wird erstellt
			InetAddress ServerIp = InetAddress.getByName("localhost");
			clientSocket = new Socket(ServerIp, 2000);
			System.out.println("Client gestartet!");

			// Threads werden erstellt und gestartet
			ClientSenden sendThread = new ClientSenden(clientSocket, outputqueue);
			sendThread.start();
			ClientEmpfangen receiveThread = new ClientEmpfangen(clientSocket, inputqueue);
			receiveThread.start();
			Thread.sleep(50);

			successful = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitte geben Sie die Serveradresse richtig an.");
			successful = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitte verbinden Sie sich erneut.");
			successful = false;
		}

		return successful;
	}

	@Override
	public void run() {
		// Ich-bin-noch-da Nachricht fuer Server
		while (true) {
			Herzschlagnachricht binMsg = new Herzschlagnachricht();
			sendeNachricht(binMsg);
			try {
				// alle 30 Sekunden
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Methode, um eine Nachricht vom Client zum Server zu senden
	 * 
	 * @author Samet, Koruk, 5869110
	 * @param neueNachricht
	 */
	public void sendeNachricht(Message neueNachricht) {

		try {
			outputqueue.put(neueNachricht);
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gesendet werden" + e);
		}
	}

	/**
	 * Methode, um Nachrichten vom Server zu empfangen eher sie von der Queue zu
	 * holen, wo Sie gespeichert sind
	 * 
	 * @author Samet, Koruk, 5869110
	 */
	public Message erhalteNachricht() {

		Message neueNachricht = null;
		try {
			while (!inputqueue.isEmpty()) {
				neueNachricht = inputqueue.take();
			}
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gelesen werden " + e);
		}
		return neueNachricht;

	}

}
