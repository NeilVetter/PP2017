package pp2017.team20.shared;

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
	public Level level;
	public Player player;
	public int playerID;
//	public boolean succsess;
	

	/**
	 * 
	 * Konstruktor fuer die LogIn Nachricht, die den Namen sowie das Passwort enthaelt
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	public LogInMessage(int clientID, String username, String  password) {
		
		super(clientID);
		this.username = username;
		this.password = password;
		
	}

	
	// Getter und Setter Methoden 
	
	public boolean getSuccess() {
		return success;
	}
	public Level getLevel(){
		return level;
	}
	public void setLevel(Level level){
		this.level=level;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Player getPlayer(){
		return player;
	}
	public void setPlayer(Player player){
		this.player=player;
	}


	public void setPlayerID(int playerID) {
		this.playerID=playerID;
		
	}

	
}
