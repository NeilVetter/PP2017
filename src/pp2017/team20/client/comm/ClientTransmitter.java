package pp2017.team20.client.comm;

import java.io.EOFException; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import pp2017.team20.shared.*;

/**
 * 
 * Die ReceiverClient Klasse baut den Thread zum Senden der Nachrichten zum Server auf.
 * Der Thread erlaubt das Schreiben von Daten in einem InputStream. Die zu sendenden Nachrichten
 * werden in einer LinkedBlockingQueue gesammelt und eine Schleife erlaubt das ständige Abfragen der
 * Nachrichten und schreibt diese in eienm OutputStream.
 * 
 * 
 * @author Yuxuan Kong 6019218
 * 
 */
public class ClientTransmitter extends Thread {

	private Socket server;
	// Zum Senden von Message-Objekten
	private ObjectOutputStream out;
	// Sammelt die Nachrichten, die zum Server gesendet sollen
	private LinkedBlockingQueue<Message> messagesToServer = new LinkedBlockingQueue<>();
	private Message messageTS;

	/**
	 * Initialisiert den Socket 'server' 
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param server
	 *            definiert den Socket des Clienten
	 */
	public ClientTransmitter(Socket server) {
		this.server = server;
	}

	/**
	 * 
	 * Baut eine Instanz des ObjectInputStream, der Nachrichten vom LBQueue in den 
	 * ObjectOutputStream schreibt. Dabei wird eine While-Schleife genommen, damit 
	 * diese ständig neue Nachrichten abfragt. 
	 * 
	 * 
	 * @author Yuxuan Kong
	 */
	private void transmitMessage() {
		try {

			out = new ObjectOutputStream(server.getOutputStream());
			while (true) {
	
				messageTS = messagesToServer.poll(100, TimeUnit.MILLISECONDS);
				if (messageTS != null) {
					// Schreibt Nachrichten in einem OutputStream
					out.writeObject(messageTS);
					out.flush();
					out.reset();
				}
			}
		} catch (EOFException e) {
			System.out.println("ERROR ObjectOutputStream: TRANSMITTERCLIENT");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("ERROR SocketException: TRANSMITTERCLIENT");
			e.printStackTrace();
		} catch (IOException | InterruptedException e) {
			System.out.println("ERROR: TRANSMITTERCLIENT resultiert in transmitMessage()");
			e.printStackTrace();
		} finally {
			try {
				System.out.println("OutputStream ist geschlossen: TRANSMITTERCLIENT");
				//Closing the output stream
				this.out.close();
			} catch (IOException e) {
				System.out.println("ERROR: TRANSMITTERCLIENT in transmitMessage()");
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
		transmitMessage();
	}

	/**
	 * Legt die Nachrichten in den LBQueue 'messagestoServer', damit die später
	 * mit der transmitMessage() Methode zum Server gesendet werden können.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	public void writeMessage(Message message) {
		try {
			//Legt die Nachrichten in den LBQueue
			this.messagesToServer.put(message);
		} catch (InterruptedException e) {
			System.out.println("ERROR: TRANSMITTERCLIENT.writeMessage(Message message)");
			e.printStackTrace();
		}
	}

	/**
	 * Gibt die LinkedBlockingQueue 'messagesToServer', die die Nachrichten zum Server, sammeln
	 * 
	 * @author Yuxuan Kong 6019218
	 * 
	 * @return the LinkedBlockingQueue<Message>
	 */
	public LinkedBlockingQueue<Message> getQueueMessagesToServer() {
		return messagesToServer;
	}

}
