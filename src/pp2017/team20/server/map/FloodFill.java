package pp2017.team20.server.map;

import java.awt.*;
import java.util.*;
import java.util.List;
/** Baue den FloodFill-Algorithmus auf
 * 
 * @author Hamid, Kirli , 6041663 
 * 
 * **/

public class FloodFill {
     // Hilfswerte zum Speichern von drei Positionen.
	 protected static Point StartCoord;
	 public static Point EndCoord;
	 public static Point NowCoord;
	
	 //Initialisierung von einer leeren Map und der Größe
	 public int[][] Map;
	 protected int size;
	 
	 //Kordinaten
	 private static final int coordUp = 0;
	 private static final int coordDown = 1;
	 private static final int coordLeft = 2;
	 private static final int coordRight = 3;

	
	// Ruft sich selbst auf und baut das "Leere" Labyrinth
	FloodFill (int size){
	Random rand = new Random();
		this.size = size;
		Map = new int [size][size];
		floodfill(new Point(size / 2,size /2 ) , rand);
		//Erstelle eine ArrayListe um "freie" Felder zu fï¿½llen.
		List<Point> free = new ArrayList<Point>();
		for (int i= 0; i<size; i++)
		for (int j=0 ; j<size; j++)
			if(Map[i][j]== 0)
				free.add(new Point(i,j));
		    if(free.size() == 0)
			throw new RuntimeException("There are no free fields in Maze");
		
		//Hilfswerte    
		StartCoord= free.get(rand.nextInt(free.size()));
		NowCoord=StartCoord;
		EndCoord=free.get(rand.nextInt(free.size()));
	
		}
	
	

	// ï¿½berprï¿½ft ob die Position noch auf der Karte ist
	private boolean OnMap(Point p){
		return p.x > 0 && p.y > 0 && p.x < size-1 && p.y < size-1;
	}
	
	
	// setze die Richtungen: OBEN UNTEN LINKS RECHTS
	public Point settingCoord(int way, Point p){
		switch(way){
		case coordUp:
			return new Point (p.x, p.y -1);
		case coordDown:
			return new Point (p.x ,p.y +1);
		case coordLeft:
			return new Point (p.x -1, p.y );
		case coordRight:
			return new Point (p.x +1 , p.y );
			default:
			throw new IllegalArgumentException ( "Fehlerhafte Richtung");
		}
	}
	
	// der eigentliche FloodFill Algorithmus
		private void floodfill(Point p, Random rand) {
			int[] array= new int[4];
			//For schleife um mï¿½glich zufï¿½llige Richtungen zu erhalten
		  	 for(int s = 0; s < array.length; s++){
			    array[s] = rand.nextInt(4);
			    for(int j = 0; j < s; j++){
			        if(array[j] == array[s]){
			             s--;
			             break;  } } }   
			        int case0=array[0];
			        int case1=array[1];
			        int case2=array[2];
			        int case3=array[3];
			
			Map[p.x][p.y] = 1;
		
			//While schleife um die zufï¿½lligen Richtungen zu ï¿½berprï¿½fen
			int i= 0;
			while(i<4){
				if (i == case0 && OnMap(new Point(p.x + 2, p.y))&& Map[p.x + 1][p.y] == 0 && Map[p.x + 2][p.y] == 0 ) {
					Map[p.x + 1][p.y] = 1;
					floodfill(new Point(p.x+2, p.y), rand);
				}
				if (i == case1 && OnMap(new Point(p.x - 2, p.y)) && Map[p.x - 1][p.y] == 0 && Map[p.x - 2][p.y] == 0 ) {
					Map[p.x - 1][p.y] = 1;
					floodfill(new Point(p.x-2, p.y), rand);
				}
				if (i == case2 && OnMap(new Point(p.x, p.y - 2)) && Map[p.x][p.y - 1] == 0 && Map[p.x][p.y - 2] == 0 ) {
					Map[p.x][p.y - 1] = 1;
					floodfill(new Point(p.x, p.y -2), rand);
				}
			
				if (i == case3 && OnMap(new Point(p.x, p.y + 2)) && Map[p.x][p.y + 1] == 0 && Map[p.x][p.y + 2] == 0 ) {
					Map[p.x][p.y + 1] = 1;
					floodfill(new Point(p.x, p.y+2), rand);
				}
			i++;
		}
		}
	}
	
//Quellen: 
////https://www.hackerearth.com/practice/algorithms/graphs/flood-fill-algorithm/tutorial/
////https://www.java-forum.org/thema/labyrinth-mittel-floodfill
