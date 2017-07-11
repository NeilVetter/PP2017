package pp2017.team20.server.comm;

import java.io.EOFException; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import pp2017.team20.shared.*;

/**
 * Die ReceiverClient Klasse baut den Thread zum Senden der Nachrichten zum Clienten auf.
 * Der Thread erlaubt das Schreiben von Daten in einem InputStream. Die zu sendenden Nachrichten
 * werden in einer LinkedBlockingQueue gesammelt und eine Schleife erlaubt das ständige Abfragen der
 * Nachrichten und schreibt diese in eienm OutputStream.
 * 
 * @author Yuxuan Kong 6019218
 * 
 */
public class ServerTransmitter extends Thread {

	
	private Socket client;
	
	private ObjectOutputStream out;
	
	private LinkedBlockingQueue<Message> messagesToClient = new LinkedBlockingQueue<>();
	// Speichert die zusendenden Nachrichten
	private Message messageTC;

	/**
	 * Initialisiert den Socket 'client' 
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param client
	 *            definiert den Socket des Clienten
	 */
	public ServerTransmitter(Socket client) {
		this.client = client;
	}

	/**
	 * 
	 * 
	 * Baut eine Instanz des ObjectInputStream, der Nachrichten vom LBQueue in den 
	 * ObjectOutputStream schreibt. Dabei wird eine While-Schleife genommen, damit 
	 * diese ständig neue Nachrichten abfragt. 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void transmitMessage() {
		try {
			
			out = new ObjectOutputStream(client.getOutputStream());
			while (true) {
				// Der Thread wartet bis die Nachrichten abgefragt werden können
				messageTC = messagesToClient.poll(200, TimeUnit.MILLISECONDS);
				if (messageTC != null) {
					// Schreibt eine Nachricht in einem OutputStream
					out.writeObject(messageTC);
					out.flush();
					out.reset();
				}
			}
		} catch (EOFException e) {
			System.out.println("ERROR ObjectOutputStream: TRANSMITTERSERVER");
//			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("ERROR SocketException: TRANSMITTERSERVER");
//			e.printStackTrace();
		} catch (IOException | InterruptedException e) {
			System.out.println("Client hat sich erfolgreich ausgeloggt");
//			e.printStackTrace();
		} finally {	
			try {
				System.out.println("OutputStream ist geschlossen: TRANSMITTERSERVER");
				
				this.out.close();
			} catch (IOException e) {
				System.out.println("Fehler: TRANSMITTERSERVER in transmitMessage()");
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
		this.transmitMessage();
	}

	/**
	 * Legt die Nachrichten in den LBQueue 'messagestoClient', damit die später
	 * mit der transmitMessage() Methode zum Clienten gesendet werden können.
	 *  
	 * @author Yuxuan Kong 6019218
	 */
	public void writeMessage(Message message) {
		try {
			
			this.messagesToClient.put(message);
		} catch (InterruptedException e) {
			System.out.println("Fehler: TRANSMITTERSERVER.writeMessage(Message message)");
			e.printStackTrace();
		}
	}

}
