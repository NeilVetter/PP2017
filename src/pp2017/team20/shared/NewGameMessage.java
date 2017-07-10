package pp2017.team20.shared;

import pp2016.team03.shared.Level;

/**
 * 
 * 
 * Nachrichtenklasse, die ein neues Spiel initiert und das jeweilige Level laedt
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class NewGameMessage extends Message {
	
	private Level lvl;
	
	//Getter und Setter
	//Initialisiert das zu ladene Level
	public void setLevel(Level lvl){
		this.lvl = lvl;
	}
	
	//Empfaengt das zu ladende Level
	public Level getLevel(){
		return lvl;
	}

}
