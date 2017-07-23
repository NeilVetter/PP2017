package pp2017.team20.shared;

import java.io.Serializable;

public class sendObject implements Serializable{
	
	/**
	 * @author Neil, Vetter, 6021336
	 */
	private static final long serialVersionUID = 1L;
	public int type; //0 is for monsters, 1 for players
	public int posX; 
	public int posY;
	public int strength;
	public int ID;
	
	public sendObject(int t, int x, int y, int s, int id){
		this.type = t;
		this.posX = x;
		this.posY = y;
		this.strength = s;
		this.ID = id;
	}
	
}