package pp2017.team20.shared;

import pp2017.team20.client.gui.GamingArea;
import pp2017.team20.server.map.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;


//Klasse f�r ein Objetk der types "Player"
//Neil Vetter 6021336

// erbt von der Klasse "Figur", �bernommen aus HndiBones
public class Player extends Figure{

	public int yPos;
	public int xPos;
	public int playerLvl;
	public int [][] playerMap;
	public static ArrayList<Maze> LevelList=new ArrayList<Maze>();
	public ArrayList<Item> ItemList = new ArrayList<Item>();
	public ArrayList<Monster> monsterList = new ArrayList<Monster>();
	public int playerID;
	public String playername;
	private String pasword;
	public int healthPotNumber;
	public int manaPotNumber;
	public boolean loggedIN;
	public boolean ownsKey;
	public boolean door;
	
	
	private GamingArea window;
	
	public Player(String imgFile, GamingArea window){
		this.window = window;

		setHealthPotNumber(0);
		setPos(0, 0);
//		setLeben(100);
//		setMaxLeben(getLeben());
//		setName("Hindi Bones");
//		setSchaden(8);
		
		// Bild fuer den Spieler laden
		try {
			setImage(ImageIO.read(new File(imgFile)));
		} catch (IOException e) {
			System.err.print("Das Bild "+ imgFile + " konnte nicht geladen werden.");
		}
	}
	
	//Konstruktor f�r die Anmeldung(Macht jetzt Datebank)
	public Player (String playername, String pasword,int PosX,int PosY,int PlayerID,boolean loggedIN){
		this.playername=playername;
		this.pasword=pasword;
		this.xPos=PosX;
		this.yPos=PosY;
		this.playerID=PlayerID;
		this.loggedIN=loggedIN;
	}
	//Konstruktor um einen neuen Spieler zu erstellen
	//Nach zusammensetzen ohne PosX/PosY
	public Player (int PlayerID,int xPos,int yPos){//,String playername){ // um im Lvlmanager zupassen.
		this.playerID=PlayerID;
		this.xPos=xPos;
		this.yPos=yPos;
		this.playername=playername;
		setHealth(100);
		setMana(100);
		setMaxHealth(getHealth());
		setMaxMana(getMana());
		setHealthPotNumber(0);
		setManaPotNumber(0);
		ownsKey=false;
		door=false;
		loggedIN=false;
		playerMap= new int [15][15];
		
	}
	
	public String getPlayername(){
		return playername;
	}
	public String getPasword(){
		return pasword;
	}
	public int getHealthPotNumber() {
		return healthPotNumber;
	}
	public void setHealthPotNumber(int HealthPotNumber){
		this.healthPotNumber=HealthPotNumber;
	}
	public int getManaPotNumber(){
		return manaPotNumber;
	}
	public void setManaPotNumber(int ManaPotNumber){
		this.manaPotNumber=ManaPotNumber;
	}
	public void setPlayerLvl(int PlayerLvl){
		this.playerLvl=PlayerLvl;
	}
	public boolean ownsKey(){
		return ownsKey;
	}
	
	
	
	
}
