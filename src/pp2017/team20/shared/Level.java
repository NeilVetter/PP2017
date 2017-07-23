package pp2017.team20.shared;

import java.util.LinkedList;
import java.io.Serializable;



import pp2017.team20.server.engine.Levelmanagement;
import pp2017.team20.server.map.Maze;

import java.io.Serializable;
import java.util.LinkedList;

/** Setter - Getter für das Level 
 * 
 * @author Hamid, Kirli 6041663
 * @author Neil, Vetter 6021336
 * */
public class Level implements Serializable {

	public int LevelID;
	public int[][] lvlMaze;
	private int xPos;
	private int yPos;
	public LinkedList<Monster> monsterList = new LinkedList<Monster>();
	public Levelmanagement lvl;
	
	
	
			
	public GameElement[][] gamearea = new GameElement [Maze.size][Maze.size];
	
	public int[][] monsterfield = new int[Maze.size][Maze.size];
	
	
	public Level(Levelmanagement lvl,int [][]lvlMaze){
		this.lvl=lvl;
		this.lvlMaze=lvlMaze;
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
	public int getLvlMazePosition(int i,int j){
		return lvlMaze[i][j];
	}
	
	
	/**Setzen Startpositionen fest */
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

	public int [][]getlvlMaze() {
		
		return lvlMaze;
	}
}
