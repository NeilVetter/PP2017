package pp2017.team20.shared;

import javafx.geometry.HPos;

/**
 * 
 * Nachrichtenklasse, die die ID des Spielers und Monsters uebermittelt.
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class AttackMessage extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Gibt die id des Angreifers an, entweder Spieler oder Monster
	public int attackType; //0 monster, 1 spieler
	public int attackID;
	//Gibt die id des Verteidigers an, entweder Spieler oder Monster
	public int defendID;
	//Vairable die die Lebenspunkte des Verteidigers angibt
	public int hpDefender;

	
	/**
	 * 
	 * Konstruktur fuer den Angriff
	 * 
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	
	public AttackMessage(int clientID, int type, int attackID, int defendID, int hpDefender) {
		super(clientID);
		this.attackType = type;
		this.attackID = attackID;
		this.defendID= defendID;
		this.hpDefender = hpDefender;
	}

}
