package pp2017.team20.shared;

import Shared.Player;

public class ItemPickUpMessage extends Message{

	public int ItemID;
	public Player player;

	public ItemPickUpMessage(int ItemID){
		this.ItemID=ItemID;
	}
}
