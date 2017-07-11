package pp2017.team20.server.comm;

import java.io.EOFException;  
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.*;

/**
 * Die ReceiverServer Klasse baut den Thread zum empfangen der Nachrichten vom Clienten auf.
 * Der Thread erlaubt das Speichern von Daten aus einem InputStream. Die gespeicherten Nachrichten
 * werden in einer LinkedBlockingQueue gesammelt und eine Schleife erlaubt das ständige Lesen der
 * Nachrichten und speichert diese in einer Queue.
 * 
 * @author Yuxuan Kong 6019218
 * 
 */
public class ServerReceiver extends Thread {

	private ServerHandler networkHandler;
	
	private Socket client;
	
	private ObjectInputStream in;
	// Sammelt Nachrichten vom Client
	LinkedBlockingQueue<Message> messagesFromClient = new LinkedBlockingQueue<>();
	// Speichert Message-Objekte
	private Message messageFC;

	
	/**
	 * * Initialisiert den Socket 'client' und den HandlerServer 'networkHandler'
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param client
	 *            definiert Socket des Clienten
	 */
	public ServerReceiver(Socket client, ServerHandler networkHandler) {
		this.client = client;
		this.networkHandler = networkHandler;
	}

	/**
	 * 
	 * Baut eine Instanz des ObjectInputStream, der vom InputStream 
	 * liest und Daten in einer LBQueue speichert.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void receiveMessage() {
		try {
			in = new ObjectInputStream(client.getInputStream());
			while (true) {
				
				ThreadWaitForMessage.waitFor(100L);
				
				messageFC = (Message) in.readObject();
				this.networkHandler.setConnectedStatus1(true);
				this.networkHandler.setConnectedStatus2(true);
				if (messageFC != null) {
					messagesFromClient.put(messageFC);
				}
			}
		} catch (EOFException e) {
			System.out.println("Fehler ObjectInputStream: RECEIVERSERVER");
			e.printStackTrace();
		} catch (SocketException e) {
			
			this.networkHandler.close();
			System.out.println("Fehler SocketException: RECEIVERSERVER");
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.out.println("readObject() ERROR in RECEIVERSERVER.receiveMessage()");
			e.printStackTrace();
		} finally {
			try {
				System.out.println("InputStream wurde geschlossen: RECEIVERSERVER");
				//Schließt den InputStream
				this.in.close();
				} catch (IOException e) {
					System.out.println("ERROR: RECEIVERSERVER");
					e.printStackTrace();
			    	}
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
	 * 
	 * Gibt den LBQueue 'messagesFromClient' aus
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return the LinkedBlockingQueue, die die Nachrichten vom Clienten sammeln
	 */
	public LinkedBlockingQueue<Message> getMessagesFromClient() {
		return messagesFromClient;
	}

	/**
	 * 
	 * Gibt die Nachrichten, die abgefragt werden, aus
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return the Message-object from the LinkedBlockingQueue
	 *         'messagesFromClient'
	 */
	public Message getMessage() {
		return messagesFromClient.poll();
	}
}
