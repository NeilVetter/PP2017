package pp2017.team20.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import pp2017.team20.shared.Player;

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
	private Chat chat;
	private MiniMap map;

	public Player player;

	public long startTime;
	public int neededTime;
	public boolean gameEnd = false;
	public boolean highscoreShown = false;
	public boolean controlShown = false;
	public boolean starFound = false;
	public boolean success = false;
	public int elixirs = 0;

	// hier wird die Breite und Hoehe des gesamten Spielfeldes festgelegt
	public final int WIDTH = 19;
	public final int HEIGHT = 19;
	public final int BOX = 40;

	// hier wird das Testlevel als 2-Dimensionales Array erzeugt
	int[][] level = new int[19][19];

	{
		int i, j;

		// 0 = Wand
		// zuerst wird alles als Wand gespeichert
		for (i = 0; i < 19; i++) {
			for (j = 0; j < 19; j++) {
				level[j][i] = 0;
			}
		}

		// 1 = Boden
		// hier werden die Wege gespeichert
		i = 1;
		for (j = 1; j < 18; j++) {
			level[j][i] = 1;
		}
		i = 3;
		for (j = 14; j < 17; j++) {
			level[j][i] = 1;
		}
		i = 4;
		for (j = 3; j < 8; j++) {
			level[j][i] = 1;
		}
		i = 6;
		for (j = 7; j < 17; j++) {
			level[j][i] = 1;
		}
		i = 8;
		for (j = 2; j < 9; j++) {
			level[j][i] = 1;
		}
		i = 10;
		for (j = 4; j < 7; j++) {
			level[j][i] = 1;
		}
		i = 12;
		for (j = 3; j < 5; j++) {
			level[j][i] = 1;
		}
		i = 12;
		for (j = 8; j < 17; j++) {
			level[j][i] = 1;
		}
		i = 15;
		for (j = 2; j < 11; j++) {
			level[j][i] = 1;
		}
		i = 15;
		for (j = 13; j < 16; j++) {
			level[j][i] = 1;
		}
		i = 17;
		for (j = 1; j < 18; j++) {
			level[j][i] = 1;
		}

		j = 1;
		for (i = 2; i < 17; i++) {
			level[j][i] = 1;
		}
		j = 3;
		for (i = 2; i < 7; i++) {
			level[j][i] = 1;
		}
		j = 4;
		for (i = 9; i < 16; i++) {
			level[j][i] = 1;
		}
		j = 7;
		for (i = 2; i < 8; i++) {
			level[j][i] = 1;
		}
		j = 8;
		for (i = 9; i < 12; i++) {
			level[j][i] = 1;
		}
		j = 10;
		for (i = 13; i < 17; i++) {
			level[j][i] = 1;
		}
		j = 11;
		for (i = 2; i < 6; i++) {
			level[j][i] = 1;
		}
		j = 13;
		for (i = 7; i < 16; i++) {
			level[j][i] = 1;
		}
		j = 14;
		for (i = 2; i < 3; i++) {
			level[j][i] = 1;
		}
		j = 15;
		for (i = 14; i < 15; i++) {
			level[j][i] = 1;
		}
		j = 17;
		for (i = 2; i < 17; i++) {
			level[j][i] = 1;
		}

		// 2 = offene Tuer
		level[3][6] = 2;

		// 3 = geschlossene Tuer
		level[15][14] = 3;

		// 4, 5, 6, 7 = Monster
		level[4][15] = 4;
		level[10][12] = 5;
		level[15][6] = 6;
		level[15][3] = 7;

		// 8 = Stern
		level[14][3] = 8;

		// 9 = Lebenstrank
		level[13][9] = 9;
	}

	/**
	 * Diese Methode wird von der Main Methode in der Startklasse aufgerufen
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public GamingArea(String title) {
		initializeJFrame(title);
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
		this.chat = new Chat(this);
		this.map = new MiniMap(this);

		this.player = new Player();

		// hier wird das LogIn Fenster aufgerufen und Benutzername und Passwort
		// werden abgefragt
		Registration.main(null);

		// Hier werden Breite und Hoehe der einzelnen Elemente des Spielfeldes
		// festgelegt
		gamingworld.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		statusbar.setPreferredSize(new Dimension(WIDTH + 6 * BOX, BOX));
		highscore.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		control.setPreferredSize(new Dimension(WIDTH * BOX, HEIGHT * BOX));
		chat.setPreferredSize(new Dimension(6 * BOX, HEIGHT));
		map.setPreferredSize(new Dimension((BOX * WIDTH) / 4, BOX));

		// Standadeinstellungen (Groesse des Fensters nicht veraenderbar, Titel
		// setzen, sichtbar machen, schliessbar machen
		this.addKeyListener(this);
		this.setResizable(false);
		this.setTitle(title);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// hier wird die Methode zugegriffen die das Spielfenster zeichnet
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
		this.requestFocus();
		this.pack();
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

	/**
	 * Hier werden die Tastaturbefehle verarbeitet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void keyPressed(KeyEvent e) {

		// aktuelle Position des Spielers wird übergeben
		int xPos = player.getXPos();
		int yPos = player.getYPos();

		if (!gameEnd) {
			// wenn die Pfeiltaste nach oben gedrueckt wird und das Feld ueber
			// dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach oben
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (yPos > 0 && !(level[xPos][yPos - 1] == 0))
					player.up();
			}
			// wenn die Pfeiltaste nach untenn gedrueckt wird und das Feld unter
			// dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach unten
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (yPos < HEIGHT - 1 && !(level[xPos][yPos + 1] == 0))
					player.down();
			}
			// wenn die Pfeiltaste nach links gedrueckt wird und das Feld links
			// neben dem aktuellen Feld des Spielers keine Wand ist, bewege den
			// Spieler ein Feld nach links
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (xPos > 0 && !(level[xPos - 1][yPos] == 0))
					player.left();
			}
			// wenn die Pfeiltaste nach rechts gedrueckt wird und das Feld
			// rechts neben dem
			// aktuellen Feld des Spielers keine Wand ist, bewege den Spieler
			// ein Feld nach rechts
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (xPos < WIDTH - 1 && !(level[xPos + 1][yPos] == 0))
					player.right();
			}
			// wenn Taste Q gedrueckt wird und ein Monster in der Naehe des
			// Spielers ist, wird dieses angegriffen und in seinen Lebenspunkten
			// geschwaecht
			else if (e.getKeyCode() == KeyEvent.VK_Q) {
				System.out.println("Wenn Monster in der Nähe, attackieren");
			}
			// wenn Taste B gedrueckt wird und der Spieler einen oder mehrere
			// Traenke in seinem Inventar
			// hat, reduziere Trankanzahl in der Statusleiste um 1 und setze
			// Lebensanzeige wieder voll
			else if (e.getKeyCode() == KeyEvent.VK_B) {
				System.out.println("Wenn Heiltrank vorhanden, benutzen");
				if (elixirs > 0) {
					elixirs = elixirs - 1;
				}
				repaint();
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
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (level[player.getXPos()][player.getYPos()] == 8) {
				starFound = true;
				level[player.getXPos()][player.getYPos()] = 1;
				repaint();
				System.out.println("Falls Spieler auf Schlüssel oder Heiltrank, nehme diesen auf");
			} else if (level[player.getXPos()][player.getYPos()] == 9) {
				elixirs = elixirs + 1;
				level[player.getXPos()][player.getYPos()] = 1;
				repaint();
				System.out.println("Falls Spieler auf Schlüssel oder Heiltrank, nehme diesen auf");
			}
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
