package pp2017.team20.server.engine;

import pp2017.team20.shared.*;


import java.io.Serializable;
import java.util.ArrayList;
import pp2017.team20.server.map.*;


/**
 * 
 * Diese Klasse erzeugt erst eine Spielwelt, und erzeugt/speichert dann die Objekte für diese
 * 
 * @author Neil,Vetter,6021336
 *
 */
public class Levelmanagement implements Serializable {
	
	
	
	
	public int[][] lvlMaze;
	public int[][] test;
	public int LevelID = -1;
	public static int size;
	public int lvlcounter;
	static int keyx;
	static int keyy;
	static int doorx;
	static int doory;
	public Maze maze;
	public int playerlvl;
	public int[][][] lvlsafeall;
	public Player player;
	public Levelmanagement lvl;
	
	private int playerID = 0;
	private int monsterID = 0;
	
	public ArrayList<Monster> MonsterList = new ArrayList<Monster>();
	public ArrayList<Player> PlayerList = new ArrayList<Player>();

	//Konstruktor erstellt eine Spielwelt mit LevelID =1
	public Levelmanagement(Player player) {
		this.player=player;
		newLevel(1,19);
	}
	

	// Diese Methode erzeugt ein Map nach Vorgaben vom Levelgenerator
	public void newLevel(int LevelID, int size) {
		//this.lvlcounter = lvlcount;
		int type =0;
		this.LevelID = LevelID;
		Levelmanagement.size =size;
		//lvlsafeall = new int [lvlcounter][size][size];
		lvlMaze = new int[size][size];
		
		// erstelle neues "Maze" mit Hilfe des Konstruktors der Klasse Maze
		
		 maze = new Maze(LevelID);
		 for (int i=0 ; i <size -1; i++){
			for (int j= 0; j<size -1; j++){
				this.lvlMaze[i][j]=maze.Map[i][j];
			}
			
		 }

		// Speicher im Spieler welches Level er zuletzt gespielt hat
		// Lvlindex geht von 1,...
		
		player.setPlayerLvl(LevelID);
		
		for (int i=0; i<maze.Map.length; i++){
			for (int j=0;j<maze.Map.length;j++){
				if (maze.Map[i][j]==2){
					player.setPos(i,j);
					player.setPlayerID(playerID);
					playerID++;
				}
				else if (maze.Map[i][j]== 6){
					
					Monster monster = new Monster(monsterID, i , j,this,  type );
					MonsterList.add(monster);
					monsterID++;	
				}
				else if(maze.Map[i][j] == 5){
					
					Monster monster = new Monster(monsterID, i, j, this , type);
					MonsterList.add(monster);
					monsterID++;
			}
				else if (maze.Map[i][j]== 3){
					doorx =j;
					doory =i;
				}}
		}

	}
		
		public ArrayList<Monster> getMonsterList(){
			return MonsterList;
		}
		
		
		
	//Diese Methode setzt einen Spieler zurueck an den Anfang des levels
	public void placePlayer(int playerID) {

		for (int i = 0; i < PlayerList.size(); i++) {
			Player player = PlayerList.get(i);
			if (playerID == player.playerID) {
				for (int j = 0; j < size; j++) {
					for (int k = 0; k < size; k++) {
						if (player.playerMap[j][k] == 2) {
							player.setXPos(j);
							player.setYPos(k);
						}
					}
				}
			}
		}
	}
	public void setLvlMazePosition(int i,int j,int k){
		 this.lvlMaze[i][j]=k;
	}
	public int getLvlMazePosition(int i,int j){
		return lvlMaze[i][j];
	}
	public int[][] getLvlMaze(){
		return lvlMaze;
	}
	public int getLevelID() {
		return LevelID;
	}
	public Player getPlayer() {
		return player;
	}
}
