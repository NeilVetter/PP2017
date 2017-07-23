package pp2017.team20.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Klasse in der die Menuleiste des Spielfensters erstellt wird
 * 
 * @author Heck, Liz, 5991099
 */
public class MenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMenu game;
	private JMenu display;
	private JMenu help;

	private JMenuItem newGame;
	private JMenuItem exitGame;
	private JMenuItem logOut;
	private JMenuItem highscore;
	private JMenuItem control;

	private GamingArea window;

	/**
	 * Hier werden alle Menu- und Menuunterpunkte erstellt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public MenuBar(GamingArea window) {
		this.window = window;

		game = new JMenu("Spiel");
		display = new JMenu("Highscore");
		help = new JMenu("Hilfe");

		newGame = new JMenuItem("Neues Spiel");
		exitGame = new JMenuItem("Spiel Beenden");
		logOut = new JMenuItem("Ausloggen");
		highscore = new JMenuItem("Highscore anzeigen");
		control = new JMenuItem("Steuerung");

		newGame.addActionListener(this);
		exitGame.addActionListener(this);
		logOut.addActionListener(this);
		highscore.addActionListener(this);
		control.addActionListener(this);

		game.add(newGame);
		game.add(exitGame);
		game.add(logOut);
		display.add(highscore);
		help.add(control);

		this.add(game);
		this.add(display);
		this.add(help);
	}

	/**
	 * Hier werden den Menu-Unterpunkten Funktionen zugeteilt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame) {
			window.resetGame();
		} else if (e.getSource() == highscore) {
			if (window.highscoreShown) {
				window.showGamingWorld();
				highscore.setText("Zeige Highscore");
			} else {
				window.showHighscore();
				highscore.setText("Zeige Spielfeld");
			}
		} else if (e.getSource() == exitGame) {
			System.exit(0);
		} else if (e.getSource() == logOut) {
			window.engine.sendLogOutMessage(window.playerID);
			System.exit(0);
		} else if (e.getSource() == control) {
			if (window.controlShown) {
				window.showGamingWorld();
				control.setText("Steuerung");
			} else {
				window.showControl();
				control.setText("Zeige Spielfeld");
			}
		} else if (e.getSource() == control){
			
		}
	}
}
