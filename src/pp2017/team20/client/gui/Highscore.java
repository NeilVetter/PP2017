package pp2017.team20.client.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasse in der die Highscore Anzeige erstellt wird
 * 
 * @author Heck, Liz, 5991099
 *
 */
public class Highscore extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image floor, highscore;

	/**
	 * Hier werden der Hintergrund und die Ueberschrift gezeichnet
	 * 
	 * @author Heck, Liz, 599100
	 */
	public void paint(Graphics g) {

		// Bilder werden geladen und ein moeglicher Fehler wird abgefangen
		try {
			highscore = ImageIO.read(new File("img//highscore.png"));
			floor = ImageIO.read(new File("img//floor.jpg"));
		} catch (IOException e) {
		}

		// Hintergrund wird gezeichnet
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				g.drawImage(floor, 40 * i, 40 * j, null);
			}
		}
		// Ueberschrift wird gezeichnet
		g.drawImage(highscore, 0, 0, null);

	}

}
