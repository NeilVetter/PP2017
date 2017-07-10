package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, die bei einem HighScore Name und Zeit uebermittelt
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class HighScoreMessage extends Message {
	//Variablen fuer den Benutzer und die Zeit
	public String user;
	public int time;
	
	/**
	 * 
	 * Konstruktor fuer die die Nachricht HighScore
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */
	
	public HighScoreMessage(String user, int time) {
		this.user = user;
		this.time = time;
	}

}
