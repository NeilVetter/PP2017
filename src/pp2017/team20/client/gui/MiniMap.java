package pp2017.team20.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import pp2017.team20.shared.sendObject;

/**
 * Klasse in der die MiniMap des Spielfensters erstellt wird, auf der das Level
 * in verkleinerter Form dargestellt ist
 * 
 * @author Heck, Liz, 5991099
 *
 */

public class MiniMap extends JPanel {

	private static final long serialVersionUID = 1L;

	private GamingArea window;

	/**
	 * Hier wird das Layout der MiniMap festgelegt
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public MiniMap(GamingArea window) {

		this.window = window;
		this.setLayout(new BorderLayout());

		// Groesse der Minimap + Hintergrundfarbe werden festgelegt
		this.setPreferredSize(new Dimension((window.BOX * window.WIDTH) / 4, (window.BOX * window.HEIGHT) / 4));
		this.setBackground(Color.GRAY);
		this.setOpaque(true);

	}

	/**
	 * Hier wird die MiniMap gezeichnet
	 * 
	 * @author Heck, Liz, 5991099
	 * 
	 */
	public void paint(Graphics g) {
		super.paintComponent(g);

		// alle Eintraege des Testlevels (aus GamingArea) werden abgefragt, und
		// mit unterschiedlich farblichen Rechtecken in der MiniMap dargestellt

		for (int i = 0; i < window.WIDTH; i++) {
			for (int j = 0; j < window.HEIGHT; j++) {

				switch (window.level.getLvlMazePosition(i, j)) {
				case 0:
					g.setColor(Color.BLACK);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;

				case 1:
					g.setColor(Color.WHITE);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;
				case 2:
					g.setColor(Color.WHITE);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;

				case 3:
					g.setColor(Color.GREEN);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;

				case 4:
					g.setColor(Color.CYAN);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;

				case 5:
					g.setColor(Color.YELLOW);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;
				case 6:
					g.setColor(Color.WHITE);
					g.fillRect(i * (window.BOX / 4), j * (window.BOX / 4), window.BOX / 4, window.BOX / 4);
					break;

				default:
					System.err.println("Falsche Matrix");
				}
			}

			// hier wird der Spieler als pinkes Kaestchen gezeichnet, der sich
			// auf
			// der
			// MiniMap mitbewegt, in dem immer die aktuelle Position abgefragt
			// wird
			g.setColor(Color.MAGENTA);
			g.fillRect(window.player.getXPos() * ((window.BOX * window.WIDTH) / 4) / window.WIDTH,
					window.player.getYPos() * ((window.BOX * window.HEIGHT) / 4) / window.HEIGHT,
					((window.BOX * window.WIDTH) / 4) / window.WIDTH,
					((window.BOX * window.HEIGHT) / 4) / window.HEIGHT);

			// hier werden die Monsterr jeweils als rotes Kaestchen gezeichnet,
			// das sich auf
			// der
			// MiniMap mitbewegt, in dem immer die aktuelle Position abgefragt
			// wird
			g.setColor(Color.RED);
			for (sendObject o : window.monster) {
				g.fillRect(o.posX * ((window.BOX * window.WIDTH) / 4) / window.WIDTH,
						o.posY * ((window.BOX * window.HEIGHT) / 4) / window.HEIGHT,
						((window.BOX * window.WIDTH) / 4) / window.WIDTH,
						((window.BOX * window.HEIGHT) / 4) / window.HEIGHT);
			}

			repaint();
		}
	}
}
