package pp2017.team20.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pp2017.team20.server.engine.Levelmanagement;
import pp2017.team20.shared.Player;

/**
 * Klasse in der die Spielflaeche erstellt wird
 * 
 * @author Heck, Liz, 5991099
 */

public class GamingWorld extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image floor, wall, doorOpen, doorClosed, key, healthpot, player, monster1, monster2, monster;
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
			key = ImageIO.read(new File("img//key.png")); // http://vignette4.wikia.nocookie.net/mario/images/d/df/Powerstern.png/revision/latest?cb=20140420194611&path-prefix=de
			healthpot = ImageIO.read(new File("img//healthpot.png")); // https://cdn.wikimg.net/strategywiki/images/thumb/5/5a/New_SMB_Wii_1-up_mushroom.png/120px-New_SMB_Wii_1-up_mushroom.png
			player = ImageIO.read(new File("img//player.png")); // http://mariopartylegacy.com/wp-content/gallery/Mario-Party%3A-Star-Rush---9/1/mpsrart1.png
//			monster1 = ImageIO.read(new File("img//wario1.png")); // http://vignette2.wikia.nocookie.net/fantendo/images/c/c3/Sochi_2014_olympic_wario_3d_render_by_ratchetmario-d8j2g4e.png/revision/latest?cb=20150517231540
//			waluigi = ImageIO.read(new File("img//waluigi.png")); // http://static.tvtropes.org/pmwiki/pub/images/waluigi-boardwin-mp9_1391.png
//			boo = ImageIO.read(new File("img//boo.png")); // https://vignette1.wikia.nocookie.net/luigi/images/7/7c/Boo.png/revision/latest?cb=20080806155504&path-prefix=de
//			donkey_kong = ImageIO.read(new File("img//donkey_kong.png")); // https://s-media-cache-ak0.pinimg.com/originals/c8/4f/92/c84f92638b3373ff3faf0ca6f4899b0c.png
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

		// jedes Element des Levels wird abgefragt und dann
		// gezeichnet
		// 0 = Wand
		// 1 = Boden
		// 2 = Player
		// 3 = offene Tuer
//		4 = Trank
//		5 = Key
//		6 = Monster
		
		for (int i = 0; i < window.WIDTH; i++) {
			for (int j = 0; j < window.HEIGHT; j++) {

				Levelmanagement level = new Levelmanagement(window);
				switch(level.getLvlSafeAll(i, j, level.LevelID)){
				case 0:
					g.drawImage(wall, i * window.BOX, j * window.BOX, null);
					break;

				case 1:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					break;

				case 2:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(player, i * window.BOX, j * window.BOX, null);
					break;

				case 3:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(doorOpen, i * window.BOX, j * window.BOX, null);
					break;

				case 4:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(healthpot, i * window.BOX, j * window.BOX, null);
					break;

				case 5:
					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
					g.drawImage(key, i * window.BOX, j * window.BOX, null);
					break;

//				case 6:
//					g.drawImage(floor, i * window.BOX, j * window.BOX, null);
//					g.drawImage(monster1, i * window.BOX, j * window.BOX, null);
//					break;

				default:
					System.err.println("Falsche Matrix");
				}

//				// hier wird der Spieler an die Position gezeichnet die in
//				// der Klasse Player uebergeben wird
//				g.drawImage(player, window.player.getXPos() * window.BOX, window.player.getYPos() * window.BOX, null);
//				repaint();

			}
		}
	}
}
