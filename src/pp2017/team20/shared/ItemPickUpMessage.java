package pp2017.team20.shared;

public class ItemPickUpMessage extends Message{

	public int ItemID;
	public Player player;

	public ItemPickUpMessage(int clientID, int ItemID){
		super(clientID);
		this.ItemID=ItemID;
	}
}
