package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, um zu uebermitteln, dass ein Schluessel aufgenommen werden soll
 * 
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class CollectKeyMessage extends Message {
	
	public boolean success;
	public int playerId;
	
	public CollectKeyMessage(int clientID, int playerId) {
		super(clientID);
		this.playerId = playerId;
	}

}
