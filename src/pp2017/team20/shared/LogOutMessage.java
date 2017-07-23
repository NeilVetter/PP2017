package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse fuer den LogOut
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class LogOutMessage extends Message {
	
	public boolean success;
	public int playerID;

	public LogOutMessage (int clientID,int playerID) {
		super(clientID);
		this.playerID=playerID;
		
	}

	public void setSuccess(boolean b) {
	this.success=b;
		
	}

}
