package pp2017.team20.shared;

import gui.HindiBones;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Monster extends Figur {
	/**
	 * Hier werden alle Monster erstellt und auch der Situation entsprechend gesteuert
	 * @author Sell, Robin, 6071120
	 * (@author HindiBones)
	 * Notiz: �rspr�nglich hatte ich alles auf englisch geschrieben, musste dann aber,
	 * um die Klasse in das HindiBones Spiel packen zu k�nnen, viele Begriffe
	 * der Umgebung anpassen und auf deutsch �ndern. F�r die Endabgabe sind dann wieder
	 * alle Variablen etc auf englisch und nur die Beschreibungen auf deutsch.
	 */

	private long lastAttack;
	private long lastStep;
	private int cooldownAttack;
	private int cooldownWalk;
	
	private int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int typ; // Von Beginn an anwesend: 0, Erscheint sp�ter: 1 	(eingedeutscht f�r Testumgebung)
	
	private int strength; //St�rke: 1 schwach, 2 normal, 3 stark, 6 Hulk-Modus
	private int state; //Zustand des Monsters: 0 Spazieren 1 Verfolgung 2 Attackieren 3 Fl�chten 4 Sterben

	public HindiBones fenster; //eingedeutscht f�r Testumgebung
	private Spieler spieler; //eingedeutscht f�r Testumgebung
	
	public Monster(int x, int y, HindiBones fenster, int typ){
		/**
		 * erstellt ein Monster an den Koordinaten (x,y) im Labyrinth und gibt ihm 
		 * eine gewisse strength mit, die alle weiteren Werte beeinflusst
		 * 
		 * @author Sell, Robin, 6071120 
		 * (Grundger�st: HindiBones)
		 * 
		 */
		this.fenster = fenster;
		this.spieler = fenster.spieler;
		this.typ = typ;
		setPos(x,y);
		
		double variable = Math.random() * 5 + fenster.currentLevel; //halb Zufall, halb Levelabh�ngig
		if (variable < 5)
			strength = 1;
		else if (variable >= 5 && variable < 7)
			strength = 2;
		else //if (variable >= 7 && variable < 9)
			strength = 3;
		//else 
			//strength = 6; //Hulk-Modus der noch kein Bild vom GUI bekommt und deshalb
			// f�r den 2.Meilenstein rausgenommen wurde
		
		setHealth(30 + 5 * strength);
		setMaxHealth(getHealth());
		lastAttack = System.currentTimeMillis();
		lastStep = System.currentTimeMillis();
		cooldownAttack = 500 - 10 * strength; 
		cooldownWalk = 1000 - 20*strength - 5* fenster.currentLevel;
		setSchaden(8+2*strength); //eingedeutscht f�r Testumgebung
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
		 * Das Herz der Klasse, was daf�r sorgt, dass immer in den richtigen Modeus
		 * geschaltet wird, sodass sich das Monster entsprechend verh�lt
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
		 * Spaziermodus, indem das Monster zuf�llige Schritte macht
		 * (Grundidee kommt von der urspr�nglichen move() Methode aus
		 * HindiBones, wurde dann aber f�r tacticMonster() erg�nzt
		 * 
		 * @author Sell, Robin, 6071120
		 * (Grundger�st: HindiBones)
		 */
		
		//verfasst bei Robin Sell, 6071120

		Random random = new Random(); 
		dir = random.nextInt(4); // erzeugt zuf�llige Zahl zwischen 0 und 3 (Laufrichtung)
		move(); 
		
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if(nextWalk){
			if (playerInVisibilityRange()){
				//System.out.println("Verfolgung aufnehmen");
				state = 1; // Springt zu Zustand: runBehind
			}
			else{
				//System.out.println("Spaziere weiter");
				state = 0; // Weiter: randomWalk (K�nnte auch weg)
			}
				
		}
			
	}
	public void runBehind(){ //Probleme
		/**
		 * �berpr�ft 3 Zust�nde: Im Fightradius, im Verfolgungsradius oder weder noch?
		 * Ersteres wechselt zu Zustand 2 und letzteres "zur�ck" in Zustand 0.
		 * Im Verfolgungsradius wird der k�rzeste Weg berechnet und der n�chste Schritt
		 * gleich der dir(ection) f�r den auszuf�hrenden Schritt gesetzt.
		 * Au�erdem wird �berpr�ft, ob der Spieler den Schl�ssel aufgenommen hat, denn
		 * dann wechseln alle Monster sofort in die Verfolgung.
		 * 
		 * @author Sell, Robin, 6071120
		 */
		
		//verfasst bei Robin Sell, 6071120
		
		if(playerInFightRange()){
			state = 2; // If im Fight Radius --> Attacke
		}
		else if(playerInVisibilityRange()||spieler.hatSchluessel()){ 
			boolean nextStep = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
			if (nextStep) {
				dir = calculateDirection();
			
			if (!zulaessig()) { //D�rfte eigentlich nicht auftreten, au�er ein Schl�ssel bzw eine T�r ist im Weg!
				System.out.println("Ein imagin�res Fabelwesen hat sich in den Weg gestellt, Monster kann sich vor Schreck nicht bewegen");
				return; 
			}
			move();
			state = 1;	// Weiter: runBehind (K�nnte auch weg)
				}
		}
		else  
			state = 0; // Weder im Fight noch im Verfolgungsradius: "zur�ck" zum randomWalk
	}
	public boolean attackiereSpieler(boolean hatSchluessel){	
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
				(Math.abs(spieler.getXPos() - this.getXPos()) + Math.abs(spieler.getYPos() - this.getYPos()) < 2);
		boolean kannAngreifen = false;
		if (typ == 0) kannAngreifen = ((System.currentTimeMillis() - lastAttack) >= cooldownAttack);
		if (typ == 1) kannAngreifen = (hatSchluessel && ((System.currentTimeMillis() - lastAttack) >= cooldownAttack));
		
		// Kann das Monster angreifen?
		if(spielerImRadius && kannAngreifen){
			lastAttack = System.currentTimeMillis();
			spieler.changeHealth(-getSchaden());
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
		 * Dann testet es, ob der Spieler den Schl�ssel hat, falls ja dann wechselt es abh�ngig
		 * vom Radius in den Kampf- bzw. Verfolgungsmodus.
		 * 
		 * Falls das alles verneint wurde, geht es in die Flucht �ber, indem gecheckt wird,
		 * wo der Spieler in Abh�ngigkeit des Monsters steht und er versucht in die anderen
		 * Himmelsrichtungen zu fliehen. Falls das nicht m�glich ist, springt das Monster
		 * nerv�s in der Ecke hin und her, sodass wenn der Spieler das Feld zwischen Ecke
		 * und Monster betritt, das Monster fl�chten kann!
		 * F�r die Endversion wird es hier noch einen Angsthasen-Typ Monster geben, das sich
		 * traurig in der Ecke verschanzt und sich vor Todesangst nicht mehr bewegen kann!
		 * 
		 * Kleines extra: Falls der Spieler den Schl�ssel hat und alle Monster auf ihn laufen,
		 * kommen die Monster in eine schwierige Situation, falls sie kurz vorm Tod stehen, da
		 * sie eigentlich nicht sterben wollen, aber auf der anderen Seite auch nicht wollen, 
		 * dass der Spieler ins n�chste Level kommt. 
		 * Vor lauter Ratlosigkeit erstarren die Monster, sodass der Player bei 
		 * der Vielzahl an Monstern, die auf ihn zukommen, nur 75% der Lebensernegie abziehen 
		 * muss, um sie Handlungsunf�hig zu machen.
		 * 
		 * @author Sell, Robin, 6071120
		 */
		
		if(getHealth() <= 0){
			state = 4;
			return;   // Falls keine Lebensenergie mehr --> monsterDies	
			}
		
		if(spieler.hatSchluessel()){ // checkt, ob Spieler den Schl�ssel hat und wechselt den Modus
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
			if (spieler.getXPos() < this.getXPos()) { 		// Player <---> Monster
				
				if (spieler.getYPos() < this.getYPos()) { 	// Player--->
															//       ---> Monster
					dir = 1; // fliehe gen Osten
					if (!zulaessig()) { // falls Wand, Tuer, Schluessel
						dir = 2; // fliehe gen Sueden
						if (!zulaessig()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden
							if (!zulaessig()) { // falls Wand, Tuer, Schluessel
								dir = 3; // fliehe gen Westen
								}
							}
						}
					} else {
				// 			   ---> Monster		ODER   Player ---> Monster
				// 		Player --->				ODER
						
						dir = 1; // fliehe gen Osten 
						if (!zulaessig()) { // falls Wand, Tuer, Schluessel
							dir = 0; // fliehe gen Norden 
							if (!zulaessig()) { // falls Wand, Tuer, Schluessel
								dir = 2; // fliehe gen Sueden
								if (!zulaessig()) { // falls Wand, Tuer, Schluessel
									dir = 3; // fliehe gen Westen
									}
								}
							}
						}
						
				} else {	// Monster <---> Player
					
					if (spieler.getYPos() < this.getYPos()) {	//          <--- Player
					         									//	Monster <--- 
						dir = 2; // fliehe gen Sueden
						if (!zulaessig()) { // falls Wand, Tuer, Schluessel
							dir = 3; // fliehe gen Westen
							if (!zulaessig()) { // falls Wand, Tuer, Schluessel
								dir = 0; // fliehe gen Norden
								if (!zulaessig()) { // falls Wand, Tuer, Schluessel
									dir = 1; // fliehe gen Osten
									}
								}
							}
						} else {
							//	 Monster <--- 				ODER		Monster <--- Player
							//			 <--- Player		ODER

							dir = 3; // fliehe nach Westen (f�r beides gut)
							if (!zulaessig()) { // Westen nicht moeglich(Wand)
								dir = 0; // probiere dann Nord (richtung = 0 --> Nord)
								if (!zulaessig()) { // falls Wand, Tuer, Schluessel
									dir = 2;
									if (!zulaessig()) { // falls Wand, Tuer, Schluessel
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
			state = 3; // (K�nnte auch weg)
			return;  //weiter fl�chten
			} else { // Monster erfolgreich gefluechtet
				setHealth(getMaxHealth()); // --> Lebensenergie zur�ck auf 100%
				state = 0;
				return; 
				}
	}
	public void monsterDies(){
		/**
		 * @author Sell, Robin, 6071120
		 * (Grundger�st: HindiBones)
		 */
		
		fenster.level[getXPos()][getYPos()] = new Heiltrank(30); // Monster hinterl�sst Heiltrank
		// Random Verteilung von Heiltrank und Manatrank f�r Endversion hier
		fenster.monsterListe.remove(this); // l�sche Monster
	}
	
	
	
	
	/** Hier wird der AStern Algorithmus benutzt, der f�r runBehind
	 * den k�rzesten Weg bzw die n�chste zu nehmende Richtung
	 * berechnet 
	 * 
	 * @author Sell, Robin, 6071120
	 */
	private class AStarNode {
		/**
		 * Erstellt Knoten, Getter und Setter f�r die
		 * Methode calculateDirection(), die den k�rzesten 
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
		 * A Stern Algorithmus, der den k�rzesten Weg berechnet.
		 *
		 * Auch hier wieder deutsche Ab�nderungen, damit ich die entsprechenden 
		 * Attribute etc. erhalten kann.
		 * 
		 * @author Sell, Robin, 6071120
		 * (unter Online-Hilfe zum Verst�ndnis des Algorithmus)
		 */

		// Erstellt 2 LinkedListen, die wir sp�ter benutzen.
		LinkedList<AStarNode> openedList = new LinkedList<AStarNode>(); // offene Liste
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>(); // geschlossene Liste 

		// Der Startknoten wird initialisiert & zur openedList hinzugefuegt
		AStarNode begin = new AStarNode(this.getXPos(), this.getYPos());
		
		begin.setPredecessor(null); 
		begin.setDistanceFrom(Math.abs(this.getXPos() - spieler.getXPos()) + Math.abs(this.getYPos() - spieler.getYPos()));
		begin.setDistanceTo(0);
		
		
		
		openedList.add(begin); //f�ge den begin Knoten zur openList hinzu

		AStarNode current = begin; 
	
		while (!openedList.isEmpty() && !(current.getX() == spieler.getXPos() && current.getY() == spieler.getYPos())) {

			openedList.remove(current); //entferne den current Knoten aus der openedList
			
			closedList.add(current); ///und f�ge ihn zur closedList hinzu
			
			
			// Teste jetzt alle m�glichen Nachbarknoten des current Knoten, 
			// ob sie begehbar sind und f�ge sie zur openedList, wenn sie in 
			// keiner Liste bisher vorkommt und dabei die openedList bzw. den 
			// Inhalt der Knoten aktualisieren.
		
			
			// Hier wird jetzt in den sich dadr�ber befindenen Nachbarknoten gegangen
			if (!((current.getY() == 0) 
					|| (fenster.level[current.getX()][current.getY() - 1] instanceof Wand)
					|| (fenster.level[current.getX()][current.getY() - 1] instanceof Tuer))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - spieler.getXPos()) + Math.abs(node.getY() - spieler.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}

			
			// Hier wird jetzt in den sich dadrunter befindenen Nachbarknoten gegangen
			if (!((current.getY() == fenster.HEIGHT) 
					|| (fenster.level[current.getX()][current.getY() + 1] instanceof Wand)
					|| (fenster.level[current.getX()][current.getY() + 1] instanceof Tuer))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - spieler.getXPos()) + Math.abs(node.getY() - spieler.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}


			// Hier wird jetzt in den sich links daneben befindenen Nachbarknoten gegangen
			if (!((current.getX() == 0) 
					|| (fenster.level[current.getX() - 1][current.getY()] instanceof Wand)
					|| (fenster.level[current.getX() - 1][current.getY()] instanceof Tuer))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - spieler.getXPos()) + Math.abs(node.getY() - spieler.getYPos()));
					node.setDistanceTo(current.getDistanceTo() + 1);
					node.setPredecessor(current);
					openedList.add(node);
				}
			}


			// Hier wird jetzt in den sich rechts daneben befindenen Nachbarknoten gegangen
			if (!((current.getX() == fenster.HEIGHT) 
					|| (fenster.level[current.getX() + 1][current.getY()] instanceof Wand)
					|| (fenster.level[current.getX() + 1][current.getY()] instanceof Tuer))) {

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
					node.setDistanceFrom(Math.abs(node.getX() - spieler.getXPos()) + Math.abs(node.getY() - spieler.getYPos()));
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

		// Hier wird  solange zur�ckverfolgt bis man am ersten Knoten
		// nach dem Startknoten landet, um die n�chsten Knoten f�r die
		// Methode zu erhalten
		if (current.getX() == spieler.getXPos() && current.getY() == spieler.getYPos()) {
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
		if(zulaessig()){
			if(nextWalk){	
				switch(dir){
					case 0 : hoch(); break;
					case 1 : rechts(); break;
					case 2 : runter(); break;
					case 3 : links(); break;
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
			fenster.level[getXPos()][getYPos()] = new Heiltrank(30);
			fenster.monsterListe.remove(this);
		}
	}
	public double cooldownProzent(){
		/**
		 * @author: HindiBones 
		 */
		return 1.0*(System.currentTimeMillis() - lastAttack)/cooldownAttack;
	}
	public int getTyp(){
		/**
		 * @author HindiBones
		 */
		return typ;
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
	private boolean zulaessig(){ // Pruefe, ob naechster Schritt zulaessig ist
		/**
		 * @author HindiBones
		 */
		if(dir == -1) return true;
		
		if(dir == 0 && getYPos()-1 > 0){
			return !(fenster.level[getXPos()][getYPos()-1] instanceof Wand) &&
				   !(fenster.level[getXPos()][getYPos()-1] instanceof Tuer) &&
				   !(fenster.level[getXPos()][getYPos()-1] instanceof Schluessel);
		}else if(dir == 1 && getXPos()+1 < fenster.WIDTH){
			return !(fenster.level[getXPos()+1][getYPos()] instanceof Wand) &&
				   !(fenster.level[getXPos()+1][getYPos()] instanceof Tuer) &&
				   !(fenster.level[getXPos()+1][getYPos()] instanceof Schluessel);
		}else if(dir == 2 && getYPos()+1 < fenster.HEIGHT){
			return !(fenster.level[getXPos()][getYPos()+1] instanceof Wand) &&
				   !(fenster.level[getXPos()][getYPos()+1] instanceof Tuer) &&
				   !(fenster.level[getXPos()][getYPos()+1] instanceof Schluessel);
		}else if(dir == 3 && getXPos() > 0 ){
			return !(fenster.level[getXPos()-1][getYPos()] instanceof Wand) &&
				   !(fenster.level[getXPos()-1][getYPos()] instanceof Tuer) &&
				   !(fenster.level[getXPos()-1][getYPos()] instanceof Schluessel);
		}
		else return false;
	}
	
	
	
	
	
	/** Die jeweiligen Reichweiten f�r die einzelnen Methoden
	 * @author Sell, Robin, 6071120
	 */
	public boolean playerInVisibilityRange() { //ab hier schaltet das Monster von randomWalk auf runBehind
		return (Math.sqrt(Math.pow(spieler.getXPos() - this.getXPos(), 2) 
				+ Math.pow(spieler.getYPos() - this.getYPos(), 2)) < 4);
	}
	
	public boolean playerInFreedomVisibilityRange() { //ab hier ist das Monster weit genug gefl�chtet, ob restForHealth zu machen
		return (Math.sqrt(Math.pow(spieler.getXPos() - this.getXPos(), 2) 
				+ Math.pow(spieler.getYPos() - this.getYPos(), 2)) < 6);
	}
	public boolean playerInFightRange() { //ab hier wird attackiert
		return (Math.abs(spieler.getXPos() - this.getXPos()) 
				+ Math.abs(spieler.getYPos() - this.getYPos()) < 2);
	}
	
}
