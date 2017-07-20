package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, die die Bewegung des Spielers bzw. eines Monsters uebermittelt.
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class MoveMessage extends Message {
	
	//Geben die Position an
	public int xPos;
	public int yPos;
	public int playerID;
	public boolean success;
	

	/**
	 * 
	 * Konstruktor fuer die Nachricht Bewegung
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */
	public MoveMessage(int clientID, int xPos, int yPos, int playerID) {
		super(clientID);
		this.xPos = xPos;
		this.yPos = yPos;
		this.playerID = playerID;
	}
	public void setSuccess(boolean success){
		this.success=success;
	}
	public boolean getSuccess(){
		return success;
	}
	
}
