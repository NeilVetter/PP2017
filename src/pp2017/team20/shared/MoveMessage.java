package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, die die Bewegung des Spielers bzw. eines Monsters uebermittelt
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class MoveMessage extends Message {
	//id gibt an, ob es sich um einen Spieler oder ein Monster handelt
	public int id;
	//Geben die Position an
	public int xPos;
	public int yPos;
	

	/**
	 * 
	 * Konstruktor fuer die Nachricht Bewegung
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */
	public MoveMessage(int xPos, int yPos, int ID) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.id = ID;
	}

}
