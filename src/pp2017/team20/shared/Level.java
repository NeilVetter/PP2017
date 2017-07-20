package pp2017.team20.shared;

import java.util.LinkedList;
import java.io.Serializable;



import pp2017.team20.server.engine.Levelmanagement;
import pp2017.team20.server.map.Maze;

import java.io.Serializable;
import java.util.LinkedList;

public class Level implements Serializable {

	public int LevelID;
	public int[][] lvlMaze;
	private int xPos;
	private int yPos;
	public LinkedList<Monster> monsterList = new LinkedList<Monster>();
	public Levelmanagement lvl;
	
	
	
			
	public GameElement[][] gamearea = new GameElement [Maze.size][Maze.size];
	
	public int[][] monsterfield = new int[Maze.size][Maze.size];
	
	
	public Level(Levelmanagement lvl){
		this.lvl=lvl;
//		for (int i= 0; i< monsterfield.length; i++){
//			for( int j= 0; j<monsterfield.length;j++){
//				monsterfield[i][j]=-1;
//			}
//		}
	}
	
	public Level(int id, int[][] level){
		LevelID= id;
		lvlMaze = level;
	}
	
	public int getLevelID(){
		return LevelID;
	}
	
	public void setLvlMaze(int i,int j, int maze ){
		lvlMaze[i][j]=maze;
	}
	public Levelmanagement getLvl(){
		return lvl;
	}

	
	
	/**Setzen Startpositionen fest? wie wichtig f�r uns @Tobi */
	public int getxPos(){
		return xPos;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	// Setzt Startposition im Level
	public void setPos(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}



	public int getlvlMazePostion(int xPos, int yPos) {
		
		return lvlMaze[xPos][yPos];
	}
	public int [][]getlvlMaze() {
		
		return lvlMaze;
	}
}
