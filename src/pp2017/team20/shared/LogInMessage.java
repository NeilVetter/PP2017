package pp2017.team20.shared;

import pp2017.team20.shared.*;
import pp2017.team20.server.map.*;
import javax.crypto.SecretKey;

/**
 * Nachrichtenklasse, die beim LogIn den aktuellen Spieler sowie das Passwort uebermittelt.
 * Im Anschluss wird das aktuelle Level des Spielers geladen und an den Client uebergeben
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class LogInMessage extends Message {
	//Variable fuer den aktuellen Spieler
	public String username;
	//Variable fuer das Passwort
	public String password;
	//Vairable zum Ueberpruefen des Passworts
	public SecretKey keylog;
	public boolean success = false;
	int[][] lvlMaze;
	
//	public boolean succsess;
	

	/**
	 * 
	 * Konstruktor fuer die LogIn Nachricht, die den Namen sowie das Passwort enthaelt
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	public LogInMessage(int clientID, String username, String  password, int[][] lvlMaze) {
		
		super(clientID);
		this.username = username;
		this.password = password;
		this.lvlMaze=lvlMaze;
	}

	
	// Getter und Setter Methoden 
	
	public boolean getSuccess() {
		return success;
	}
	public void setLvlMaze(int i,int j){
		this.lvlMaze[i][j]=lvlMaze[i][j];
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int[][] getLvlMaze() {
		return lvlMaze;
	}
}
