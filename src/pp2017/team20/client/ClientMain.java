package pp2017.team20.client;

import pp2017.team20.client.gui.GamingArea;

/**
 * Klasse der Main-Methode fuer den Client, um den Client zu starten, sich mit dem Server zu verbinden
 * und ein neues Spiel zu starten
 * 
 * @author Wagner, Tobias, 5416213
 */

public class ClientMain {

	/**
	 * Team 20: TeamLos
	 * 
	 * Teammitglieder: 	Heck, Liz, 5991099
	 * 					Kirli, Hamid, 6041663
	 * 					Kong, Yuxuan, 6019218
	 * 					Vetter, Neil, 6021336
	 * 					Sell, Robin, 6071120
	 * 					Wagner, Tobias, 5416213
	 * 
	 */

	//Variablen, die die Groesse des Spielfensters angeben bzw. bestimmen
//	public static final int BOX = 38;
//	public static final int WIDTH = 19, HEIGHT = 19;
	
	//startet das Spiel
	public static void main(String args[]) {
		new GamingArea("TeamLos");

	}
}

