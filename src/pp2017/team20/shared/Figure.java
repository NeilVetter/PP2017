package pp2017.team20.shared;

import java.awt.Image;

public abstract class Figure {
	
	/**
	 * @author HindiBones
	 * (minimale Erg�nzung: Sell, Robin, 6071120)
	 */

	private int xPos, yPos;
	private Image image;
	
	private int health;
	private int damage;
	private int maxHealth;
	private int mana;
	private int maxMana;
		
	
	// Getter und Setter
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
	}
	
	public int getMaxMana(){
		return maxMana;
	}
	
	public void setMaxMana(int maxMana){
		this.maxMana = maxMana;
	}
	
	public void setDamage(int damage){
		this.damage = damage;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public void changeHealth(int change){
		health = Math.min(health + change, getMaxHealth());
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setMana(int mana){
		this.mana = mana;
	}
	
	public int getMana(){
		return mana;
	}
	
	public Image getImage(){
		return image;
	}
	
	public void setImage(Image img){
		image = img;
	}
	
	public void setPos(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public void up(){
		yPos--;
	}
	
	public void right(){
		xPos++;
	}
	
	public void down(){
		yPos++;
	}
	
	public void left(){
		xPos--;
	}
	
}
