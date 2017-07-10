package pp2017.team20.server.map;

import java.awt.*;

/** 
 * Klasse f�r die Getter und Settermethoden der Klasse FloodFill
 * 
 * @author Hamid, Kirli, 6041663 **/

public class CoordPoints extends FloodFill {
	

	CoordPoints(int size ) {
		super(size);
	
	}

	public int getMapSize(){
		 return size;
	 }
	 
	public Point getStartCoord(){
		return StartCoord;
	}
	
	public Point getEndCoord(){
		return EndCoord;
	}

	
	public Point getNowCoord(){
		return NowCoord;
	}
	
	}

