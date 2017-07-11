package pp2017.team20.client.comm;

import java.io.IOException;  
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.*;


/**
 * 
 * Die HandlerClient Klasse ist für den Aufbau des Client-Sockets und der Verbindung 
 * zum Server verantwortlich. Außerdem stellt die Klasse Methoden für das Senden und Empfangen 
 * von Nachrichten für den Client-Engine zur Verfügung. 
 * 
 * @author Yuxuan Kong 6019218
 * 
 */

public class ClientHandler {
	// Sammelt die Nachrichten, die zum Server geschickt werden, in einer Queue
	public LinkedBlockingQueue<Message> outputQueue = new LinkedBlockingQueue<>();
	// Verbindung mit dem Server-Port
	private Socket server;
	// Erhalte Nachrichten mit Threads
	private ClientReceiver receiver;
	// Sende Nachrichten mit Threads
	private ClientTransmitter transmitter;
	// Executes the TimerTask
	private Timer pingTimer;
	// Verbindungen zum Socket schließen
	private boolean closeNetwork;
	// Verbindungsstatus
	private boolean connectedStatus1;
	// Verbindungsstatus
	private boolean connectedStatus2;

	/**
	 * 
	 * Erzeugt den Socket für den Client und startet Threads um Nachrichten 
	 * mithilfe von ReceiverClient und TransmitterClient zu empfangen bzw. 
	 * zu senden
	 * 
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param adresse
	 *            definiert Serveradresse
	 */
	public ClientHandler(String adresse) {
		this.pingTimer = new Timer();
		this.closeNetwork = false;
		
		//Verbindung mit dem ServerSocket
		while (this.server == null) {
			try {
				this.server = new Socket(adresse, 55555);
				this.connectedStatus1 = true;
				this.connectedStatus2 = true;
			} catch (UnknownHostException e) {
				System.out.println("Fehler ClientHandler");
				e.printStackTrace();
			} catch (IOException e) {
				// Falls die Verbindung zum Server fehlschlaegt
				System.out.println("ClientHandler: Server nicht erreichbar");
				e.printStackTrace();
				System.out.println(
						"Verbindungsaufbau zum Server nicht möglich! \n\n Prüfen Sie bitte : \n 1. Wurde der Server gestartet? \n 2. Folgt der Client der richtigen Serveradresse? \n 3. Stimmen Server-Port und Client-Port überein? \n\n Starten Sie das Spiel danach!");
				System.exit(0);
			}
		}
		// Startet Threads und den TimerTask
		runComp();
	}

	/**
	 * Startet Threads und TimerTask. Initialisiert ReceiverClient und TransmitterClient. 
	 * Der TimerTask wird ebenfalls gestartet um MessPing Nachrichten an den Server zu schicken
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void runComp() {
		System.out.println("HandlerClient.startComp()");
		// Initialisiert Thread um Nachrichten zu senden und empfangen
		transmitter = new ClientTransmitter(server);
		receiver = new ClientReceiver(server, this);
		transmitter.start();
		receiver.start();
		// Der Timer 'pingTimer' führt den TimerTask-PingCheckClient innerhalb eines bestimmten Intervalls aus
		this.pingTimer.scheduleAtFixedRate(new ClientPing(this), 2000, 2000);
	}


	/**
	 * Sendet Nachrichten zum Server durch den TransmitterClient-Thread
	 * 
	 * @author Yuxuan Kong, 6019218
	 * @param message
	 *            definiert die Nachricht, die in dem ObjectOutputStream geschrieben und zum Server geschickt wird
	 *           
	 */
	public void sendMessageToServer(Message message) {
		// Schreibt Nachrichten in einem ObjectOutputStream
		transmitter.writeMessage(message);
	}

	/**
	 * Empfange Nachrichten des Servers durch den ReceiverClient-Thread
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	public Message getMessageFromServer() {
		// Bekommt Nachrichten von der LinkedBlockingQueue 'messagesFromServer'
		return receiver.getMessage();
	}

	/**
	 * Gibt den LinkedBlockingQueue zurück. der die gesammelten Nachrichten, die zum Server
	 * geschickt werden sollen, ausgibt.
	 * Ermöglicht ein effizientes Testen der Sende-Operation
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return the LinkedBlockingQueue<Message>, die Nachrichten, die zum Server geschickt werden sollen, sammelt 
	 */
	public LinkedBlockingQueue<Message> getOutputQueue() {
		return this.outputQueue = transmitter.getQueueMessagesToServer();
	}
	
	
	/**
	 * Schließt den Socket, TimerTask und stoppt die Anwendung. Diese Methode erlaubt es einen,
	 * die Verbindung zwischen Server und Client zu trennen. Schließt den Socket des Clienten.
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param errorMessage
	 *            definiert Error-Message
	 */
	public void close(String errorMessage) {
		try {
			System.out.println("Geschlossen: HandlerClient");
			// Bricht den Timer ab
			this.pingTimer.cancel();
			// Schließt Socket
			this.getServer().close();
			System.out.println(errorMessage);
			// Beendet den laufenden Java Virtual Machine
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Geschlossen: HandlerClient.close(String errorMessage)");
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Gibt die Instanz der TransmitterClient zurück
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return Instanz TransmitterClient
	 */
	public ClientTransmitter getTransmitterC() {
		return this.transmitter;
	}

	/**
	 * Gibt die Instanz der ReceiverClient zurück
	 * 
	 * @author Yuxuan Kong 6019218
	 * @return Instanz ReceiverClient
	 */
	public ClientReceiver getReceiverC() {
		return this.receiver;
	}

	/**
	 * Die anschließenden Methoden sind normale Getter und Setter, die keine weitere 
	 * Erläuterungen benötigen
	 * 
	 * @author Yuxuan Kong 6019218
	 */

	
	public void setCloseNetwork(boolean closeNetwork) {
		this.closeNetwork = closeNetwork;
	}

	
	public boolean getCloseNetwork() {
		return this.closeNetwork;
	}

	public Socket getServer() {
		return this.server;
	}

	
	public void setServer(Socket server) {
		this.server = server;
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
