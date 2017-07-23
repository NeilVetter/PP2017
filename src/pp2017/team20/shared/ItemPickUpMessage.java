package pp2017.team20.shared;

/** Message Untertyp
 * 
 * @autor Neil. Vetter, 6021336
 */

public class ItemPickUpMessage extends Message{

	public int ItemID;
	public int playerID;

	public ItemPickUpMessage(int clientID, int ItemID,int playerID){
		super(clientID);
		this.ItemID=ItemID;
		this.playerID=playerID;
	}
}
