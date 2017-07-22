package pp2017.team20.shared;


public class UpdateMonsterMessage extends Message {

	

	public sendObject obj;
	
	public UpdateMonsterMessage(int clientID) {
		super(clientID);
		//MonsterList = l;
	}
	
	public void set(Monster l){
		obj = new sendObject(0, l.getXPos(), l.getYPos(), l.getStrength(), l.getId());
	}
	
	public sendObject get(){
		return obj;
	}
	
}