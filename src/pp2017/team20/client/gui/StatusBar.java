package pp2017.team20.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasse in der die Statusleiste des Spielfensters erstellt wird
 * 
 * @author Heck, Liz, 5991099
 *
 */
public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image floor, star, elixir, player;

	private GamingArea window;

	/**
	 * Hier werden die Bilder geladen
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public StatusBar(GamingArea window) {
		this.window = window;

		// Bilder werden geladen und ein moeglicher Fehler wird abgefangen
		try {
			floor = ImageIO.read(new File("img//floor.jpg"));
			star = ImageIO.read(new File("img//star.png"));
			elixir = ImageIO.read(new File("img//elixir.png"));
			player = ImageIO.read(new File("img//player.png"));
		} catch (Exception e) {
			System.err.println("Image could not be found.");
		}
	}

	/**
	 * Hier wird die Statusleiste mit den einzelnen Elementen gezeichnet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void paint(Graphics g) {

		// zuerst wird alles schwarz gemalt
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// hier wird nun der Hintergrund gezeichnet
		for (int i = 0; i < this.getWidth(); i++) {
			g.drawImage(floor, i * window.BOX, 0, null);
		}

		// das Symbol und der Name des Spielers werden links unter das Spielfeld
		// gezeichnet
		g.drawImage(player, 190, 0, window.BOX, window.BOX, null);
		g.setColor(Color.WHITE);
		g.drawString("" + window.player.getName(), window.BOX * (window.WIDTH - 13), 25);

		// das Symbol fuer die Lebenstraenke und die Anzahl werden in die Mitte
		// unter das Spielfeld gezeichnet
		g.drawImage(elixir, 510, 0, window.BOX, window.BOX, null);
		g.drawString("" + window.elixirs, window.BOX * (window.WIDTH - 5), 25);

		// hier wird die benoetigte Zeit des aktuellen Spiels angezeigt
		g.drawString("Time: 0", window.BOX * (window.WIDTH + 1), 25);

		// hier wird das Level in dem sich der Spieler grade befindet angezeigt
		g.drawString("Level: 1", window.BOX * (window.WIDTH + 3) - 5, 25);

		// hier wird die Lebensanzeige des Spielers gezeichnet
		g.setColor(Color.GREEN);
		g.fillRect((window.WIDTH / 2) * window.BOX - 60, getHeight() - 20, 150, 9);

		// falls der Spieler den Stern gefunden und aufgenommen hat, wird dieser
		// ebenfalls in der Statusleiste angezeigt
		if (window.starFound == true) {
			g.drawImage(star, 460, 0, window.BOX, window.BOX, null);
		}

	}

}