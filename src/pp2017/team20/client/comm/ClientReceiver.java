package pp2017.team20.client.comm;

import java.io.EOFException; 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.*;

/**
 * 
 * Die ReceiverClient Klasse baut den Thread zum empfangen der Nachrichten vom Server auf.
 * Der Thread erlaubt das Speichern von Daten aus einem InputStream. Die gespeicherten Nachrichten
 * werden in einer LinkedBlockingQueue gesammelt und eine Schleife erlaubt das ständige Lesen der
 * Nachrichten und speichert diese in einer Queue.
 * 
 * 
 * @author Yuxuan Kong 6019218
 * 
 */
public class ClientReceiver extends Thread {

	// Verbindungsstatus nach dem Empfangen der Nachrichten
	private ClientHandler networkHandler;
	// Sammelt die vom Server empfangenen Nachrichten
	private LinkedBlockingQueue<Message> messagesFromServer = new LinkedBlockingQueue<>();
	
	private Socket server;
	// Für das Lesen der Nachrichten aus dem InputStream
	private ObjectInputStream in;
	// Für das speichern der Nachrichten
	private Message messageFS;

	/**
	 * Initialisiert den Socket 'server' und den HandlerClient 'networkHandler'
	 * 
	 * @author Yuxuan Kong 6019218
	 * 
	 * @param server
	 *            Definiert den Socket für den Client
	 * 
	 */
	public ClientReceiver(Socket server, ClientHandler networkHandler) {
		this.server = server;
		this.networkHandler = networkHandler;
	}

	/**
	 * Baut eine Instanz des ObjectInputStream, der vom InputStream des Client-Sockets
	 * liest und Daten in einer LBQueue speichert.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void receiveMessage() {
		try {

			in = new ObjectInputStream(server.getInputStream());
			while (true) {
				// Thread wartet bis die Nachrichten lesbar sind
				ThreadWaitForMessage.waitFor(100L);
				messageFS = (Message) in.readObject();
				// System.out.println(messageFS.toString());

				//Setze die Variablen connectedState1 und connectedState2 auf true, 
				//damit die Verbindung zwischen Server und Client steht.
				//Der PingCheckClient checkt ob der Inputstream lesbar ist
				networkHandler.setConnectedStatus1(true);
				networkHandler.setConnectedStatus2(true);
				if (messageFS != null) {
					// Speichert die Nachrichten in die Queue messagesFromServer
					messagesFromServer.put(messageFS);
				}
			}
		} catch (EOFException e) {
			System.out.println("ERROR ObjectInputStream: RECEIVERCLIENT");
			e.printStackTrace();
		} catch (SocketException e) {
			// Schließt Verbindung zwischen Server und Client
			this.networkHandler.close("Verbindung zum Server abgebrochen");
			System.out.println("ERROR SocketException: RECEIVERCLIENT");
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("InputStream wurde geschlossen: RECEIVERCLIENT");
				// Schließt den InputStream
				this.in.close();
			} catch (IOException e) {
				System.out.println("ERROR: RECEIVERCLIENT");
				e.printStackTrace();
			}
			// Beendet den laufenden Java Virtual Machine
			System.exit(1);
		}
	}

	/**
	 * @author Yuxuan Kong 6019218
	 */
	@Override
	public void run() {
		this.receiveMessage();
	}

	/**
	 * Gibt den LinkedBlockingQueue 'messagesFromServer' zurück
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return LinkedBlockingQueue<Message>, die Nachrichten vom Server sammelt
	 */
	public LinkedBlockingQueue<Message> getMessagesFromServer() {
		return messagesFromServer;
	}

	/**
	 * Gibt die abgefragte Nachricht von der Queue 'messagesFromServer'zurück
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return Message-object von der LinkedBlockingQueue
	 *         'messagesFromServer'
	 */
	public Message getMessage() {
		return messagesFromServer.poll();
	}

}
