package pp2017.team20.client.comm;


import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.TimerTask;

import pp2017.team20.shared.*;

/**
 *
 * Die Klasse PingCheckClient baut den TimerTask zum Senden einer Nachricht zum  
 * Server innerhalb eines bestimmten Intervalls. Die Verbindung zwischen Server und
 * Client wird durch das ständige Senden von Nachrichten an den Server geprüft. Falls
 * die Nachrichten nicht gelesen werden können, wird der PingCheckClient die Verbindung
 * zum Server nach einer gewissen Zeit schließen.
 * 
 * 
 * @author Yuxuan Kong 6019218
 */
public class ClientPing extends TimerTask {

	//Um Ping-Nachrichten zu senden
	private ClientHandler networkHandler;
	// Zählt die gesendeten Ping-Nachrichten
	private int pingIteration = 0;

	/**
	 * Initialisiert die Instanz des HandlerClient 'networkHandler'
	 * 
	 * @author Yuxuan Kong
	 * @param networkHandler
	 *            definiert den HandlerClient, der den TimerTask ausführt
	 */
	public ClientPing(ClientHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	/**
	 * Führt den TimerTask aus. Sendet eine MessPing-Nachricht an den
	 * Server um eine Rückmeldung zu bekommen. Schließt den Client-Socket,
	 * wenn keine Nachrichten lesbar sind. Startet 2 Versuche um die Nachrichten
	 * zu lesen. 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	@Override
	public void run() {
		//Zählt die Ping-Nachrichten
		pingIteration++;
		if (this.networkHandler.getConnectedState1() && this.networkHandler.getConnectedState2()) {
			// Sendet den ersten Versuch
			pingOne();
		} else if (!this.networkHandler.getConnectedState1() && this.networkHandler.getConnectedState2()) {
			// Sendet den zweiten Versuch
			pingTwo();
		} else if (!this.networkHandler.getConnectedState1() && !this.networkHandler.getConnectedState2()) {
			// Schließt die Verbindung nach zwei fehlgeschlagenen Versuchen
			stopConnection();
		}
	}

	/**
	 * 
	 * Sendet eine MessPing-Nachricht an den Server. Setzt den 'connectedState1' des
	 * HandlerClient auf false um zu checken, ob der ReceiverClient die Nachricht vom
	 * InputStream gelesen hat und ändert den Wert dann wieder auf true. 
	 * 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void pingOne() {

		this.networkHandler.setConnectedState1(false);
		this.networkHandler.sendMessageToServer(new MessPing(100, 0));
	}

	/**
	 * 
	 * Sendet eine MessPing-Nachricht an den Server. Setzt den 'connectedState1' und
	 * 'connectedState2' des
	 * HandlerClient auf false um zu checken, ob der ReceiverClient die Nachricht vom
	 * InputStream gelesen hat und ändert den Wert dann wieder auf true. 	 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void pingTwo() {
		this.networkHandler.setConnectedState1(false);
		this.networkHandler.setConnectedState2(false);
		this.networkHandler.sendMessageToServer(new MessPing(100, 0));
	}

	/**
	 * Schließt die Verbindung zwischen Server und Client 
	 * Beendet die Anwendung. TimerTask ebenfalls beendet.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void stopConnection() {
		// Closes the Sockets
		this.networkHandler.close("PingCheckClient stoppt für Client nach " + pingIteration + " Pings");
//		this.networkHandler.setCloseNetwork(true);
		// Cancels this TimerTask
		this.cancel();
		try {
			System.out.println(
					"Verbindung zum Server verloren ( PINGCHECK)! \n\n Vergewissern Sie sich, dass der Server nicht geschlossen wurde \n Starten Sie danach das Spiel nochmals!");
			this.networkHandler.getServer().close();
		} catch (EOFException e) {
			System.out.println("CLIENT SOCKET geschlossen in PINGCHECKCLIENT");
		} catch (SocketException e) {
			System.out.println("CLIENT SOCKET geschlossen in PINGCHECKCLIENT");
		} catch (IOException e) {
			System.out.println("ERROR: PINGCHECKCLIENT");
			System.out.println("Socket ERROR für HANDLERCLIENT");
			e.printStackTrace();
		} finally {
			System.out.println("PINGCHECKCLIENT schließt das Spiel");
			System.exit(1);
		}
	}

}


