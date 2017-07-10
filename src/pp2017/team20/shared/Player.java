package pp2017.team20.shared;

import pp2017.team20.server.map.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


//Klasse f�r ein Objetk der types "Player"
//Neil Vetter 6021336

// erbt von der Klasse "Figur", �bernommen aus HndiBones
public class Player extends Figure{

	public int PosY;
	public int PosX;
	public int PlayerLvl;
	public int [][] PlayerMap;
	public static ArrayList<Maze> LevelList=new ArrayList<Maze>();
	public ArrayList<Item> ItemList = new ArrayList<Item>();
	public int PlayerID;
	public String playername;
	private String pasword;
	public int HealthPotNumber;
	public int ManaPotNumber;
	public boolean loggedIN;
	public boolean GotKey;
	public boolean Door;
	
	//Konstruktor f�r die Anmeldung(Macht jetzt Datebank)
	public Player (String playername, String pasword,int PosX,int PosY,int PlayerID,boolean loggedIN){
		this.playername=playername;
		this.pasword=pasword;
		this.PosX=PosX;
		this.PosY=PosY;
		this.PlayerID=PlayerID;
		this.loggedIN=loggedIN;
	}
	//Konstruktor um einen neuen Spieler zu erstellen
	//Nach zusammensetzen ohne PosX/PosY
	public Player (int PlayerID,int PosX,int PosY,String playername){
		this.PlayerID=PlayerID;
		this.PosX=PosX;
		this.PosY=PosY;
		this.playername=playername;
		setHealth(100);
		setMana(100);
		setMaxHealth(getHealth());
		setMaxMana(getMana());
		setHealthPotNumber(0);
		setManaPotNumber(0);
		GotKey=false;
		Door=false;
		loggedIN=false;
		PlayerMap= new int [15][15];
		
//		// Bild fuer den Spieler laden
//		try {
//			setImage(ImageIO.read(new File(imgDatei)));
//		} catch (IOException e) {
//			System.err.print("Das Bild "+ imgDatei + " konnte nicht geladen werden.");
//		}
	}
	
	public String getPlayername(){
		return playername;
	}
	public String getPasword(){
		return pasword;
	}
	public int getPosX(){
		return PosX;
	}
	public int getPosY(){
		return PosY;
	}
	public int getHealthPotNumber() {
		return HealthPotNumber;
	}
	public void setHealthPotNumber(int HealthPotNumber){
		this.HealthPotNumber=HealthPotNumber;
	}
	public int getManaPotNumber(){
		return ManaPotNumber;
	}
	public void setManaPotNumber(int ManaPotNumber){
		this.ManaPotNumber=ManaPotNumber;
	}
	public void setPlayerLvl(int PlayerLvl){
		this.PlayerLvl=PlayerLvl;
	}
}
