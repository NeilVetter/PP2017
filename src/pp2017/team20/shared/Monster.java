package pp2017.team20.shared;

import pp2017.team20.client.gui.GamingArea;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Monster extends Figure {
	/**
	 * Hier werden alle Monster erstellt und auch der Situation entsprechend gesteuert
	 * @author Sell, Robin, 6071120
	 * (Grundidee: HindiBones)
	 */

	private long lastAttack;
	private long lastStep;
	private int cooldownAttack;
	private int cooldownWalk;
	
	private int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int type; // Von Beginn an anwesend: 0, Erscheint spaeter: 1 	
	
	private int strength; //Staerke: 1 schwach, 2 normal, 3 stark, 6 Hulk-Modus
	private int state; //Zustand des Monsters: 0 Spazieren 1 Verfolgung 2 Attackieren 3 Fluechten 4 Sterben

	public GamingArea window; 
	private Player player; 
	public int monsterID;
	
	public Monster(int monsterID, int x, int y, GamingArea window, int type){
		/**
		 * erstellt ein Monster an den Koordinaten (x,y) im Labyrinth und gibt ihm 
		 * eine gewisse strength mit, die alle weiteren Werte beeinflusst
		 * 
		 * @author Sell, Robin, 6071120 
		 * (Grundgeruest: HindiBones)
		 * 
		 */
		this.monsterID = monsterID;
		this.window = window;
		this.player = window.player;
		this.type = type;
		setPos(x,y);
		
		double variable = Math.random() * 5 + window.currentLevel; //halb Zufall, halb Levelabhaengig
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
		cooldownWalk = 1000 - 20*strength - 5* window.currentLevel;
		setDamage(8+2*strength); 
		state = 0;
		
		
		// Bild fuer das Monster laden
		
		try {
			setImage(ImageIO.read(new File("img//drache" + strength + ".png")));
		} catch (IOException e) {
			System.err.print("Das Bild drache.png konnte nicht geladen werden.");
		}
		
	}
	
	
	/**Ab hier wird der endliche Automat programmiert
	 * zusammen mit seinen jeweiligen Methoden
	 * 
	 * @author Sell, Robin, 6071120
	 */
	public void tacticMonster(){ 
		/**
		 * Das Herz der Klasse, was dafuer sorgt, dass immer in den richtigen Modeus
		 * geschaltet wird, sodass sich das Monster entsprechend verhaelt
		 * 
		 * @ author: Sell, Robin, 607112
		 */
		
		switch(state){
		case 0: 
			randomWalk(); //-->randomWalk, -->runBehind
			break;
		case 1: 
			runBehind(); //-->runBehind, -->attackPlayer, -->randomWalk
			break;
		case 2: 
			attackiereSpieler(false); //-->attackPlayer, -->runBehind, -->flee, -->monsterDies
			break;
		case 3: 
			flee(); //-->flee, -->randomWalk, -->runBehind(attackPlayer)[Kamikaze-Modus], -->monsterDies
			break;
		case 4:  
			monsterDies();
			break; //ENDE
		}
		
	}
	public void randomWalk() {
		/**
		 * Spaziermodus, indem das Monster zufaellige Schritte macht
		 * (Grundidee kommt von der urspruenglichen move() Methode aus
		 * HindiBones, wurde dann aber fuer tacticMonster() erg�nzt
		 * 
		 * @author Sell, Robin, 6071120
		 * (Grundgeruest: HindiBones)
		 */
		
		//verfasst bei Robin Sell, 6071120

		Random random = new Random(); 
		dir = random.nextInt(4); // erzeugt zufaellige Zahl zwischen 0 und 3 (Laufrichtung)
		move(); 
		
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if(nextWalk){
			if (playerInVisibilityRange()){
				state = 1; // Springt zu Zustand: runBehind
			}
			else{
				state = 0; // Weiter: randomWalk (K�nnte auch weg)
			}
				
		}
			
	}
	public void runBehind(){ //Probleme
		/**
		 * ueberprueft 3 Zustaende: Im Fightradius, im Verfolgungsradius oder weder noch?
		 * Ersteres wechselt zu Zustand 2 und letzteres "zurueck" in Zustand 0.
		 * Im Verfolgungsradius wird der kuerzeste Weg berechnet und der naechste Schritt
		 * gleich der dir(ection) fuer den auszufuehrenden Schritt gesetzt.
		 * Ausserdem wird ueberprueft, ob der Spieler den Schluessel aufgenommen hat, denn
		 * dann wechseln alle Monster sofort in die Verfolgung.
		 * 
		 * @author Sell, Robin, 6071120
		 */
		
		//verfasst bei Robin Sell, 6071120
		
		if(playerInFightRange()){
			state = 2; // If im Fight Radius --> Attacke
		}
		else if(playerInVisibilityRange()||player.ownsKey()){ 
			boolean nextStep = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
			if (nextStep) {
				dir = calculateDirection();
			
			if (!moveAllowed()) { //Duerfte eigentlich nicht auftreten, ausser ein Schluessel bzw eine Tuer ist im Weg!
				System.out.println("Ein imaginaeres Fabelwesen hat sich in den Weg gestellt, Monster kann sich vor Schreck nicht bewegen");
				return; 
			}
			move();
			state = 1;	// Weiter: runBehind (K�nnte auch weg)
				}
		}
		else  
			state = 0; // Weder im Fight noch im Verfolgungsradius: "zur�ck" zum randomWalk
	}
	public boolean attackiereSpieler(boolean ownsKey){	
		/** 
		 * Schaltet hier in den Kampfmodus, abh�ngig davon ob der Spieler den Schl�ssel hat, und
		 * �berpr�ft vorher, ob das Monster bei geringer Lebensenergie nicht lieber fliehen sollte, 
		 * um sich regenrieren zu k�nnen
		 * 
		 * @author HindiBones
		 * @author Sell, Robin (Erg�nzungen)
		 */
		
		if(this.getHealth() <= this.getMaxHealth()/4){  //Teste, ob Monster < oder = 25% Lebensenergie hat
			state = 3;	  // --> flee 
			return false;
		}
		
		// Ist der Spieler im Radius des Monsters?	
		boolean spielerImRadius = 
				(Math.abs(player.getXPos() - this.getXPos()) + Math.abs(player.getYPos() - this.getYPos()) < 2);
		boolean kannAngreifen = false;
		if (type == 0) kannAngreifen = ((System.currentTimeMillis() - lastAttack) >= cooldownAttack);
		if (type == 1) kannAngreifen = (ownsKey && ((System.currentTimeMillis() - lastAttack) >= cooldownAttack));
		
		// Kann das Monster angreifen?
		if(spielerImRadius && kannAngreifen){
			lastAttack = System.currentTimeMillis();
			player.changeHealth(-getDamage());
			state = 2; //k�nnte auch weggelassen werden, aber zur �bersicht
			return spielerImRadius;
		}
		
		state = 1;
		return spielerImRadius;
	}	
	public void flee(){
		/**
		 * Diese Methode testet zuerst, ob das Monster noch lebt, falls nicht --> monsterDies.
		 * 
		 * Dann testet es, ob der Spieler den Schl�ssel hat, falls ja dann wechselt es abhaengig
		 * vom Radius in den Kampf- bzw. Verfolgungsmodus.
		 * 
		 * Falls das alles verneint wurde, geht es in die Flucht ueber, indem gecheckt wird,
		 * wo der Spieler in Abhaengigkeit des Monsters steht und er versucht in die anderen
		 * Himmelsrichtungen zu fliehen. Falls das nicht moeglich ist, springt das Monster
		 * nervoes in der Ecke hin und her, sodass wenn der Spieler das Feld zwischen Ecke
		 * und Monster betritt, das Monster fluechten kann!
		 * Fuer die Endversion wird es hier noch einen Angsthasen-Typ Monster geben, das sich
		 * traurig in der Ecke verschanzt und sich vor Todesangst nicht mehr bewegen kann!
		 * 
		 * Kleines extra: Falls der Spieler den Schluessel hat und alle Monster auf ihn laufen,
		 * kommen die Monster in eine schwierige Situation, falls sie kurz vorm Tod stehen, da
		 * sie eigentlich nicht sterben wollen, aber auf der anderen Seite auch nicht wollen, 
		 * dass der Spieler ins naechste Level kommt. 
		 * Vor lauter Ratlosigkeit erstarren die Monster, sodass der Player bei 
		 * der Vielzahl an Monstern, die auf ihn zukommen, nur 75% der Lebensernegie abziehen 
		 * muss, um sie Handlungsunfaehig zu machen.
		 * 
		 * @author Sell, Robin, 6071120
		 */
		
		if(getHealth() <= 0){
			state = 4;
			return;   // Falls keine Lebensenergie mehr --> monsterDies	
			}
		
		if(player.ownsKey()){ // checkt, ob Spieler den Schluessel hat und wechselt den Modus
			if(playerInFightRange()){
				state = 2; // --> Attackieren
				return;  
				}
			else{
				state = 1; // --> Verfolgung 
				return;
				}
			}
		
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (nextWalk) {
			// Spieler ist westl. vom Monster
			if (player.getXPos() < this.getXPos()) { 		// Player <---> Monster
				
				if (player.getYPos() < this.getYPos()) { 	// Player--->
															//       ---> Monster
					dir = 1; // fliehe gen Osten
					if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
						dir = 2; // fliehe gen Sueden
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden
							if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
								dir = 3; // fliehe gen Westen
								}
							}
						}
					} else {
				// 			   ---> Monster		ODER   Player ---> Monster
				// 		Player --->				ODER
						
						dir = 1; // fliehe gen Osten 
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden 
							if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
								dir = 2; // fliehe gen Sueden
								if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
									dir = 3; // fliehe gen Westen
									}
								}
							}
						}
						
				} else {	// Monster <---> Player
					
					if (player.getYPos() < this.getYPos()) {	//          <--- Player
					         									//	Monster <--- 
						dir = 2; // fliehe gen Sueden
						if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
							dir = 3; // fliehe gen Westen
							if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
								dir = 0; // fliehe gen Norden
								if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
									dir = 1; // fliehe gen Osten
									}
								}
							}
						} else {
							//	 Monster <--- 				ODER		Monster <--- Player
							//			 <--- Player		ODER

							dir = 3; // fliehe nach Westen (fuer beides gut)
							if (!moveAllowed()) { // Westen nicht moeglich(Wand)
								dir = 0; // probiere dann Nord (richtung = 0 --> Nord)
								if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
									dir = 2;
									if (!moveAllowed()) { // falls Wand, Tuer, Schluessel
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
			return;  //weiter fluechten
			} else { // Monster erfolgreich gefluechtet
				setHealth(getMaxHealth()); // --> Lebensenergie zurueck auf 100%
				state = 0;
				return; 
				}
	}
	public void monsterDies(){
		/**
		 * @author Sell, Robin, 6071120
		 * (Grundgeruest: HindiBones)
		 */
		
		window.level[getXPos()][getYPos()] = new HealthPot(30); // Monster hinterlaesst Heiltrank
		// Random Verteilung von Heiltrank und Manatrank fuer Endversion hier
		window.monsterList.remove(this); // loesche Monster
	}
	
	
	
	
	/** Hier wird der AStern Algorithmus benutzt, der fuer runBehind
	 * den kuerzesten Weg bzw die naechste zu nehmende Richtung
	 * berechnet 
	 * 
	 * @author Sell, Robin, 6071120
	 */
	private class AStarNode {
		/**
		 * Erstellt Knoten, Getter und Setter fuer die
		 * Methode calculateDirection(), die den kuerzesten 
		 * Weg hin zum Spieler berechnet
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
		 * @author Sell, Robin, 6071120
		 * (unter Online-Hilfe zum Verstaendnis des Algorithmus)
		 */

		// Erstellt 2 LinkedListen, die wir sp�ter benutzen.
		LinkedList<AStarNode> openedList = new LinkedList<AStarNode>(); // offene Liste
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>(); // geschlossene Liste 

		// Der Startknoten wird initialisiert & zur openedList hinzugefuegt
		AStarNode begin = new AStarNode(this.getXPos(), this.getYPos());
		
		begin.setPredecessor(null); 
		begin.setDistanceFrom(Math.abs(this.getXPos() - player.getXPos()) + Math.abs(this.getYPos() - player.getYPos()));
		begin.setDistanceTo(0);
		
		
		
		openedList.add(begin); //fuege den begin Knoten zur openList hinzu

		AStarNode current = begin; 
	
		while (!openedList.isEmpty() && !(current.getX() == player.getXPos() && current.getY() == player.getYPos())) {

			openedList.remove(current); //entferne den current Knoten aus der openedList
			
			closedList.add(current); ///und f�ge ihn zur closedList hinzu
			
			
			// Teste jetzt alle moeglichen Nachbarknoten des current Knoten, 
			// ob sie begehbar sind und fuege sie zur openedList, wenn sie in 
			// keiner Liste bisher vorkommt und dabei die openedList bzw. den 
			// Inhalt der Knoten aktualisieren.
		
			
			// Hier wird jetzt in den sich dadrueber befindenen Nachbarknoten gegangen
			if (!((current.getY() == 0) 
					|| (window.level[current.getX()][current.getY() - 1] instanceof Wall)
					|| (window.level[current.getX()][current.getY() - 1] instanceof Door))) {

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
					if (node.getX() == current.getX() && node.getY() == current.getY() - 1){
						neew = false;
					}

				if (neew) {

					AStarNode node = new AStarNode(current.getX(), current.getY() - 1);
					node.setDistanceFrom(Math.abs(node.getX() - player.getXPos()) + Math.abs(node.getY() - player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			
			// Hier wird jetzt in den sich dadrunter befindenen Nachbarknoten gegangen
			if (!((current.getY() == window.HEIGHT) 
					|| (window.level[current.getX()][current.getY() + 1] instanceof Wall)
					|| (window.level[current.getX()][current.getY() + 1] instanceof Door))) {

				boolean neew = true;
				for (AStarNode node : openedList) {
					if (node.getX() == current.getX() && node.getY() == current.getY() + 1) {
						neew = false;
						if (current.getDistanceTo() + 1 < node.getDistanceTo() ) {
							node.setPredecessor(current);
							node.setDistanceTo(current.getDistanceTo() + 1);
						}
					}
				}

				for (AStarNode node : closedList)
					if (node.getX() == current.getX() && node.getY() == current.getY() + 1){
						neew = false;
					}

				if (neew) {

					AStarNode node = new AStarNode(current.getX(), current.getY() + 1);
					node.setDistanceFrom(Math.abs(node.getX() - player.getXPos()) + Math.abs(node.getY() - player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}


			// Hier wird jetzt in den sich links daneben befindenen Nachbarknoten gegangen
			if (!((current.getX() == 0) 
					|| (window.level[current.getX() - 1][current.getY()] instanceof Wall)
					|| (window.level[current.getX() - 1][current.getY()] instanceof Door))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - player.getXPos()) + Math.abs(node.getY() - player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}


			// Hier wird jetzt in den sich rechts daneben befindenen Nachbarknoten gegangen
			if (!((current.getX() == window.HEIGHT) 
					|| (window.level[current.getX() + 1][current.getY()] instanceof Wall)
					|| (window.level[current.getX() + 1][current.getY()] instanceof Door))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - player.getXPos()) + Math.abs(node.getY() - player.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			// Hier wird jetzt der naechste Knoten gesucht, der benutzt werden soll
			if (!openedList.isEmpty()) {
				AStarNode next = openedList.get(0);
				for (AStarNode node : openedList) {
					if (node.getDistanceTo() + node.getDistanceFrom() < next.getDistanceTo() + next.getDistanceFrom()){
						next = node;
					}
				}
				current = next;
			}

		}

		int dir = -1;

		// Hier wird  solange zurueckverfolgt bis man am ersten Knoten
		// nach dem Startknoten landet, um die naechsten Knoten fuer die
		// Methode zu erhalten
		if (current.getX() == player.getXPos() && current.getY() == player.getYPos()) {
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
	
	
	/**reine HindiBones Methoden
	 * 
	 * @author HindiBones
	 */
	public void move(){
		/**
		 * @author HindiBones
		 */
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if(moveAllowed()){
			if(nextWalk){	
				switch(dir){
					case 0 : up(); break;
					case 1 : right(); break;
					case 2 : down(); break;
					case 3 : left(); break;
				}
				lastStep = System.currentTimeMillis();
			}
		}else{
			changeDir();			
		}
	}
	public void changeHealth(int change){
		/**
		 * @author HindiBones
		 */
		super.changeHealth(change);
		if(getHealth()<=0){
			window.level[getXPos()][getYPos()] = new HealthPot(30);
			window.monsterList.remove(this);
		}
	}
	public double cooldownProzent(){
		/**
		 * @author: HindiBones 
		 */
		return 1.0*(System.currentTimeMillis() - lastAttack)/cooldownAttack;
	}
	public int getType(){
		/**
		 * @author HindiBones
		 */
		return type;
	}
	
	public void changeDir(){
		/**
		 * @author HindiBones
		 */
		Random random = new Random();		
		dir = random.nextInt(4);
	}
	public void changeDir(int dir) {
		this.dir = dir;
	}
	private boolean moveAllowed(){ // Pruefe, ob naechster Schritt zulaessig ist
		/**
		 * @author HindiBones
		 */
		if(dir == -1) return true;
		
		if(dir == 0 && getYPos()-1 > 0){
			return !(window.level[getXPos()][getYPos()-1] instanceof Wall) &&
				   !(window.level[getXPos()][getYPos()-1] instanceof Door) &&
				   !(window.level[getXPos()][getYPos()-1] instanceof Key);
		}else if(dir == 1 && getXPos()+1 < window.WIDTH){
			return !(window.level[getXPos()+1][getYPos()] instanceof Wall) &&
				   !(window.level[getXPos()+1][getYPos()] instanceof Door) &&
				   !(window.level[getXPos()+1][getYPos()] instanceof Key);
		}else if(dir == 2 && getYPos()+1 < window.HEIGHT){
			return !(window.level[getXPos()][getYPos()+1] instanceof Wall) &&
				   !(window.level[getXPos()][getYPos()+1] instanceof Door) &&
				   !(window.level[getXPos()][getYPos()+1] instanceof Key);
		}else if(dir == 3 && getXPos() > 0 ){
			return !(window.level[getXPos()-1][getYPos()] instanceof Wall) &&
				   !(window.level[getXPos()-1][getYPos()] instanceof Door) &&
				   !(window.level[getXPos()-1][getYPos()] instanceof Key);
		}
		else return false;
	}
	
	
	
	
	
	/** Die jeweiligen Reichweiten fuer die einzelnen Methoden
	 * @author Sell, Robin, 6071120
	 */
	public boolean playerInVisibilityRange() { //ab hier schaltet das Monster von randomWalk auf runBehind
		return (Math.sqrt(Math.pow(player.getXPos() - this.getXPos(), 2) 
				+ Math.pow(player.getYPos() - this.getYPos(), 2)) < 4);
	}
	
	public boolean playerInFreedomVisibilityRange() { //ab hier ist das Monster weit genug gefluechtet, ob restForHealth zu machen
		return (Math.sqrt(Math.pow(player.getXPos() - this.getXPos(), 2) 
				+ Math.pow(player.getYPos() - this.getYPos(), 2)) < 6);
	}
	public boolean playerInFightRange() { //ab hier wird attackiert
		return (Math.abs(player.getXPos() - this.getXPos()) 
				+ Math.abs(player.getYPos() - this.getYPos()) < 2);
	}
	
}
