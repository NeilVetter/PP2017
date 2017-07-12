package pp2017.team20.shared;

import pp2017.team20.shared.*;
import pp2017.team20.server.map.*;

/**
 * Nachrichtenklasse, die beim LogIn den aktuellen Spieler sowie das Passwort uebermittelt.
 * Im Anschluss wird das aktuelle Level des Spielers geladen und an den Client uebergeben
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class LogInMessage extends Message {
	//Variable fuer den aktuellen Spieler
	public String user;
	//Variable fuer das Passwort
	public String password1;
	//Vairable zum Ueberpruefen des Passworts
	public String password2;
	private boolean success = false;
	
	int LevelID;
	Level level = new Level();
	public boolean succsess;
	

	/**
	 * 
	 * Konstruktor fuer die LogIn Nachricht, die den Namen sowie das Passwort enthaelt
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	public LogInMessage(int clientID, String user, String password1, String password2) {
		
		super(clientID);
		this.user = user;
		this.password1 = password1;
		this.password2 = password2;
	}

	
	// Getter und Setter Methoden 
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Level getLevel(){
		return level;
	}
	public void setLevel(Level level){
		this.level = level;
	}
	
	
}
