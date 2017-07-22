package pp2017.team20.shared;

public class DeathMessage extends Message {

	public int type; //0 für monster 1 für spieler
	public int id;

	public DeathMessage(int clientID, int type, int id){
		super(clientID);
		this.type = type;
		this.id=id;
	}
	
}
