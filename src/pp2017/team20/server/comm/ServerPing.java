package pp2017.team20.server.comm;

import java.io.EOFException; 
import java.io.IOException;
import java.net.SocketException;
import java.util.TimerTask;

import pp2017.team20.shared.*;

/**
 * Die Klasse PingCheckServer baut den TimerTask zum Senden einer Nachricht zum  
 * Clienten innerhalb eines bestimmten Intervalls. Die Verbindung zwischen Server und
 * Client wird durch das ständige Senden von Nachrichten an den Server geprüft. Falls
 * die Nachrichten nicht gelesen werden können, wird der PingCheckServer die Verbindung
 * zum Clienten nach einer gewissen Zeit schließen. 
 * Die Methoden sind hier analog wie beim PingCheckClient. Keine Erläuterungen nötig
 * 
 * @author Yuxuan Kong 6019218
 */
public class ServerPing extends TimerTask {

	
	private int pingIteration = 0;
	//Verbindungsstatuts und senden von MessPing-Nachrichten
	private ServerHandler networkHandler;

	/**
	 * Initialisiert HandlerServer 'networkHandler'
	 * 
	 * @author Yuxuan Kong
	 * @param networkHandler
	 *            definiert HandlerServer, der TimerTask startet
	 */
	public ServerPing(ServerHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	/**
	 * Führt den TimerTask aus. Sendet eine MessPing-Nachricht an den
	 * Clienten um eine Rückmeldung zu bekommen. Schließt den Socket,
	 * wenn keine Nachrichten lesbar sind. Startet 2 Versuche um die Nachrichten
	 * zu lesen. 
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	@Override
	public void run() {
		pingIteration++;
		if (this.networkHandler.getConnectedStatus1() && this.networkHandler.getConnectedStatus2()) {
			
			pingOne();
		} else if (!this.networkHandler.getConnectedStatus1() && this.networkHandler.getConnectedStatus2()) {

			pingTwo();
		} else if (!this.networkHandler.getConnectedStatus1() && !this.networkHandler.getConnectedStatus2()) {

			stopConnection();
		}
	}

	/**
	 * @author Yuxuan Kong 6019218
	 */
	private void pingTwo() {
		this.networkHandler.setConnectedStatus1(false);
		this.networkHandler.setConnectedStatus2(false);
		//Neil
		this.networkHandler.sendMessageToClient(new pp2017.team20.shared.MessPing(100, 0));
	}

	/**
	 * @author Yuxuan Kong 6019218
	 */
	private void pingOne() {
		this.networkHandler.setConnectedStatus1(false);
		//Neil
		this.networkHandler.sendMessageToClient(new MessPing(100, 0));
	}

	/**
	 * Schließt die Verbindung zwischen Server und Client 
	 * Beendet die Anwendung. TimerTask ebenfalls beendet.
	 * 
	 * @author Yuxuan Kong 6019218
	 */
	private void stopConnection() {

		System.out.println("PingCheckServer STOPTHREADS für Server nach " + pingIteration + " Pings");
		
		this.cancel();
		
		try {
			System.out.println(
					"Verbindung zum Clienten verloren (PINGCHECK)! \n\n Bitte vergewissern Sie sich, dass der Client nicht geschlossen wurde! \n Starten Sie das Spiel danach neu!");
			this.networkHandler.getClient().close();
			System.out.println("PINGCHECKSERVER: CLIENT geschlossen");

			this.networkHandler.getServer().close();
			System.out.println("PINGCHECKSERVER: SERVER geschlossen");
		}catch (SocketException e) {
			System.out.println("CLIENT SOCKET schließt in PINGCHECKSERVER");
		}  
		catch (EOFException e) {
			System.out.println("CLIENT SOCKET schließt in PINGCHECKSERVER");
		} catch (IOException e) {
			System.out.println("ERROR: PINGCHECKSERVER");
			System.out.println("Socket ERROR für HandlerServer resultiert in PingCheckServer");
			e.printStackTrace();
		}
				this.networkHandler.close();
	}

}
