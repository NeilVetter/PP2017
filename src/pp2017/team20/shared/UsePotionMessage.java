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
	public int id;
	
	/**
	 * 
	 * Konstruktor der Klasse NehmeTrank
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	
	public UsePotionMessage(int id){
		this.id = id;
	}

}
