package pp2017.team20.shared;

import pp2017.team20.server.engine.Levelmanagement;

import java.io.Serializable;

import java.util.LinkedList;
import java.util.Random;

public class Monster extends Figure implements Serializable {
	/**
	 * Hier werden alle Monster erstellt und auch der Situation entsprechend
	 * gesteuert
	 * 
	 * @author Sell, Robin, 6071120 (Grundidee: HindiBones)
	 */

	private long lastAttack;
	private long lastStep;
	private int cooldownAttack;
	private int cooldownWalk;

	private int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int type; // Von Beginn an anwesend: 0, Erscheint spaeter: 1

	private int strength; // Staerke: 1 schwach, 2 normal, 3 stark, 6 Hulk-Modus
	private int state; // Zustand des Monsters: 0 Spazieren 1 Verfolgung 2
						// Attackieren 3 Fluechten 4 Sterben

	private int x ,y;
	private Player player;
	public int monsterID;
	public Levelmanagement lvl;
	public AttackMessage am;

	public Monster(int monsterID, int x, int y, Levelmanagement lvl, int type) {
		/**
		 * erstellt ein Monster an den Koordinaten (x,y) im Labyrinth und gibt
		 * ihm eine gewisse strength mit, die alle weiteren Werte beeinflusst
		 * 
		 * @author Sell, Robin, 6071120 (Grundgeruest: HindiBones)
		 * 
		 */
		this.monsterID = monsterID;
		this.type = type;
		this.lvl = lvl;
		setPos(x, y);
		double variable = Math.random() * 5 + lvl.getLevelID(); // halb Zufall,
																// halb
																// Levelabhaengig
		if (variable < 5)
			strength = 1;
		else if (variable >= 5 && variable < 7)
			strength = 2;
		else if (variable >= 7 && variable < 9)
			strength = 3;
		else
			strength = 6;

		setHealth(30 + 5 * strength);
		setMaxHealth(getHealth());
		lastAttack = System.currentTimeMillis();
		lastStep = System.currentTimeMillis();
		cooldownAttack = 500 - 10 * strength;
		cooldownWalk = 1000 - 20 * strength - 5 * lvl.getLevelID();
		setDamage(10 + 2 * strength);
		state = 0;
	}

	/**
	 * Ab hier wird der endliche Automat programmiert zusammen mit seinen
	 * jeweiligen Methoden
	 * 
	 * @author Sell, Robin, 6071120
	 */
	public void tacticMonster() {
		/**
		 * Das Herz der Klasse, was dafuer sorgt, dass immer in den richtigen
		 * Modeus geschaltet wird, sodass sich das Monster entsprechend verhaelt
		 * 
		 * @ author: Sell, Robin, 607112
		 */
			switch (state) {
			case 0:
				randomWalk(); // -->randomWalk, -->runBehind
				break;
			case 1:
				runBehind(); // -->runBehind, -->attackPlayer, -->randomWalk
				break;
			case 2:
				attackiereSpieler(false); // -->attackPlayer, -->runBehind, -->flee,
											// -->monsterDies
				break;
			case 3:
				flee(); // -->flee, -->randomWalk,
						// -->runBehind(attackPlayer)[Kamikaze-Modus],
				break;		
			}
	}

	public void randomWalk() {
		/**
		 * Spaziermodus, indem das Monster zufaellige Schritte macht (Grundidee
		 * kommt von der urspruenglichen move() Methode aus HindiBones, wurde
		 * dann aber fuer tacticMonster() ergaenzt
		 * 
		 * @author Sell, Robin, 6071120 (Grundgeruest: HindiBones)
		 */

		
		Random random = new Random();
		dir = random.nextInt(4); // erzeugt zufaellige Zahl zwischen 0 und 3
									// (Laufrichtung)
		move();

		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (nextWalk) {
			if (playerInVisibilityRange() || lvl.player.ownsKey()) {
				state = 1; // Springt zu Zustand: runBehind
			} else {
				state = 0; // Weiter: randomWalk (Koennte auch weg)
			}
		}
	}

