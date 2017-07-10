package pp2017.team20.shared;

import pp2017.team20.server.map.*;

/**
 * 
 * 
 * Nachrichtenklasse, die ein neues Spiel initiert und das jeweilige Level laedt
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class NewGameMessage extends Message {
	
	private Maze lvl;
	
	//Getter und Setter
	//Initialisiert das zu ladene Level
	public void setMaze(Maze lvl){
		this.lvl = lvl;
	}
	
	//Empfaengt das zu ladende Level
	public Maze getMaze(){
		return lvl;
	}

}
