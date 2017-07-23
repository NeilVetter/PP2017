package pp2017.team20.shared;



/**
 * 
 * Nachrichtenklasse, um ein neues Level zu laden.
 * Hat der Spieler das vorherige Level beendet und moechte in das nachste wechseln,
 * so wird selbiges geladen. Handelt es sich zuvor um das letzte Level, wird eine Mitteilung
 * ueber das Spielende uebermittelt
 * 
 * @author Wagner, Tobias, 5416213
 * @author Neil, Vetter 6021336
 *
 */

public class NextLevelMessage extends Message{
	//Variable, die das Spielende angibt. Zu Beginn wird diese auf false gesetzt
	public boolean gameEnding = false;
	
	private boolean successReceive;
	//Variable fuer ein neues Level
	public int playerID;
	public Level lvl;
	
	public NextLevelMessage(int clientID) {
		super(clientID);
	}
	
	//Methode fuer das erfolgreiche uebermitteln eines neuen Levels
	public boolean isSuccessReceive() {
		return successReceive;
	}
	
	//Methode um die boolean Variable zu verandern. Also beim Erreichen des Spielendes
	public void setSuccessReceive(boolean successReceive) {
		this.successReceive = successReceive;
	}
	
	//Methode, um das zu ladende Level zu erhalten
	public Level getLevel() {
		return lvl;
	}

}
