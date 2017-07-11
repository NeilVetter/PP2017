package pp2017.team20.shared;

public class DeathMessage extends Message {

public int id;
public int playerID;
public int monsterID;

	public DeathMessage(int clientID, int id){
		super(clientID);
		this.id=id;
	}
	
}
