package pp2017.team20.client.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasse in der die Erklaerung der Steuerung erstellt wird
 * 
 * @author Heck, Liz, 5991099
 *
 */
public class Control extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image floor, control;

	/**
	 * Hier werden der Hintergrund und das Bild mit den Tastaturbefehlen
	 * gezeichnet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void paint(Graphics g) {

		// Bilder werden geladen und ein moeglicher Fehler wird abgefangen
		try {
			control = ImageIO.read(new File("img//control.png"));
			floor = ImageIO.read(new File("img//floor.jpg"));
		} catch (IOException e) {
		}

		// Hintergrund wird gezeichnet
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				g.drawImage(floor, 32 * i, 32 * j, null);
			}
		}
		// Bild mit Tastaturbefehlen wird gezeichnet
		g.drawImage(control, 0, 0, null);

	}

}