	public void runBehind() { // Probleme
		/**
		 * ueberprueft 3 Zustaende: Im Fightradius, im Verfolgungsradius oder
		 * weder noch? Ersteres wechselt zu Zustand 2 und letzteres "zurueck" in
		 * Zustand 0. Im Verfolgungsradius wird der kuerzeste Weg berechnet und
		 * der naechste Schritt gleich der dir(ection) fuer den auszufuehrenden
		 * Schritt gesetzt. Ausserdem wird ueberprueft, ob der Spieler den
		 * Schluessel aufgenommen hat, denn dann wechseln alle Monster sofort in
		 * die Verfolgung.
		 * 
		 * @author Sell, Robin, 6071120
		 */

		// verfasst bei Robin Sell, 6071120

		if (playerInFightRange()) {
			state = 2; // If im Fight Radius --> Attacke
		} else if (playerInVisibilityRange() || lvl.player.ownsKey()) {
			boolean nextStep = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
			if (nextStep) {
				dir = calculateDirection();

				if (!moveAllowed()) { // Duerfte eigentlich nicht auftreten,
										// ausser ein Schluessel bzw eine Tuer
										// ist im Weg!
					System.out.println(
							"Ein imaginaeres Fabelwesen hat sich in den Weg gestellt, Monster kann sich vor Schreck nicht bewegen");
					return;
				}
				move();
				state = 1; // Weiter: runBehind (Koennte auch weg)
			}
		} else
			state = 0; // Weder im Fight noch im Verfolgungsradius: "zurueck"
						// zum randomWalk
	}

	public boolean attackiereSpieler(boolean ownsKey) {
		/**
		 * Schaltet hier in den Kampfmodus, abhaengig davon ob der Spieler den
		 * Schluessel hat, und ueberprueft vorher, ob das Monster bei geringer
		 * Lebensenergie nicht lieber fliehen sollte, um sich regenrieren zu
		 * koennen
		 * 
		 * @author HindiBones
		 * @author Sell, Robin (Ergaenzungen)
		 */

		if (this.getHealth() <= this.getMaxHealth() / 4) { // Teste, ob Monster
															// < oder = 25%
															// Lebensenergie hat
			state = 3; // --> flee
			return false;
		}

		// Ist der Spieler im Radius des Monsters?
		boolean playerINRadius = (Math.abs(player.getXPos() - this.getXPos())
				+ Math.abs(player.getYPos() - this.getYPos()) < 2);
		boolean ableToAttack = false;
		if (type == 0)
			ableToAttack = ((System.currentTimeMillis() - lastAttack) >= cooldownAttack);
		if (type == 1)
			ableToAttack = (ownsKey && ((System.currentTimeMillis() - lastAttack) >= cooldownAttack));

		// Kann das Monster angreifen?
		if (playerINRadius && ableToAttack) {
			lastAttack = System.currentTimeMillis();
			player.changeHealth(-getDamage());
			state = 2; // koennte auch weggelassen werden, aber zur Uebersicht
			am = new AttackMessage(1, 0, this.getId(), player.getPlayerID(), player.getHealth());
			// sende eine AttackMessage
			return playerINRadius;
		}
		state = 1;
		return playerINRadius;
	}

