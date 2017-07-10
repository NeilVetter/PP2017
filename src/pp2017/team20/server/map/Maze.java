package pp2017.team20.server.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Maze {
	
/** Erstelle ein zuf�lliges Labyrinth mit den Spielelementen
 * 
 *  @author Hamid, Kirli , 6041663
 * 6
 *  **/
	
 	public static int size = 16;
	public int[][] Map = new int [size][size];
	static Coordinate [] floor;
	
	// Lvlip: 0=Wall, 1 = Floor, 2 = Player, 3 = Door, 4 = Elixier, 5=Key, 6 = Monster
	public Maze(int LvL){
		// Lege fest wie viele Monster 
		// und Heiltr�nke sich auf der Karte befinden sollen
		int Elixier = 3;
		int StageMonster = (int) ((Math.random()) * LvL + 1);
		floor = new Coordinate [size*size];
	
	/*Erstellt mit hilfe des Floodfill-Algo
	 * die Wege f�r den Spieler indem er die "W�nde einreisst" */	
	FloodFill floodfiller = new FloodFill(size);
	for(int i = 0;i < size -1  ; i++){
		for(int j = 0; j< size -1 ; j++){
			this.Map[i][j] = floodfiller.Map[i][j];
		}
	}
	// Speichere die Kacheln ab.
	int floorcount = 0;
	for (int i = 0; i< size; i++){
		for (int j = 0; j< size; j++){
			if(Map[i][j] == 1){
				floor[floorcount] = new Coordinate(i,j);
				floorcount++;
			}
		}
	
	}
	// Zuf�llige Position Spieler und Eingangst�r
	int rand = (int)((Math.random() * floorcount -1));
	int rplayer = rand;
	Map[floor[rplayer].xcoord][floor[rplayer].ycoord] = 2;
	
	//Zuf�llige position der "verschlosssenen" Ausgangst�r
	int rpdoor= rand;
	while(rpdoor == rplayer){
		rand = (int) ((Math.random() * floorcount-1));
		rpdoor = rand;
	}
	Map[floor[rpdoor].xcoord][floor[rpdoor].ycoord] = 3;
 

	
	// Zuf�llige Position der Heiltr�nke
	int rand2 =(int) ((Math.random() * floorcount -1));
	int rpelixier = rand2;
	int i=0;
	while(i<Elixier){ //L�uft solange bis alle Heiltr�nke positioniert sind
		rand2 = (int)(Math.random() * floorcount -1) ;
		while(rpelixier == rplayer || rpelixier == rpdoor){
			rand2= (int) (Math.random() * floorcount) -1 ;
			rpelixier = rand2;
		}	
		Map[floor[rpelixier].xcoord][floor[rpelixier].ycoord] = 4;
		i++;
	}
	//Zuf�llige Positionn f�r den Schl�ssel
	int rand3 =(int) ((Math.random() * floorcount) -1);
	int rpkey = rand3;
	while(rpkey == rplayer || rpkey == rpdoor || rpkey == rpelixier ){
		rand3= (int) (Math.random() * floorcount)-1;
		rpkey = rand3;
	}
	Map[floor[rpkey].xcoord][floor[rpkey].ycoord] = 5;
	
	// Zuf�llige Positionen der Monster
	int rand4 =(int) ((Math.random() * floorcount) -1);
	int rpmonster = rand4;
	int j=1;
	while(j<StageMonster){
		while (rpmonster == rplayer || rpmonster == rpdoor || rpmonster == rpelixier || rpmonster == rpkey ){
			rand4 = (int) (Math.random() * floorcount)-1;
					rpmonster =rand4;
		}
		Map[floor[rpmonster].xcoord][floor[rpmonster].ycoord] = 6;
		j++;
	}
	}

// Main Methode zum Testen.	
	
	public static void main(String[] args){
		for ( int k=0; k<10; k++){
		int akutellesLvl=2;
		Maze map = new Maze(akutellesLvl);
		int[][] maptest = new int [size][size];
		for (int i = 0; i < size ; i++) {
			for (int j = 0; j < size ; j++) {
				maptest[i][j]= map.Map[i][j];
				System.out.print(maptest[i][j]);}
			System.out.println();}
		System.out.println();}
		 
	}
	
	
	}

	

