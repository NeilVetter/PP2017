package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, um zu uebermitteln, dass ein Trank aufgenommen werden soll
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class CollectPotionMessage extends Message {
	
	public int posX;
	public int posY;
	public int playerID;
	public CollectPotionMessage(int clientID, int x, int y, int playerID) {
		super(clientID);
		this.posX = x;
		this.posY = y;
		this.playerID = playerID;
	}

}