	public void flee() {
		/**
		 * 
		 * Diese Methode testet zuerst, ob der Spieler den Schluessel hat, falls ja dann
		 * wechselt es abhaengig vom Radius in den Kampf- bzw. Verfolgungsmodus.
		 * 
		 * Falls das alles verneint wurde, geht es in die Flucht ueber, indem
		 * gecheckt wird, wo der Spieler in Abhaengigkeit des Monsters steht und
		 * er versucht in die anderen Himmelsrichtungen zu fliehen. Falls das
		 * nicht moeglich ist, springt das Monster nervoes in der Ecke hin und
		 * her, sodass wenn der Spieler das Feld zwischen Ecke und Monster
		 * betritt, das Monster fluechten kann! Fuer die Endversion wird es hier
		 * noch einen Angsthasen-Typ Monster geben, das sich traurig in der Ecke
		 * verschanzt und sich vor Todesangst nicht mehr bewegen kann!
		 * 
		 * Kleines extra: Falls der Spieler den Schluessel hat und alle Monster
		 * auf ihn laufen, kommen die Monster in eine schwierige Situation,
		 * falls sie kurz vorm Tod stehen, da sie eigentlich nicht sterben
		 * wollen, aber auf der anderen Seite auch nicht wollen, dass der
		 * Spieler ins naechste Level kommt. Vor lauter Ratlosigkeit erstarren
		 * die Monster, sodass der Player bei der Vielzahl an Monstern, die auf
		 * ihn zukommen, nur 75% der Lebensernegie abziehen muss, um sie
		 * Handlungsunfaehig zu machen.
		 * 
		 * @author Sell, Robin, 6071120
		 */

		

		if (lvl.player.ownsKey()) { // checkt, ob Spieler den Schluessel hat und
								// wechselt den Modus
			if (playerInFightRange()) {
				state = 2; // --> Attackieren
				return;
			} else {
				state = 1; // --> Verfolgung
				return;
			}
		}

		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (nextWalk) {
			// Spieler ist westl. vom Monster
			if (lvl.player.getXPos() < this.getXPos()) { // Player <---> Monster

				if (lvl.player.getYPos() < this.getYPos()) { // Player--->
															// ---> Monster
					dir = 1; // fliehe gen Osten
					if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
						dir = 2; // fliehe gen Sueden
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden
							if (!moveAllowed()) { // falls Wand, Tuer,
													// Schluessel
								dir = 3; // fliehe gen Westen
							}
						}
					}
				} else {
					// ---> Monster ODER Player ---> Monster
					// Player ---> ODER

					dir = 1; // fliehe gen Osten
					if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
						dir = 0; // fliehe gen Norden
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 2; // fliehe gen Sueden
							if (!moveAllowed()) { // falls Wand, Tuer,
													// Schluessel
								dir = 3; // fliehe gen Westen
							}
						}
					}
				}

			} else { // Monster <---> Player

				if (lvl.player.getYPos() < this.getYPos()) { // <--- Player
															// Monster <---
					dir = 2; // fliehe gen Sueden
					if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
						dir = 3; // fliehe gen Westen
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden
							if (!moveAllowed()) { // falls Wand, Tuer,
													// Schluessel
								dir = 1; // fliehe gen Osten
							}
						}
					}
				} else {
					// Monster <--- ODER Monster <--- Player
					// <--- Player ODER

					dir = 3; // fliehe nach Westen (fuer beides gut)
					if (!moveAllowed()) { // Westen nicht moeglich(Wand)
						dir = 0; // probiere dann Nord (richtung = 0 --> Nord)
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 2;
							if (!moveAllowed()) { // falls Wand, Tuer,
													// Schluessel
								dir = 1;
							}
						}
					}
				}
			}
			move();
		}
		// Monster noch nicht gefluechtet, da in Reichweite des Spielers
		if (playerInFreedomVisibilityRange()) {
			state = 3; // (Koennte auch weg)
			return; // weiter fluechten
		} else { // Monster erfolgreich gefluechtet
			setHealth(getMaxHealth()); // --> Lebensenergie zurueck auf 100%
			state = 0;
			return;
		}
	}

	/**
	 * Hier wird der AStern Algorithmus benutzt, der fuer runBehind den
	 * kuerzesten Weg bzw die naechste zu nehmende Richtung berechnet
	 * 
	 * @author Sell, Robin, 6071120
	 */
	private class AStarNode {
		/**
		 * Erstellt Knoten, Getter und Setter fuer die Methode
		 * calculateDirection(), die den kuerzesten Weg hin zum Spieler
		 * berechnet
		 */

		private int distanceTo, distanceFrom;
		private final int X, Y;

		private AStarNode predecessor;

		public AStarNode(int x, int y) {

			/**
			 * Erstellt einen Knoten mit x-Koordinate X und y-Koordinate Y
			 * 
			 * @author
			 */

			this.X = x;
			this.Y = y;
		}

		// Getter und Setter

		public void setDistanceFrom(int distanceFrom) {
			this.distanceFrom = distanceFrom;
		}

		public int getDistanceFrom() {
			return this.distanceFrom;
		}

		public void setDistanceTo(int distanceTo) {
			this.distanceTo = distanceTo;
		}

		public int getDistanceTo() {
			return this.distanceTo;
		}

		public void setPredecessor(AStarNode predecessor) {
			this.predecessor = predecessor;
		}

		public AStarNode getPredecessor() {
			return this.predecessor;
		}

		public int getX() {
			return this.X;
		}

		public int getY() {
			return this.Y;
		}
	}

	public int calculateDirection() {

		/**
		 * A Stern Algorithmus, der den kuerzesten Weg berechnet.
		 *
		 * Auch hier wieder deutsche Abaenderungen, damit ich die entsprechenden
		 * Attribute etc. erhalten kann.
		 * 
		 * @author Sell, Robin, 6071120 (unter Online-Hilfe zum Verstaendnis des
		 *         Algorithmus)
		 */

		// Erstellt 2 LinkedListen, die wir spaeter benutzen.
		LinkedList<AStarNode> openedList = new LinkedList<AStarNode>(); // offene
																		// Liste
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>(); // geschlossene
																		// Liste

		// Der Startknoten wird initialisiert & zur openedList hinzugefuegt
		AStarNode begin = new AStarNode(this.getXPos(), this.getYPos());

		begin.setPredecessor(null);
		begin.setDistanceFrom(
				Math.abs(this.getXPos() - lvl.player.getXPos()) + Math.abs(this.getYPos() - lvl.player.getYPos()));
		begin.setDistanceTo(0);

		openedList.add(begin); // fuege den begin Knoten zur openList hinzu

		AStarNode current = begin;

		while (!openedList.isEmpty() && !(current.getX() == lvl.player.getXPos() && current.getY() == lvl.player.getYPos())) {

			openedList.remove(current); // entferne den current Knoten aus der
										// openedList

			closedList.add(current); /// und fuege ihn zur closedList hinzu

			// Teste jetzt alle moeglichen Nachbarknoten des current Knoten,
			// ob sie begehbar sind und fuege sie zur openedList, wenn sie in
			// keiner Liste bisher vorkommt und dabei die openedList bzw. den
			// Inhalt der Knoten aktualisieren.

			// Hier wird jetzt in den sich dadrueber befindenen Nachbarknoten
			// gegangen
			if (!((current.getY() == 0) || (lvl.getLvlMazePosition(current.getX(), current.getY() - 1) == 0
					|| (lvl.getLvlMazePosition(current.getX(), current.getY() - 1) == 3)))) {

				boolean neew = true; // AB HIER GILT: neew = new bzw neu
				for (AStarNode node : openedList) {
					if (node.getX() == current.getX() && node.getY() == current.getY() - 1) {
						neew = false;
						if (current.getDistanceTo() + 1 < node.getDistanceTo()) {
							node.setDistanceTo(current.getDistanceTo() + 1);
							node.setPredecessor(current);
						}
					}
				}
				for (AStarNode node : closedList)
					if (node.getX() == current.getX() && node.getY() == current.getY() - 1) {
						neew = false;
					}

				if (neew) {

					AStarNode node = new AStarNode(current.getX(), current.getY() - 1);
					node.setDistanceFrom(
							Math.abs(node.getX() - lvl.player.getXPos()) + Math.abs(node.getY() - lvl.player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			// Hier wird jetzt in den sich dadrunter befindenen Nachbarknoten
			// gegangen
			if (!((current.getY() == 19) || (lvl.getLvlMazePosition(current.getX(), current.getY() + 1) == 0
					|| (lvl.getLvlMazePosition(current.getX(), current.getY() + 1) == 3)))) {

				boolean neew = true;
				for (AStarNode node : openedList) {
					if (node.getX() == current.getX() && node.getY() == current.getY() + 1) {
						neew = false;
						if (current.getDistanceTo() + 1 < node.getDistanceTo()) {
							node.setPredecessor(current);
							node.setDistanceTo(current.getDistanceTo() + 1);
						}
					}
				}

				for (AStarNode node : closedList)
					if (node.getX() == current.getX() && node.getY() == current.getY() + 1) {
						neew = false;
					}

				if (neew) {

					AStarNode node = new AStarNode(current.getX(), current.getY() + 1);
					node.setDistanceFrom(
							Math.abs(node.getX() - lvl.player.getXPos()) + Math.abs(node.getY() - lvl.player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			// Hier wird jetzt in den sich links daneben befindenen
			// Nachbarknoten gegangen
			if (!((current.getX() == 0) || (lvl.getLvlMazePosition(current.getX() - 1, current.getY()) == 0
					|| (lvl.getLvlMazePosition(current.getX() - 1, current.getY()) == 3)))) {

				boolean neew = true;
				for (AStarNode node : openedList) {
					if (node.getX() == current.getX() - 1 && node.getY() == current.getY()) {
						neew = false;
						if (current.getDistanceTo() + 1 < node.getDistanceTo()) {
							node.setDistanceTo(current.getDistanceTo() + 1);
							node.setPredecessor(current);
						}
					}
				}

				for (AStarNode node : closedList)
					if (node.getX() == current.getX() - 1 && node.getY() == current.getY())
						neew = false;

				if (neew) {

					AStarNode node = new AStarNode(current.getX() - 1, current.getY());
					node.setDistanceFrom(
							Math.abs(node.getX() - lvl.player.getXPos()) + Math.abs(node.getY() - lvl.player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			// Hier wird jetzt in den sich rechts daneben befindenen
			// Nachbarknoten gegangen
			if (!((current.getX() == 19) || (lvl.getLvlMazePosition(current.getX() + 1, current.getY()) == 0
					|| (lvl.getLvlMazePosition(current.getX() + 1, current.getY()) == 3)))) {

				boolean neew = true;
				for (AStarNode node : openedList) {
					if (node.getX() == current.getX() + 1 && node.getY() == current.getY()) {
						neew = false;
						if (current.getDistanceTo() + 1 < node.getDistanceTo()) {
							node.setDistanceTo(current.getDistanceTo() + 1);
							node.setPredecessor(current);
						}
					}
				}

				for (AStarNode node : closedList)
					if (node.getX() == current.getX() + 1 && node.getY() == current.getY())
						neew = false;

				if (neew) {

					AStarNode node = new AStarNode(current.getX() + 1, current.getY());
					node.setDistanceFrom(
							Math.abs(node.getX() - lvl.player.getXPos()) + Math.abs(node.getY() - lvl.player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			// Hier wird jetzt der naechste Knoten gesucht, der benutzt werden
			// soll
			if (!openedList.isEmpty()) {
				AStarNode next = openedList.get(0);
				for (AStarNode node : openedList) {
					if (node.getDistanceTo() + node.getDistanceFrom() < next.getDistanceTo() + next.getDistanceFrom()) {
						next = node;
					}
				}
				current = next;
			}
		}
		int dir = -1;
		
		// Hier wird solange zurueckverfolgt bis man am ersten Knoten
		// nach dem Startknoten landet, um die naechsten Knoten fuer die
		// Methode zu erhalten
		if (current.getX() == lvl.player.getXPos() && current.getY() == lvl.player.getYPos()) {
			while (!current.getPredecessor().equals(begin))
				current = current.getPredecessor();

			// Richtung fuer den naechsten Schritt wird bestimmt
			if (current.getX() == begin.getX() + 1)
				dir = 1;
			else if (current.getX() == begin.getX() - 1)
				dir = 3;
			else if (current.getY() == begin.getY() + 1)
				dir = 2;
			else if (current.getY() == begin.getY() - 1)
				dir = 0;
		}

		return dir;
	}

	/**
	 * HindiBones Methoden
	 * (mit Ergaenzungen/Abaenderungen)
	 * 
	 * @author HindiBones
	 */
	public void move() {
		/**
		 * @author HindiBones
		 */
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (moveAllowed()) {
			if (nextWalk) {
				switch (dir) {
				case 0:
					up();
					break;
				case 1:
					right();
					break;
				case 2:
					down();
					break;
				case 3:
					left();
					break;
				}
				lastStep = System.currentTimeMillis();
			}
		} else {
			Random random = new Random();
			dir = random.nextInt(4);
		}
	}

	public double cooldownProzent() {
		/**
		 * @author: HindiBones
		 */
		return 1.0 * (System.currentTimeMillis() - lastAttack) / cooldownAttack;
	}

	public int getType() {
		/**
		 * @author HindiBones
		 */
		return this.type;
	}

	private boolean moveAllowed() { // Pruefe, ob naechster Schritt zulaessig
									// ist
		/**
		 * @author Sell, Robin, 6071120
		 *(Grundgeruest: HindiBones)
		 */
		if (dir == -1)
			return true;

		if (dir == 0 && getYPos() - 1 > 0) {
			return !((lvl.getLvlMazePosition(getXPos(), getYPos() - 1) == 0)
				&& !((lvl.getLvlMazePosition(getXPos(), getYPos() - 1) == 3)
				&& !((lvl.getLvlMazePosition(getXPos(), getYPos() - 1) == 5))));
		} else if (dir == 1 && getXPos() + 1 < 19) {
			return !((lvl.getLvlMazePosition(getXPos() + 1, getYPos()) == 0)
				&& !((lvl.getLvlMazePosition(getXPos() + 1, getYPos()) == 3)
				&& !((lvl.getLvlMazePosition(getXPos() + 1, getYPos()) == 5))));
		} else if (dir == 2 && getYPos() + 1 < 19) {
			return !((lvl.getLvlMazePosition(getXPos(), getYPos() + 1) == 0)
				&& !((lvl.getLvlMazePosition(getXPos(), getYPos() + 1) == 3)
				&& !((lvl.getLvlMazePosition(getXPos(), getYPos() + 1) == 5))));
		} else if (dir == 3 && getXPos() > 0) {
			return !((lvl.getLvlMazePosition(getXPos() - 1, getYPos()) == 0)
				&& !((lvl.getLvlMazePosition(getXPos() - 1, getYPos()) == 3)
				&& !((lvl.getLvlMazePosition(getXPos() - 1, getYPos()) == 5))));
		} else
			return false;
	}

	/**
	 * Die jeweiligen Reichweiten fuer die einzelnen Methoden
	 * 
	 * @author Sell, Robin, 6071120
	 */
	public boolean playerInVisibilityRange() { // ab hier schaltet das Monster
												// von randomWalk auf runBehind
		boolean result = false;
		double tmp;
		double min = Double.MAX_VALUE;
		for (Player player : lvl.PlayerList) {
			tmp = Math.sqrt(
					Math.pow(player.getXPos() - this.getXPos(), 2) + Math.pow(player.getYPos() - this.getYPos(), 2));
			result = (Math.sqrt(Math.pow(player.getXPos() - this.getXPos(), 2)
					+ Math.pow(player.getYPos() - this.getYPos(), 2)) <7);
			if (result && tmp < min) {
				min = tmp;
				this.player = player;
			}
		}
		return result;
	}

	public boolean playerInFreedomVisibilityRange() { // ab hier ist das Monster
										// weit genug gefluechtet, um restForHealth
										//zu machen
		boolean result = false;
		double tmp;
		double min = Double.MAX_VALUE;
		for (Player player : lvl.PlayerList) {
			tmp = (Math.sqrt(
					Math.pow(player.getXPos() - this.getXPos(), 2) + Math.pow(player.getYPos() - this.getYPos(), 2)));
			result = (Math.sqrt(
					Math.pow(player.getXPos() - this.getXPos(), 2) + Math.pow(player.getYPos() - this.getYPos(), 2)) < 9);
			if (result && tmp < min) {
				min = tmp;
				this.player = player;
			}
		}
		return result;
	}

	public boolean playerInFightRange() { // ab hier wird attackiert
		boolean result = false;
		double tmp;
		double min = Double.MAX_VALUE;
		for (Player player : lvl.PlayerList) {
			tmp = (Math.abs(player.getXPos() - this.getXPos()) + Math.abs(player.getYPos() - this.getYPos()));
			result = (Math.abs(player.getXPos() - this.getXPos()) + Math.abs(player.getYPos() - this.getYPos()) < 2);
			if (result && tmp < min) {
				min = tmp;
				this.player = player;
			}
		}
		return result;	
	}
	
	public int getStrength(){
		return this.strength;
	}

	public int getId(){
		return this.monsterID;
	}
	
	public void setX(int x){
		this.x = x;
	}
	public int getX(){
		return x;
	}
	
	public void setY(int x){
		this.y = x;
	}
	public int getY(){
		return y;
	}
}
