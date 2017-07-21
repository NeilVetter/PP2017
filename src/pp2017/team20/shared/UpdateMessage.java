package pp2017.team20.shared;

import java.util.ArrayList;

public class UpdateMessage extends Message {

	

	public ArrayList<Monster> MonsterList;
	
	public UpdateMessage(int clientID) {
		super(clientID);
	}
}