package pp2017.team20.server.comm;

import java.io.IOException; 
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

import pp2017.team20.shared.*;

/**
 * Die HandlerServer Klasse ist für den Aufbau des Server-Sockets und der Verbindung 
 * zum Clienten verantwortlich. Außerdem stellt die Klasse Methoden für das Senden und Empfangen 
 * von Nachrichten für den Server-Engine zur Verfügung. 
 * 
 * @author Yuxuan Kong 6019218
 * 
 */
public class ServerHandler {

	// Definiert den Serverport
	private ServerSocket server;
	// Verbindet Client-Socket
	private Socket client;
	// Führt TimerTask aus
	private Timer pingTimer;
	// Startet receiving Thread
	private ServerReceiver receiver;
	// Startet sending Thread
	private ServerTransmitter transmitter;
	// Verbindungsstatuts zwischen Server und Client
	private boolean connected;

	private boolean closeNetwork;
	
	private boolean connectedStatus1;
	
	private boolean connectedStatus2;

	/**
	 * 
	 * Führt die init()-Methode aus, s.d. die Verbindung zwischen Server und Client aufgebaut werden kann.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	public ServerHandler() {
		init();
	}

	/**
	 *
	 * Erzeugt den Server-Socket für den Server und startet Threads um Nachrichten 
	 * mithilfe von ReceiverServer und TransmitterServer zu empfangen bzw. 
	 * zu senden. Der TimerTask wird ebenfalls gestartet.
	 * 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	public void init() {

		pingTimer = new Timer();
		this.connectedStatus1 = true;
		this.connectedStatus2 = true;
		this.closeNetwork = false;

		try {
			server = new ServerSocket(55555);
			while (!connected) {
				
				client = server.accept();
				// Startet die Threads und den TimerTask
				if (client.isConnected()) {
					receiver = new ServerReceiver(client, this);
					transmitter = new ServerTransmitter(client);
					receiver.start();
					transmitter.start();
					
					pingTimer.scheduleAtFixedRate(new ServerPing(this), 2000, 2000);
					connected = true;
				}

			}
		} catch (IOException e) {
			System.out.println("ERROR: HANDLERSERVER PORT NOT FOUND OR OCCUPIED");
			e.printStackTrace();
			
			System.exit(1);
		}
	}

	/**
	 * 
	 * Gibt den Socket 'client', der mit dem ServerSocket verbinden ist, zurück
	 * 
	 * @author Yuxuan Kong 6019218
	 * 
	 */
	public Socket getClient() {
		return this.client;
	}

	/**
	 * Gibt den ServerSocket 'server' zurück.
	 * 
	 * @author Yuxuan Kong 6019218
	 * 
	 */
	public ServerSocket getServer() {
		return this.server;
	}

	/**
	 * 
	 * Sendet Nachrichten an den Clienten mithilfe der writeMessage-Methode
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param message
	 *            definiert die Nachricht, die mit dem ObjectOutputStream an den Clienten geschickt werden soll
	 */
	public void sendMessageToClient(Message message) {
		// Schreibt die Nachricht in einem ObjectOutputStream
		transmitter.writeMessage(message);
	}

	
	
	/**
	 * 
	 * Gibt die Nachrichten aus, die vom Clienten geschickt werden
	 *
	 * @author Yuxuan Kong 6019218
	 * @return Message-object, das vom ObjectInputStream gelesen wird
	 */
	public Message getMessageFromClient() {
		
			return receiver.getMessage();
	
	}


	
	/**
	 * 
	 * Schließt den Socket, den Timertask und die Anwendung. Diese Methode erlaubt
	 * das Schließen der Verbindung zwischen Server und Client. Startet dann wieder
	 * die Verbindung.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	public void close() {

		try {
			System.out.println("CLOSED: HandlerServer");
			// Beendet den Timer
			this.pingTimer.cancel();
			this.setConnected(false);
			// Beendet den Thread für Nachrichten empfangen
			this.receiver.interrupt();
			// Beendet den Thread für Nachrichten senden
			this.transmitter.interrupt();
			// Schließt den Socket
			this.client.close();
			// Schließt den Server-Socket
			this.server.close();
			// Startet eine Verbindung mit einem Client
			this.init();
			
		} catch (IOException e) {
			System.out.println("ERROR: HANDLERSERVER");
			e.printStackTrace();
		}
	}

	
	/**
	 * Die folgenden Methoden sind ebenfalls wieder normale Getter und Setter, die nicht
	 * ausführlich erklärt werden müssen
	 * 
	 * @author Yuxuan Kong 6019218
	 * 
	 */

	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean getConnected() {
		return this.connected;
	}

	public void setCloseNetwork(boolean closeNetwork) {
		this.closeNetwork = closeNetwork;
	}

	public boolean getCloseNetwork() {
		return this.closeNetwork;
	}

	public void setConnectedStatus1(boolean connectedStatus1) {
		this.connectedStatus1 = connectedStatus1;
	}

	public boolean getConnectedStatus1() {
		return this.connectedStatus1;
	}

	public void setConnectedStatus2(boolean connectedStatus2) {
		this.connectedStatus2 = connectedStatus2;
	}

	public boolean getConnectedStatus2() {
		return this.connectedStatus2;
	}

}