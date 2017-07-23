package pp2017.team20.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.swing.JFrame;

import pp2017.team20.client.gui.Highscore;
import pp2017.team20.client.comm.ClientCommunication;
import pp2017.team20.client.engine.ClientEngine;
import pp2017.team20.client.gui.GamingWorld;
import pp2017.team20.client.gui.StatusBar;
import pp2017.team20.shared.Level;
import pp2017.team20.shared.Monster;
import pp2017.team20.shared.Player;
import pp2017.team20.shared.sendObject;

/**
 * Klasse in der das Spielfenster erstellt wird
 * 
 * @author Heck, Liz, 5991099
 * 
 */

public class GamingArea extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;

	private GamingWorld gamingworld;
	private Highscore highscore;
	private Control control;
	private StatusBar statusbar;
	private MenuBar menubar;
	public Chat chat;
	private MiniMap map;

	public ClientCommunication communication = new ClientCommunication();
	public ClientEngine engine;

	public Player player;
	public ArrayList<Monster> monsterList;
	public ArrayList<Monster> buffermonsterList;
	public ArrayList<sendObject> monster = new ArrayList<sendObject>();

	public Level level;
	public int clientID;
	public int id;
	public int attackID;
	public int defendID;
	public int playerID;
	public int monsterID;
	public int time;
	public String user;
	public String loginName;
	public boolean success = false;
	public boolean firstLogIn = true;
	public String password;
	public SecretKey keylog;

	public int currentLevel = 0;
	public long startTime;
	public int neededTime;
	public boolean gameEnd = false;
	public boolean gameLost = false;

	public boolean controlShown = false;
	public boolean highscoreShown = false;

	// hier wird die Breite und Hoehe des gesamten Spielfeldes festgelegt
	public final int WIDTH = 19;
	public final int HEIGHT = 19;
	public final int BOX = 32;

	/**
	 * Diese Methode wird von der Main Methode in der Startklasse aufgerufen
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public GamingArea(String title) {
		this.player = new Player();
		monsterList = new ArrayList<Monster>();
		buffermonsterList = new ArrayList<Monster>();

		communication.connectServer();
		communication.start();
		engine = new ClientEngine(communication, this);
		initializeJFrame(title);

		startNewGame();

	}

	/**
	 * Hier werden die Einstellungen fuer das Spielfeld gesetzt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void initializeJFrame(String title) {

		this.setLayout(new BorderLayout());
		this.gamingworld = new GamingWorld(this);
		this.menubar = new MenuBar(this);
		this.statusbar = new StatusBar(this);
		this.highscore = new Highscore();
		this.control = new Control();
		this.chat = new Chat(this, user);
		this.map = new MiniMap(this);

		// Hier werden Breite und Hoehe der einzelnen Elemente des Spielfeldes
		// festgelegt
		gamingworld.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		statusbar.setPreferredSize(new Dimension(WIDTH + 6 * BOX, BOX));
		highscore.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		control.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		chat.setPreferredSize(new Dimension(6 * BOX, HEIGHT));
		map.setPreferredSize(new Dimension((BOX * WIDTH) / 4, BOX));

		/**
		 * Ruft das Registration Fenster auf
		 * 
		 * @author Hamid, Kirli , 6041663
		 * 
		 */

		Registration windowTest = new Registration(this);
		windowTest.window.setVisible(true);

		while (!success) {

			engine.receiveMessage();

		}

		// Standadeinstellungen (Groesse des Fensters nicht veraenderbar, Titel
		// setzen, sichtbar machen, schliessbar machen)
		this.addKeyListener(this);
		this.setResizable(false);
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		showGamingWorld();

		// das Spielfenster wird auf dem Bildschirm zentriert
		final Dimension d = this.getToolkit().getScreenSize();
		this.setLocation((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d.getHeight() - this.getHeight()) / 2));
	}

	/**
	 * Hier wird das Spielfenster gezeichnet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void showGamingWorld() {
		// zuerst werden Highscore und Steuerungs Anzeige entfernt
		highscoreShown = false;
		controlShown = false;
		this.remove(highscore);
		this.remove(control);

		// die einzelnen Komponenten des Spielfeldes werden hinzugefuegt
		// und ausgerichtet
		this.add(gamingworld, BorderLayout.CENTER);
		this.add(menubar, BorderLayout.NORTH);
		this.add(statusbar, BorderLayout.SOUTH);
		this.add(chat, BorderLayout.EAST);
		this.add(map, BorderLayout.WEST);

		// das fertige Spielfeld wird aktiviert
		this.setVisible(true);

		this.pack();
		this.requestFocus();
	}

	/**
	 * hier wird die Anzeige des Highscores erstellt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void showHighscore() {
		highscoreShown = true;
		controlShown = false;

		// alle Elemente des Spielfeldes werden entfernt
		this.remove(gamingworld);
		this.remove(statusbar);
		this.remove(control);

		// Highscore wird hinzugefuegt
		this.add(highscore, BorderLayout.CENTER);

		// das Spielfeld wird aktiviert
		this.requestFocus();
		this.pack();
		highscore.repaint();
	}

	/**
	 * hier wird die Anzeige der Steuerung erstellt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void showControl() {
		highscoreShown = false;
		controlShown = true;

		// alle Elemente des Spielfeldes werden entfernt
		this.remove(gamingworld);
		this.remove(statusbar);
		this.remove(highscore);

		// Steuerungs-Erklaerung wird hinzugefuegt
		this.add(control, BorderLayout.CENTER);

		// das Spielfeld wird aktiviert
		this.requestFocus();
		this.pack();
		control.repaint();
	}

	// Getter fuer die Spielflaeche bzw. Statusleiste
	public GamingWorld getGamingWorld() {
		return gamingworld;
	}

	public StatusBar getStatusBar() {
		return statusbar;
	}

	public Highscore getHighscore() {
		return highscore;
	}

	/**
	 * Hier werden die Tastaturbefehle verarbeitet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void keyPressed(KeyEvent e) {

		// aktuelle Position des Spielers wird uebergeben
		int xPos = player.getXPos();
		int yPos = player.getYPos();
		if (!gameEnd) {
			// wenn die Pfeiltaste nach oben gedrueckt wird und das Feld ueber
			// dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach oben
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (yPos > 0 && !(level.getLvlMazePosition(xPos, yPos - 1) == 0)) {
					yPos = yPos - 1;
					engine.sendMoveMessage(clientID, xPos, yPos, player.playerID);
				}
			}
			// wenn die Pfeiltaste nach unten gedrueckt wird und das Feld unter
			// dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach unten
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (yPos < HEIGHT - 1 && !(level.getLvlMazePosition(xPos, yPos + 1) == 0)) {
					yPos = yPos + 1;
					engine.sendMoveMessage(clientID, xPos, yPos, player.playerID);
				}
			}
			// wenn die Pfeiltaste nach links gedrueckt wird und das Feld links
			// neben dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach links
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (xPos > 0 && !(level.getLvlMazePosition(xPos - 1, yPos) == 0)) {
					xPos = xPos - 1;
					engine.sendMoveMessage(clientID, xPos, yPos, player.playerID);
				}
			}
			// wenn die Pfeiltaste nach rechts gedrueckt wird und das Feld
			// rechts neben dem
			// aktuellen Feld des Spielers keine Wand ist, bewege den Spieler
			// ein Feld nach rechts
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (xPos < WIDTH - 1 && !(level.getLvlMazePosition(xPos + 1, yPos) == 0)) {
					xPos = xPos + 1;
					engine.sendMoveMessage(clientID, xPos, yPos, player.playerID);
				}
			}
			// wenn Taste Q gedrueckt wird und ein Monster in der Naehe des
			// Spielers ist, wird dieses angegriffen und in seinen Lebenspunkten
			// geschwaecht
			else if (e.getKeyCode() == KeyEvent.VK_Q) {
				for (sendObject m : monster) {
					int distanceOffet = Math.abs((player.getXPos() - m.posX)) + Math.abs(player.getYPos() - m.posY);
					if (distanceOffet <= 1) {
						engine.sendAttackMessage(clientID, player.playerID, m.ID, -1 * player.getDamage());
					}
				}
			}
			// wenn Taste B gedrueckt wird und der Spieler einen oder mehrere
			// Traenke in seinem Inventar
			// hat, reduziere Trankanzahl in der Statusleiste um 1 und setze
			// Lebensanzeige wieder voll
			else if (e.getKeyCode() == KeyEvent.VK_B) {

				if (player.healthPotNumber > 0) {
					engine.sendUsePotionMessage(clientID, id, player.playerID);
				}

			}
			// wenn Escape gedrueckt wird, schliesse das Spielfenster
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
		}
		// wenn Leertaste gedrueckt wird und der Spieler auf einem Feld ist auf
		// dem der Stern oder
		// ein Trank liegt, nehme Stern/Trank auf und fuege Stern in
		// Statusleiste hinzu bzw. erhoehe Trankanzahl um 1
		// wenn der Spieler auf der offenen Tuer steht und den Schluessel
		// aufgenommen hat, gehe ins naechste Level
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (level.getLvlMazePosition(xPos, yPos) == 5) {
				engine.sendCollectKeyMessage(clientID);
				level.lvlMaze[xPos][yPos] = 1;
			} else if (level.getLvlMazePosition(xPos, yPos) == 4) {
				level.lvlMaze[xPos][yPos] = 1;
				engine.sendCollectPotionMessage(clientID, xPos, yPos);
			} else if (level.getLvlMazePosition(xPos, yPos) == 3) {
				if (level.getLvlMazePosition(xPos, yPos) == 3 && player.ownsKey()) {
					engine.sendNextLevelMessage(clientID);
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Hier wird das Spiel resettet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void resetGame() {

		monsterList = new ArrayList<Monster>();

		if (buffermonsterList != null) {
			for (int i = 0; i < buffermonsterList.size(); i++) {
				Monster element = buffermonsterList.get(i);
				element = new Monster(monsterID, element.getXPos(), element.getYPos(), engine.level.getLvl(),
						element.getType());
				monsterList.add(element);
			}
		}
		currentLevel = 0;
		gameEnd = false;
		gameLost = false;

		startTime = System.currentTimeMillis();

	}

	/**
	 * Hier wird das Spiel gestartet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void startNewGame() {
		resetGame();
		do {
			if (!gameEnd) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				engine.receiveMessage();
				getGamingWorld().repaint();
				getStatusBar().repaint();
				if (player.getHealth() <= 0) {
					gameEnd = true;
					gameLost = true;
				}
			} else {
				neededTime = (int) ((System.currentTimeMillis() - startTime) / 1000);

				if (gameLost) {
					engine.sendHighscoreMessage(clientID, user, time);
					getHighscore().repaint();
				} else {
					getGamingWorld().repaint();
					engine.receiveMessage();
				}
			}
		} while (success);

	}

	/**
	 * Hier wird der LogIn ausgefuehrt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void login(String username, String password) {
		// Username und Passwort werden an den Client uebermittelt
		chat = new Chat(this, username);
		engine.sendLogInMessage(clientID, username, password);
	}

	// benoetigte Getter und Setter
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setPlayer(Player player) {
		this.player = player;

	}

	public Player getPlayer() {
		return player;
	}

}
