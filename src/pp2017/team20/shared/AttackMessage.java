package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, die die ID des Spielers und Monsters uebermittelt.
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class AttackMessage extends Message {
	
	//Gibt die id des Angreifers an, entweder Spieler oder Monster
	public int attackID;
	//Gibt die id des Verteidigers an, entweder Spieler oder Monster
	public int defendID;
	//Vairable die die Lebenspunkte des Verteidigers angibt
	public int hpDefender;
	//Variablen, um die jeweils aktuelle Position zu bestimmen
	public int xPos;
	public int yPos;
	
	/**
	 * 
	 * Konstruktur fuer den Angriff
	 * 
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	
	public AttackMessage(int clientID, int attackID, int defendID) {
		super(clientID);
		this.attackID = attackID;
		this.defendID = defendID;
	}

}
