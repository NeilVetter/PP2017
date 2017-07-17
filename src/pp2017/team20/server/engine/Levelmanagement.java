package pp2017.team20.server.engine;

import pp2017.team20.shared.*;

import java.util.ArrayList;

import pp2017.team20.client.gui.GamingArea;
import pp2017.team20.server.map.*;

//Neil Vetter 6021336
//Diese Klasse erzeugt erst, und speichert dann das Level in einem Array

public class Levelmanagement {
	
	// Erstellt ein Objekt vom Typ "Player"
	public Player player;
	
	Player[] playerList;
	Monster[] monsterList;
	Healthpot[] healpotList;
	public static int[][] lvlMaze;
	public int LevelID = -1;
	public static int size;
	public int lvlcount;
	static int keyx;
	static int keyy;
	static int doorx;
	static int doory;
//abstest
	public Levelmanagement(Player player) {
		this.player = player;
	}

	// Diese Methode erzeugt ein Map nach Vorgaben vom Levelgenerator
	public void newLevel(int LevelID, GamingArea window, int type) {

		// erstelle neues "Maze" mit Hilfe des Konstruktors der Klasse Maze
		Maze maze = new Maze(LevelID);

		// Fï¿½gt das Spiel zur Levelliste des eingeloggten Spielers hinzu
		Player.LevelList.add(maze);

		// Speicher im Spieler welches Level er zuletzt gespielt hat
		// Lvlindex geht von 0,1,...
		player.playerLvl = LevelID;
		

		// Speichert die Map beim Spieler, sodass Elemente darauf abgelesen
		// werden kï¿½nnen
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				player.playerMap[i][j] = maze.Map[i][j];
				
			}
		}
		
		/** 
		 * Gibt den Spielern, Monstern und den Heiltränken eine ID und speichert diese
		 * 
		 * @autor Hamid Kirli 6041663*/
		int playerID = 0;
		int monsterID = 0;
		int healpotID =0;
		//Arrays definieren 
		playerList = new Player[LevelID];
		monsterList = new Monster[LevelID*3];
		healpotList = new Healthpot[LevelID];
		
		for (int i=0; i<player.playerMap.length; i++){
			for (int j=0;j<player.playerMap.length;j++){
				if (player.playerMap[i][j]==2){
					Player player = new Player(playerID ,i, j);
					// wieso passt es nicht mit deinem Konstrucktor @neil
					player.setPos(i,j);
					playerList[playerID]= player;
					playerID++;
				}
				else if (player.playerMap[i][j]== 6){
					
					Monster monster = new Monster(monsterID, i , j, window ,  type );
					// Robin muss den Construktor ändern // also einfach eine monsterID einfügen
					monsterList[monsterID]= monster;
					monsterID++;	
				}
				else if (player.playerMap[i][j]==4){
					Healthpot healpot= new Healthpot(healpotID, i, j);
					// muss ich in klasse healthpot ändern.
					healpotList[healpotID] = healpot;
					healpotID++;		
				}
				else if(player.playerMap[i][j] == 5){
					int k;
					Monster monster = new Monster(monsterID, i, j, window, type);
					monsterList[monsterID]= monster;
					monsterID++;
			}
				else if (player.playerMap[i][j]== 6){
					doorx =j;
					doory =i;
				}}
		}
	}
		
		/** Getter Methoden für SpielerListe, MonsterListe
		// und Heiltrank 
		 * 
		 * @author Hamid Kirli 6041663*/
 		public Player[] getPlayerList() {
			return playerList;
		}
		
		public Monster[] getMonsterList(){
			return monsterList;
		}
		
		public Healthpot[] getHealthpot(){
			return healpotList;
		}
		
		/** 
		 * überprüfen ob einer der Spieler einen Heiltrank nutzt.
		 * 
		 *   @author Hamid, Kirli 6041663 
		 *   */
		
		public boolean healpotuse(int playerID){
			boolean used;
			if(playerList[playerID].getHealthPotNumber() ==  0){
				used = false;
			}else
				used = true;
			
		return used;
		}
		
		/** 
		 *Methode zum Benutzen von Heiltränken  */
		public void useHealthpot(int playerID){
			if(healpotuse(playerID)){
				playerList[playerID].setHealthPotNumber(playerList[playerID].getHealthPotNumber()-1);
				playerList[playerID].setHealth(10);
	
			}
		}
		
		/* Testmap
		int[][] test = new int[15][15];

		// Testweises Ausgeben der Map, wie in der Klasse "Maze"
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				test[i][j] = maze.Map[i][j];
				System.out.print(test[i][j]);
			}
			System.out.println();
		}
	}*/
	/** Setter-Um einzelne Level Elemente zu verändern
	 * 
	 *  @author Hamid  Kirli  6041663 */
		public void setLvlMaze (int levelID, int x, int y,int substance, Levelmanagement game1 ){
			lvlMaze[x][y] = substance;
			if(substance == 2){
				int playerID = 0;
				game1.playerList[playerID].setXPos(x);
				game1.playerList[playerID].setYPos(y);
			} else if ( substance == 6){
				boolean monsterbaby = false;
				int monsterID = 0;
				while(!monsterbaby){
					if(Levelmanagement.lvlMaze[x][y] != 0 &&
							monsterList[monsterID].getXPos()== x &&
							monsterList[monsterID].getYPos() == y){
						monsterbaby = true;
						Levelmanagement.lvlMaze[monsterList[monsterID].getXPos()][monsterList[monsterID].getYPos()] = 1;
						game1.monsterList[monsterID].setXPos(x);
						game1.monsterList[monsterID].setYPos(y);	
					} else {
						if (monsterID < monsterList.length -1){
							monsterID++;
						}
						else { 
							monsterbaby = true;
						}
					}
				}
				}else if ( substance == 4 ){
					boolean healpotbaby = false;
					int healpotID = 0 ;
					while(!healpotbaby){
						if(healpotList[healpotID].getPosx() == x && 
								healpotList[healpotID].getPosy()== y){
							healpotbaby = true;
							healpotList[healpotID].setPosx(x);
							healpotList[healpotID].setPosy(y);
							healpotList[healpotID].take= false;
						} else {
							if (healpotID < healpotList.length -1){
								healpotID++;
							} else
								healpotbaby =true;
						}
					}
					
				}
			
		}
		
	// Diese Methode setzt einen Spieler zurï¿½ck an den anfang des levels
	// (des von ihm gespeicherten)
	// z.B. nach seinem tot
	public void placePlayer(int playerID) {

		MessageProcessing m = new MessageProcessing();

		for (int i = 0; i < m.PlayerList.size(); i++) {
			Player player = m.PlayerList.get(i);
			if (playerID == player.playerID) {
				for (int j = 0; j < size; j++) {
					for (int k = 0; k < size; k++) {
						if (player.playerMap[j][k] == 2) {
							player.xPos = j;
							player.yPos = k;
						}
					}
				}
			}
		}
	}
}
