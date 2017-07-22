package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, um einen Trank zu nehmen
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class UsePotionMessage extends Message{
	
	//id zur Identifikation des aktuellen Spielers
	public int type; //-1 heißt benutzen, 1 heißt hinzufügen
	public int playerID;
	
	/**
	 * 
	 * Konstruktor der Klasse NehmeTrank
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	
	public UsePotionMessage(int clientID, int type,int playerID){
		super(clientID);
		this.type = type;
		this.playerID=playerID;
	}

}
