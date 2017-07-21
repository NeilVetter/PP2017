package pp2017.team20.client.comm;

import java.io.IOException;  
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Herzschlagnachricht;
import pp2017.team20.shared.Message;

/**
 * Eine Klasse mit der der Client mit dem Server kommunizieren kann. Hier werden die einzelnen
 * Threads abgehandelt
 * 
 * @author Yuxuan Kong, 6019218
 * 
 */
public class ClientCommunication extends Thread {
	/**
	 * Attributblock
	 */
	public static LinkedBlockingQueue<Message> outputQueue = new LinkedBlockingQueue<Message>();
	public static LinkedBlockingQueue<Message> inputQueue = new LinkedBlockingQueue<Message>();
	public Socket clientSocket;

	public boolean connectToServer() {
		boolean successful = false;
		try {
			// Socket wird erstellt
			InetAddress ServerAddress = InetAddress.getByName("localhost");
			clientSocket = new Socket(ServerAddress, 2000);
			System.out.println("Client wurde gestartet!");

			// Threads werden erstellt und gestartet

			ClientReceive receiveThread = new ClientReceive(clientSocket, inputQueue);
			receiveThread.start();
			ClientSend sendThread = new ClientSend(clientSocket, outputQueue);
			sendThread.start();
			Thread.sleep(100);

			successful = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitte richtige Serveradresse eingeben.");
			successful = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitte Versuchen Sie sich erneut mit dem Server zu verbinden.");
			successful = false;
		}

		return successful;
	}

	@Override
	public void run() {
		// Ich-bin-noch-da Nachricht fuer Server
		while (true) {
			//Herzschlagnachricht binMsg = new Herzschlagnachricht();
			//sendeNachricht(binMsg);
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
	 * @author Yuxuan Kong, 6019218
	 */
	public void sendMessage(Message message) {

		try {
			outputQueue.put(message);
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gesendet werden" + e);
		}
	}

	/**
	 * Methode, um Nachrichten vom Server zu empfangen eher sie von der Queue zu
	 * holen, wo Sie gespeichert sind
	 * 
	 * @author  Yuxuan Kong, 6019218
	 */
	public Message receiveMessage() {

		Message message = null;
		try {
			while (!inputQueue.isEmpty()) {
				message = inputQueue.take();
			}
		} catch (InterruptedException e) {
			System.out.println("Nachricht konnte nicht gelesen werden " + e);
		}
		return message;

	}

}
