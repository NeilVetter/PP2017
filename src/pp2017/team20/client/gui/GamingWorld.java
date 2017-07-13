package pp2017.team20.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasse in der die Spielflaeche erstellt wird
 * 
 * @author Heck, Liz, 5991099
 */

public class GamingWorld extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image floor, wall, doorOpen, doorClosed, star, elixir, player, wario, waluigi, boo, donkey_kong;
	private GamingArea window;

	public GamingWorld(GamingArea window) {
		this.window = window;

		// Die oben aufgefuehrten Bilder werden geladen und ein moeglicher
		// Fehler wird abgefangen
		try {
			floor = ImageIO.read(new File("img//floor.jpg")); // http://www.edingershops.de/magazin/wp-content/uploads/2016/05/rasen-frisch-gemaeht-1.jpg
			wall = ImageIO.read(new File("img//wall.jpg")); // http://i.imgur.com/RA1VHFU.jpg
			doorOpen = ImageIO.read(new File("img//doorOpen.png")); // http://vignette1.wikia.nocookie.net/mario/images/3/3a/NSMB_Röhre_grün_senkrecht.png/revision/latest?cb=20130521065939&path-prefix=de
			doorClosed = ImageIO.read(new File("img//doorClosed.png")); // https://vignette2.wikia.nocookie.net/mario/images/c/c9/Piranha_Plant%2C_New_Super_Mario_Bros._U.png/revision/latest?cb=20121119025023
			star = ImageIO.read(new File("img//star.png")); // http://vignette4.wikia.nocookie.net/mario/images/d/df/Powerstern.png/revision/latest?cb=20140420194611&path-prefix=de
			elixir = ImageIO.read(new File("img//elixir.png")); // https://cdn.wikimg.net/strategywiki/images/thumb/5/5a/New_SMB_Wii_1-up_mushroom.png/120px-New_SMB_Wii_1-up_mushroom.png
			player = ImageIO.read(new File("img//player.png")); // http://mariopartylegacy.com/wp-content/gallery/Mario-Party%3A-Star-Rush---9/1/mpsrart1.png
			wario = ImageIO.read(new File("img//wario.png")); // http://vignette2.wikia.nocookie.net/fantendo/images/c/c3/Sochi_2014_olympic_wario_3d_render_by_ratchetmario-d8j2g4e.png/revision/latest?cb=20150517231540
			waluigi = ImageIO.read(new File("img//waluigi.png")); // http://static.tvtropes.org/pmwiki/pub/images/waluigi-boardwin-mp9_1391.png
			boo = ImageIO.read(new File("img//boo.png")); // https://vignette1.wikia.nocookie.net/luigi/images/7/7c/Boo.png/revision/latest?cb=20080806155504&path-prefix=de
			donkey_kong = ImageIO.read(new File("img//donkey_kong.png")); // https://s-media-cache-ak0.pinimg.com/originals/c8/4f/92/c84f92638b3373ff3faf0ca6f4899b0c.png
		} catch (Exception e) {
			System.err.println("Image could not be found.");
		}
	}

	/**
	 * Hier wird die Spielflaeche gezeichnet
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public void paint(Graphics g) {

		// zuerst wird alles schwarz uebermalt
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		int i, j;

		// jedes Element im Arrays des Testlevels wird abgefragt und dann
		// gezeichnet
		// 0 = Wand
		// 1 = Boden
		// 2 = geschlossene Tuer
		// 3 = offene Tuer
		// 4 = Monster Donkey Kong
		// 5 = Monster Walugi
		// 6 = Monster Buu Huu
		// 7 = Monster Wario
		// 8 = Stern
		// 9 = Trank
		for (i = 0; i < window.WIDTH; i++) {
			for (j = 0; j < window.HEIGHT; j++) {

				switch (window.level.getlvlMaze(i, j)) {

				case 0:
					g.drawImage(wall, i * window.BOX, j * window.BOX, null);
					break;

				case 1:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					break;

				case 2:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(doorClosed, i * window.BOX, j * window.BOX, null);
					break;

				case 3:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(doorOpen, i * window.BOX, j * window.BOX, null);
					break;

				case 4:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(donkey_kong, i * window.BOX, j * window.BOX, null);
					break;

				case 5:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(waluigi, i * window.BOX, j * window.BOX, null);
					break;

				case 6:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(boo, i * window.BOX, j * window.BOX, null);
					break;

				case 7:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(wario, i * window.BOX, j * window.BOX, null);
					break;

				case 8:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(star, i * window.BOX, j * window.BOX, null);
					break;

				case 9:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(elixir, i * window.BOX, j * window.BOX, null);
					break;

				default:
					System.err.println("Falsche Matrix");
				}

				// hier wird der Spieler an die Position gezeichnet die in
				// der Klasse Player uebergeben wird
				g.drawImage(player, window.player.getXPos() * window.BOX, window.player.getYPos() * window.BOX, null);
				repaint();

			}
		}
	}
}
