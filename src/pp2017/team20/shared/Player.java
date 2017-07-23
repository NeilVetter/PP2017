package pp2017.team20.shared;

import pp2017.team20.client.gui.GamingArea;
import pp2017.team20.server.map.*;
import java.io.Serializable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import pp2017.team20.shared.Monster;


/** 
 * Klasse fuer ein Objetk der types "Player"
 * @autor Neil, Vetter, 6021336
 * 
 * */


public class Player extends Figure implements Serializable {
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
	public int impactHealth;
	public boolean loggedIN;
	public boolean ownsKey;
	public boolean door;
	
	
	private GamingArea window;
	
	//Konstruktor um einen neuen Spieler zu erstellen
	//Nach zusammensetzen ohne PosX/PosY
	public Player (){
		setHealth(100);
		setMana(100);
		setMaxHealth(getHealth());
		setMaxMana(getMana());
		setHealthPotNumber(0);
		setManaPotNumber(0);
		ownsKey=false;
		door=false;
		loggedIN=false;
	}
	
	// Methode, um den Schluessel zu entfernen
	public void removeKey(){
		ownsKey = false;
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
	
	public void collectPotion() {
		healthPotNumber++;
	}
	
	public int usePotion() {
		if (healthPotNumber > 0) {
			setHealthPotNumber(healthPotNumber - 1);
			return impactHealth;
		} else
			return 0;
	}

	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID){
		this.playerID=playerID;
	}
}
