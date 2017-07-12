package pp2017.team20.shared;

/** Methode für die Heiltränke 
 * 
 * @autor Kirli Hamid 6041663 */


public class Healthpot extends GameElement {
	private int impact;
	
	// Für LevelManagment 
	public int posx;
	public int posy;
	public boolean take;
	public boolean drop;
	public int healpotID;
	
	public Healthpot(int healpotID, int j, int i){
		this.healpotID= healpotID;
		setdrop(false);
		settake(false);
		setPosx(j);
		setPosy(i);
	}
	public Healthpot(int impact){
		this.impact = impact;
	}
	
	public int getimpact(){
		return impact;
	}

	//getter und setter Methoden
 public int gethealpotID(){
	 return healpotID;
 }
 
 public void setdrop(boolean droped){
	 drop = droped;
 }
 
 public boolean getdrop(){
	 return drop;
 }
 
 public void settake(boolean takehealpot){
	 take = takehealpot;
 }
 
 public boolean gettake(){
	 return take;
 }
 
 public void setPosx(int Posx){
	 posx= Posx;
 }
 
 public void setPosy( int Posy){
	 posy = Posy;
 }
 
 public int getPosy(){
	 return posy;
 }

}
