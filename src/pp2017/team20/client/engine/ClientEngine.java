package pp2017.team20.client.engine;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pp2017.team20.shared.*;

/**
 * 
 * In dieser Klasse werden die moeglichen Nachrichten, die vom Server empfangen
 * und an selbigen gesendet werden, bearbeitet und sortiert
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class ClientEngine {

	// Aufbau der Kommunikation zwischen CLient und Server
	ClientCommunication communication;
	// Spielfenster erstellen
	public Window window;

	/**
	 * 
	 * Konstruktor fuer die Klasse CLientEngine
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public ClientEngine(ClientCommunication communication, Window window) {
		this.communication = communication;
		this.window = window;
	}

	// Erstellung der Nachrichten, die an den Server geschickt werden

	/**
	 * 
	 * Erstellung einer Einlognachricht, die das eingegebene Passwort
	 * ueberprueft
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendLogInMessage(String user, String password1, String password2) {
		if (password1.equals(password2)) {
			LogInMessage message = new LogInMessage(user, password1, password2);
			communication.sendMessage(message);
		}
	}

	/**
	 * 
	 * Erstellung einer Auslognachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendLogOutMessage() {
		LogOutMessage message = new LogOutMessage();
		communication.senMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Bewegungsnachricht, bei der ueberprueft wird, ob die
	 * neue Position keine Wand ist, sodass eine Bewegung des Spielers moeglich
	 * ist
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendMoveMessage(int xPos, int yPos, int id) {
		if (!(window.level[xPos][yPos] instanceof wall)) {
			MoveMessage message = new MoveMessage(xPos, yPos, id);
			communication.sendMessage(message);
		}
	}

	/**
	 * 
	 * Erstellung einer Kampfnachricht, bei der die ID des Spielers, sowie die
	 * ID des Monsters an den Server uebermittelt wird
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendAttackMessage(int attackID, int defendID) {
		AttackMessage message = new AttackMessage(attackID, defendID);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der ein Trank aufgenommen bzw.
	 * eingesammelt wurde
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendCollectPotionMessage() {
		CollectPotionMessage message = new CollectPotionMessage();
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Schluesselnachricht, in der geprueft wird, ob sich der
	 * Spieler auf einem Feld mit einem Schluessel befindet. Steht er, wird der
	 * Schluessel aufgenommen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendCollectKeyMessage() {
		if (window.level[window.player.getXPos()][window.player.getYPos()] instanceof Key) {
			CollectKeyMessage message = new CollectKeyMessage();
			communication.sendMessage(message);
		}
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der geprueft wird, ob sich der Spieler auf einem Feld mit einer Tuer befindet. 
	 * Hat der Spieler zudem noch einen Schluessel, so wird die Tuere geoeffnet
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */
	
	public void sendOpenDoorMessage() {
		if (window.level[window.player.getXPos()][window.player.getYPos()] instanceof door && 
				((door) window.level[window.player.getXPos()][window.player.getYPos()]).key) ) {
					OpenDoorMessage message = new OpenDoorMessage();
					communication.sendMessage(message);
		}
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der zunaechst geprueft wird, ob der
	 * Spieler mindestens einen Trank zur Verfuegung hat. Falls ja, wird ein
	 * Trank genutzt. Die Variable id gibt hierbei den aktuellen Spieler an, -1
	 * fuer den ersten Spieler, id bezeichnet den anderen Spieler
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendUsePotionMessage(int id) {
		if (id == -1) {
			if (window.player.getNumberPotion() > 0) {
				UsePotionMessage message = new UsePotionMessage(id);
				communication.sendMessage(message);
			} else {
				UsePotionMessage message = new PotionMessage(id);
				communication.sendMessage(message);
			}
		}
	}

	/**
	 * 
	 * Erstellung einer Nachricht, dass der Spieler das naechste Level erreicht
	 * hat
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendNextLevelMessage() {
		NextLevelMessage message = new NextLevelMessage();
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer HighScore Nachricht. Dabei werden der aktuelle Spieler
	 * und die Zeit uebermittelt
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendHighscoreMessage(String user, int time) {
		HighScoreMessage message = new HighScoreMessage(user, time);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Chat Nachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendChatMessage(String content) {
		ChatMessage message = new ChatMessage(content);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der ein neues Spiel erzeugt wird.
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendNewGameMessage() {
		NewGameMessage message = new NewGameMessage();
		communication.sendMessage(message);
	}

	/**
	 * Methode zum Empfangen von Nachrichten, die vom Server geschickt werden.
	 * Dabei wird zunaechst geschaut, um welche Art der Nachricht es sich
	 * handelt, um diese dann im Anschluss auszufuehren
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveRequest(Message msg) {
		// Unterscheidung der eingehenden Nachrichten und casten in das Objekt
		// message

		// EinlogNachricht empfangen
		if (msg instanceof LogInMessage) {
			LogInMessage message = (LogInMessage) msg;
			receiveLogInMessage(message);
		}

		// LogOut-Nachricht empfangen
		else if (msg instanceof LogOutMessage) {
			LogOutMessage message = (LogOutMessage) msg;
			receiveLogOutMessage(message);
		}

		// Bewegungsnachricht empfangen
		else if (msg instanceof MoveMessage) {
			MoveMessage message = (MoveMessage) msg;
			receiveMoveMessage(message);
		}

		// Kampfnachricht empfangen
		else if (msg instanceof AttackMessage) {
			AttackMessage message = (AttackMessage) msg;
			receiveAttackMessage(message);
		}

		// Tranknachricht empfangen
		else if (msg instanceof CollectPotionMessage) {
			CollectPotionMessage message = (CollectPotionMessage) msg;
			receiveCollectPotionMessage(message);
		}

		// Schluesselnachricht empfangen
		else if (msg instanceof CollectKeyMessage) {
			CollectKeyMessage message = (CollectKeyMessage) msg;
			receiveCollectKeyMessage(message);
		}

		// Tuer Nachricht empfangen
		else if (msg instanceof OpenDoorMessage) {
			OpenDoorMessage message = (OpenDoorMessage) msg;
			receiveOpenDoorMessage(message);
		}

		// Benutze Trank empfangen
		else if (msg instanceof UsePotionMessage) {
			UsePotionMessage message = (UsePotionMessage) msg;
			receiveUsePotionMessage(message);
		}

		// Neues Level empfangen
		else if (msg instanceof NextLevelMessage) {
			NextLevelMessage message = (NextLevelMessage) msg;
			receiveNextLevelMessage(message);
		}

		// Highscore empfangen
		else if (msg instanceof HighScoreMessage) {
			HighScoreMessage message = (HighScoreMessage) msg;
			receiveHighScoreMessage(message);
		}

		// Neuer Chat empfangen
		else if (msg instanceof ChatMessage) {
			ChatMessage message = (ChatMessage) msg;
			receiveChatMessage(message);
		}

		// Neues Spiel Nachricht empfangen
		else if (msg instanceof NewGameMessage) {
			NewGameMessage message = (NewGameMessage) msg;
			receiveNewGameMessage(message);
		}

	}

	// Ab hier werden die Nachrichten behandlet, die vom Server empfangen werden

	/**
	 * 
	 * Hier wird eine Einlognachricht vom Server empfangen. Zunaechst wird
	 * ueberprueft, ob die eingegebenen Daten korrekt sind. Im Anschluss werden
	 * das Level, die Position des Spielers sowie die Position der Monster
	 * geladen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveLogInMessage(LogInMessage message) {
		// Wenn die Enlogdaten korrekt sind, dann wird das Level geladen
		if (message.isSuccess()) {
			window.success = true;
			// Laedt das Level
			window.level = message.getLevel().gameworld;
			// Laedt die Startposition des Spielers
			window.xPos = message.getLevel().getxXPos();
			window.yPos = message.getLevel().getYPos();
			// Laedt die Monster des Levels
			window.buffermonsterList = message.getLevel().monsterList;
			// Wen die Spielfeldkachel nicht vom Spieler belegt wird, also
			// ungleich -1 ist, dann wird ein Monster platziert
			for (int i = 0; i < message.getLevel().monsterfield.length; i++) {
				for (int j = 0; j < message.getLevel().monsterfield.length; j++) {
					if (message.getLevel().monsterfield[i][j] != -1) {
						window.buffermonsterList.get(message.getLevel().monsterfield[i][j]).setPos(i, j);
					}
				}
			}
			// Anzeigen der Spielwelt
			window.showGameworld();
			window.setVisible(true);

			// Wenn man sich nicht zum ersten Mal einloggt, muss kein komplett
			// neues Spiel gestartet werden
			if (window.firstLogIn = true) {
				window.firstLogin = false;
			} else {
				window.startNewGame();
			}
		}
		// Waren die eingegebenen Daten nicht korrekt, wird eine Fehlermeldung
		// angezeigt
		else {
			JOptionPane.showMessageDialog(window, "Wrong user or wrong password");
			window.login();
		}
	}

	/**
	 * 
	 * Empfangen einer Auslognachricht vom Server
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveLogOutMessage(LogOutMessage message) {
		if (message.success) {
			// Zeigt nach dem Ausloggen wieder das Anmeldefenster an
			window.LogIn();
		}
	}

	/**
	 * 
	 * Empfangen einer Bewegungsnachricht. Hierbei muss unterschieden werden, ob
	 * sich der Spieler oder ein Monster bewegt.
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveMoveMessage(MoveMessage message) {
		if (message.success) {
			// Hier fuehrt der Spieler eine Bewegung aus
			// xPos und yPos geben die neue Position des Spielers an
			if (message.id == -1) {
				window.player.setPos(message.xPos, message.yPos);
			}
			// Es findet eine Monsterbewegung statt. xPos und yPos bezeichnen
			// die neue Position des Monsters
			else {
				window.monsterList.get(message.id).setPos(message.xPos, message.yPos);
			}

		}
	}

	/**
	 * 
	 * Empfangen einer Angriffsnachricht. Hier muss unterschieden werden, ob der
	 * Spieler oder ein Monster angreift
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveAttackMessage(AttackMessage message) {
		if (message.success) {
			// Das ist der Fall, wenn der Spieler der ANgreifer und ein Monster
			// der Verteidiger ist
			if (message.attackID == -1 && message.defendID != -1) {
				// Ist das Monster besiegt, also hat kein Leben mehr, so
				// hinterlaesst es einen Trank
				if (message.hpDefender == 0) {
					window.level[message.xPos][message.yPos] = new Potion();
				}
				// Andernfalls werden die Lebenspunkte des Monsters angepasst
				else {
					window.monsterListe.get(message.defendID).setHealth(message.hpDefender);
				}
			}
			// Hier ist das Monster der Angreifer und der Spieler der
			// Verteidiger
			else if (message.attackID != -1 && message.defendID == -1) {
				// Lebenspunkte des Spielers werden angepasst
				window.player.setHealth(message.hpDefender);
				// Fallen die Lebenspunkte des Spielers auf Null, so endet das
				// Spiel
				if (message.hpDefender == 0) {
					window.gameEnding = true;
				}
			}
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, um einen Trank aufzunehmen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveCollectPotionMessage(CollectPotionMessage message) {
		if (message.success) {
			// Variablen, um die Position des Spielers zu bestimmen
			int xPos = window.player.getXpos();
			int yPos = window.player.getYPos();
			// Steht der Spieler auf einem Trank, so wird dieser aufgenommen
			if (window.level[xPos][yPos] instanceof Potion) {
				window.player.collectPotion((Potion) window.level[xPos][yPos]);
				// An die Stelle des Trankes wird eine leere Spielkachel
				// platziert
				window.level[xPos][yPos] = new Ground();
			}
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, um einen Schluessel aufzunehmen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveCollectKeyMessage(CollectKeyMessage message) {
		if (message.success) {
			// Variablen, um die Position des Spielers zu bestimmen
			int xPos = window.player.getXpos();
			int yPos = window.player.getYPos();
			// Steht der Spieler auf einem Feld mit einem Schluessel, so wird
			// dieser aufgenommen
			if (window.level[xPos][yPos] instanceof Key) {
				window.player.collectKey();
				// An der Stelle des Schluessels wird eine leere Spielkachel
				// platziert
				window.level[xPos][yPos] = new Ground();
			}
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, um eine Tuere zu oeffnen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveOpenDoorMessage(OpenDoorMessage message) {
		if (message.success) {
			// Nach dem Oeffnen der Tuere wird ein neues Level geladen
			window.player.openDoor();
			window.NextLevel();
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, dass ein Trank genommen wurde
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveUsePotionMessage(UsePotionMessage message) {
		if (message.success) {
			// Spieler nimmt den Trank
			if (message.id == -1) {
				window.player.usePotion();
			}
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, dass ein neues Level geladen werden soll
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveNextLevelMessage(NextLevelMessage message) {
		if (message.success) {
			// So lange noch nicht das Ende des Spiels erreicht wurde, wird ein
			// neues Level geladen
			if (!message.gameEnding) {
				// Neues Level wird angefordert
				window.currentLevel++;
				// Neues Level wird geladen
				window.level = message.getLevel().gameworld;
				// Laedt die Startposition des Spielers
				window.XPos = message.getLevel().getXPos();
				window.YPos = message.getLevel().getYPos();
				// Laedt die Monster des Levels
				window.buffermonsterList = message.getLevel().monsterList;
				// Wen die Spielfeldkachel nicht vom Spieler belegt wird, also
				// ungleich -1 ist, dann wird ein Monster platziert
				for (int i = 0; i < message.getLevel().monsterfield.length; i++) {
					for (int j = 0; j < message.getLevel().monsterfield.length; j++) {
						if (message.getLevel().monsterfield[i][j] != -1) {
							window.buffermonsterList.get(message.getLevel().monsterfield[i][j]).setPos(i, j);
						}
					}
				}
				// Der Schluessel wird von dem Spieler entfernt
				window.player.removeKey();
			}
			// Wurde das letzte moegliche Level bereits geladen, so endet das
			// Spiel
			else {
				window.gameEnding = true;
			}
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, dass ein HighScore erzielt wurde
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveHighScoreMessage(HighScoreMessage message) {
		// Eintragen des neuen HighScores
		window.getHighScore().repaint();
	}

	/**
	 * 
	 * Empfangen einer Nachricht, dass etwas in den Chat geschrieben wurde. Es
	 * wird zudem geprueft, ob es sich dabei um einen Cheat handelt
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveChatMessage(ChatMessage message) {
		String messageContent = message.messageContent;
		// Es wird ueberprueft, ob ein Cheat eingegeben wurde
		switch (messageContent) {
		// Gibg dem Spieler quasi ungbegrenztes Leben
		case "UnlimitLife":
			window.player.ChatOutput.append("Unbesiegbar freigeschaltet");
			window.player.setLife(100000);
			break;
		// Laesst den Spieler Monster mit einem Schlag/Klick toeten
		case "IncreaseDamage":
			window.player.ChatOutput.append("Erhoeter Schaden");
			window.player.setDamage(100);
			break;
		// Setzt die Cheats wieder zurueck
		case "Normal":
			window.player.ChatOutput.append("Cheats sind deaktiviert");
			window.player.setLife(200);
			window.player.setDamage(10);
			break;
		// Wird kein Cheat eingegeben, wird die Eingabe ausgegeben
		default:
			window.chat.ChatOutput(message);
			break;
		}
	}

	/**
	 * 
	 * Empfangen einer Nachricht, dass ein neues Spiel erstellt werden soll.
	 * Dazu werden das Spielfeld sowie die Monster vom Server empfangen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveNewGameMessage(NewGameMessage message) {
		if (message.success) {
			window.level = message.getLevel().gameworld;
			window.xPos = message.getLevel().getXPos();
			window.yPos = message.getLevel().getYPos();
			window.buffermonsterList = message.getLevel().monsterList;
			// Wen die Spielfeldkachel nicht vom Spieler belegt wird, also
			// ungleich -1 ist, dann wird ein Monster platziert
			for (int i = 0; i < message.getLevel().monsterfield.length; i++) {
				for (int j = 0; j < message.getLevel().monsterfield.length; j++) {
					if (message.getLevel().monsterfield[i][j] != -1) {
						window.buffermonsterList.get(message.getLevel().monsterfield[i][j]).setPos(i, j);
					}
				}
			}
		}

		window.gameReset();
		window.showGameWorld();
	}

}
