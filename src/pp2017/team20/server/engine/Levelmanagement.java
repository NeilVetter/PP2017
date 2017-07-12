package pp2017.team20.server.engine;

import pp2017.team20.shared.*;

import java.util.ArrayList;

import pp2017.team20.server.map.*;

//Neil Vetter 6021336
//Diese Klasse erzeugt erst, und speichert dann das Level in einem Array

public class Levelmanagement {
	
	// Erstellt ein Objekt vom Typ "Player"
	public Player player;
	
	Player[] playerList;
	Monster[] monsterList;
	Healthpot[] healpotList;
	public int[][] lvlMaze;
	public int LevelID = -1;
	public static int size;
	public int lvlcount;
	static int keyx;
	static int keyy;
	static int doorx;
	static int doory;

	public Levelmanagement(Player player) {
		this.player = player;
	}

	// Diese Methode erzeugt ein Map nach Vorgaben vom Levelgenerator
	public void newLevel(int LevelID) {

		// erstelle neues "Maze" mit Hilfe des Konstruktors der Klasse Maze
		Maze maze = new Maze(LevelID);

		// F�gt das Spiel zur Levelliste des eingeloggten Spielers hinzu
		Player.LevelList.add(maze);

		// Speicher im Spieler welches Level er zuletzt gespielt hat
		// Lvlindex geht von 0,1,...
		player.playerLvl = LevelID;
		

		// Speichert die Map beim Spieler, sodass Elemente darauf abgelesen
		// werden k�nnen
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				player.playerMap[i][j] = maze.Map[i][j];
				
			}
		}
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
					Monster monster = new Monster(monsterID, i , j);
					// Robin muss den Construktor �ndern // also einfach eine monsterID einf�gen
					monsterList[monsterID]= monster;
					monsterID++;	
				}
				else if (player.playerMap[i][j]==4){
					Healthpot healpot= new Healthpot(healpotID, i, j);
					// muss ich in klasse healthpot �ndern.
					healpotList[healpotID] = healpot;
					healpotID++;		
				}
				else if(player.playerMap[i][j] == 5){
					Monster monster = new Monster(monsterID, i, j);
					monsterList[monsterID]= monster;
					monsterID++;
			}
				else if (player.playerMap[i][j]== 6){
					doorx =j;
					doory =i;
				}}
		}
	}
		
		// Getter Methoden f�r SpielerListe, MonsterListe
		// und Heiltrank
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
		 * �berpr�fen ob einer der Spieler einen Heiltrank nutzt.
		 * 
		 *   @author Hamid, Kirli 6041663 
		 *   */
		
		public boolean healpotuse(int playerID){
			boolean correct;
			if(playerList[playerID].getHealthPotNumber()> 0){
				correct = true;
			}else
				correct = false;
			
		return correct;
		}
		
		/** 
		 *Methode zum Benutzen von Heiltr�nken  */
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
	
	// Diese Methode setzt einen Spieler zur�ck an den anfang des levels
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
