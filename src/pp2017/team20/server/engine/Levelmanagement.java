package pp2017.team20.server.engine;

import pp2017.team20.shared.*;

//Neil Vetter 6021336
//Diese Klasse erzeugt erst, und speichert dann das Level in einem Array

public class Levelmanagement {

	// Erstellt ein Objekt vom Typ "Player"
	public Player player;
	
	//
	public int LevelID = -1;
	public int size=15;
	
	public Levelmanagement(Player player) {
		this.player = player;
	}

	//Diese Methode erzeugt ein Map nach Vorgaben vom Levelgenerator
	public void newLevel(int LevelID) {

		//erstelle neues "Maze" mit Hilfe des Konstruktors der Klasse Maze
		Maze maze = new Maze(LevelID);

		//F�gt das Spiel zur Levelliste des eingeloggten Spielers hinzu
		Player.LevelList.add(maze);

		//Speicher im Spieler welches Level er zuletzt gespielt hat
		//Lvlindex geht von 0,1,...
		player.PlayerLvl = LevelID;

		//Speichert die Map beim Spieler, sodass Elemente darauf abgelesen werden k�nnen
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				player.PlayerMap[i][j] = maze.Map[i][j];
			}
		}
		
		//Testmap
		int[][] test = new int[15][15];
		
		//Testweises Ausgeben der Map, wie in der Klasse "Maze"
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				test[i][j] = maze.Map[i][j];
				System.out.print(test[i][j]);
				}
				System.out.println();
		}
	}
	
	//Diese Methode setzt einen Spieler zur�ck an den anfang des levels
	//(des von ihm gespeicherten)
	//z.B. nach seinem tot
	public void placePlayer(Player player){
	
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(player.PlayerMap[i][j] ==2){
					player.PosX=i;
					player.PosY=j;
				}
			}
		}
	}
}
