package pp2017.team20.client.engine;

import pp2017.team20.shared.*;
import pp2017.team20.client.gui.*;
import pp2017.team20.client.comm.*;


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
	// Spielfenster erstellen und Level laden
	public GamingArea window;
	public Level level;

	// Spielfenster anzeigen
	public GamingArea getWindow() {
		return window;
	}

	/**
	 * 
	 * Konstruktor fuer die Klasse CLientEngine
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public ClientEngine(ClientCommunication communication, GamingArea window) {
		this.communication = communication;
		this.window = window;
	}

	/**
	 * 
	 * Methode um Nachricht zu empfangen
	 * 
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	public void receiveMessage() {
		Message msg = communication.receiveMessage();
		receiveRequest(msg);
	}

	// Erstellung der Nachrichten, die an den Server geschickt werden

	/**
	 * 
	 * Erstellung einer Einlognachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendLogInMessage(int clientID, String username, String password) {
		LogInMessage message = new LogInMessage(clientID, username, password);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Auslognachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendLogOutMessage(int clientID, int playerID) {
		LogOutMessage message = new LogOutMessage(clientID, playerID);
		communication.sendMessage(message);
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

	public void sendMoveMessage(int clientID, int xPos, int yPos, int playerID) {

		// Ueberpruefen, ob man sich auf eine gueltige Position bewegt, also
		// keine Wand.
		if (!(window.level.getLvl().getLvlMazePosition(window.player.getXPos(), window.player.getYPos()) == 0)) {
			MoveMessage message = new MoveMessage(clientID, xPos, yPos, playerID);
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

	public void sendAttackMessage(int clientID, int attackID, int defendID, int hpDefender) {
		AttackMessage msg = new AttackMessage(clientID, 1, attackID, defendID, hpDefender);
		communication.sendMessage(msg);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der ein Trank aufgenommen bzw.
	 * eingesammelt wurde
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendCollectPotionMessage(int clientID, int x, int y) {
		CollectPotionMessage message = new CollectPotionMessage(clientID, x, y, window.player.getPlayerID());
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

	public void sendCollectKeyMessage(int clientID) {
		if (window.level.getLvlMazePosition(window.player.getXPos(), window.player.getYPos()) == 5) {
			CollectKeyMessage message = new CollectKeyMessage(clientID, window.player.getPlayerID());
			communication.sendMessage(message);
		}
	}

	/**
	 * 
	 * Erstellung einer Nachricht,um einen Trank benutzen bzw aufnehmen zu
	 * koennen
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendUsePotionMessage(int clientID, int id, int playerID) {
		UsePotionMessage message = new UsePotionMessage(clientID, -1, playerID);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, dass der Spieler das naechste Level erreicht
	 * hat
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendNextLevelMessage(int clientID) {
		NextLevelMessage message = new NextLevelMessage(clientID);
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

	public void sendHighscoreMessage(int clientID, String user, int time) {
		HighScoreMessage message = new HighScoreMessage(clientID, user, time);
		communication.sendMessage(message);
	}

	/**
	 * 
	 * Erstellung einer Chat Nachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendChatMessage(int clientID, String content) {
		ChatMessage message = new ChatMessage(clientID, content);
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

		// Monster fuer das Level empfangen
		else if (msg instanceof UpdateMonsterMessage) {
			UpdateMonsterMessage message = (UpdateMonsterMessage) msg;
			if (((UpdateMonsterMessage) msg).get().type == 0)
				;
			recieveMonsterUpdateMessage(message);
		}

		// Nachricht empfangen, dass ein Monster besiegt wurde
		else if (msg instanceof DeathMessage) {
			DeathMessage message = (DeathMessage) msg;
			recieveDeatheMessage(message);
		}

	}

	// Ab hier werden die Nachrichten behandelt, die vom Server empfangen werden

	/**
	 * 
	 * Erstellung einer Nachricht, in der mittgeteilt wird, dass ein Monster
	 * besiegt wurde. Ist es besiegt, wird das Monster aus der Liste und der Map
	 * entfernt
	 * 
	 * @author Wagner, Tobias, 5416213
	 * @author Sell, Robin, 6071120
	 */

	private void recieveDeatheMessage(DeathMessage message) {
		if (message.type == 0) {
			sendObject tmp = null;
			for (sendObject m : window.monster) {
				if (m.ID == message.id) {
					tmp = m;
				}
			}
			if (tmp != null) {
				window.monster.remove(tmp);
			}
		}
	}

	/**
	 * 
	 * In dieser Nachricht werden die Monster fuer das Level geladen und auf die
	 * Positionen im Level platziert
	 * 
	 * @author Wagner, Tobias, 5416213
	 * @author Sell, Robin, 6071120
	 */

	private void recieveMonsterUpdateMessage(UpdateMonsterMessage message) {

		boolean changed = false;
		for (sendObject m : window.monster) {
			if (m.ID == message.get().ID) {
				m.posX = message.get().posX;
				m.posY = message.get().posY;
				changed = true;
			}
		}
		if (!changed) {
			window.monster.add(message.get());
		}

		for (sendObject m : window.monster) {
		}

	}

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
		if (message.getSuccess()) {
			window.setSuccess(true);
			// Laedt das Level
			window.level = message.getLevel();
			// Laedt die Startposition des Spielers

			for (int i = 0; i < message.getLevel().getlvlMaze().length; i++) {
				for (int j = 0; j < message.getLevel().getlvlMaze().length; j++) {
					if (message.getLevel().getLvlMazePosition(i, j) == 2) {
						// Erzeugen bzw laden des Spielers
						Player player = new Player();
						// Position des Spielers im Level zu beginn
						player.setXPos(i);
						player.setYPos(j);
						// Player uebergeben an GUI
						window.player = player;
						window.player.setPlayerID(message.playerID);
					}
				}
			}

			// Anzeigen der Spielwelt
			window.showGamingWorld();
			window.setVisible(true);
			Registration.window.dispose();

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
			window.login(null, null);
		}
	}

	/**
	 * 
	 * Empfangen einer Bewegungsnachricht.
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void receiveMoveMessage(MoveMessage message) {
		if (message.success) {
			// Umsetzen der Bewegung und uebermitteln der neuen Position
			window.player.setPos(message.xPos, message.yPos);
		}
	}

	/**
	 * 
	 * Empfangen einer Angriffsnachricht. Hier muss unterschieden werden, ob der
	 * Spieler oder ein Monster angreift
	 * 
	 * @author Wagner, Tobias, 5416213
	 * @author Sell, Robin, 6071120
	 * 
	 */

	public void receiveAttackMessage(AttackMessage message) {
		if (message.attackType == 0) { // Monster greift Spieler an
			if (window.player.getPlayerID() == message.defendID) {
				window.player.setHealth(message.hpDefender);
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
			int xPos = window.player.getXPos();
			int yPos = window.player.getYPos();
			// Steht der Spieler auf einem Trank, so wird dieser aufgenommen
			if (window.level.getLvlMazePosition(window.player.getXPos(), window.player.getYPos()) == 4) {
				window.player.collectPotion();
				// An die Stelle des Trankes wird eine leere Spielkachel
				// platziert
				window.level.setLvlMaze(xPos, yPos, 4);
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
		if (window.player.getPlayerID() == message.playerId) {
			window.player.ownsKey = true;
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
		if (window.player.getPlayerID() == message.playerID) {
			// Ueberpruefen,b der Spieler genuegend Traenke hat
			window.player.healthPotNumber += message.type;
			if (message.type < 0) {
				// Anpassen der Lebensenergie des Spielers
				window.player.changeHealth(30);
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
				window.level = message.getLevel();
				// Laedt die Startposition des Spielers
				window.player.setXPos(message.getLevel().getxPos());
				window.player.setYPos(message.getLevel().getyPos());
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
				window.gameEnd = true;
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
		window.getHighscore().repaint();
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
			window.chat.chatOutput.append("Unbesiegbar freigeschaltet");
			window.player.setHealth(100000);
			break;
		// Laesst den Spieler Monster mit einem Schlag/Klick toeten
		case "IncreaseDamage":
			window.chat.chatOutput.append("Erhoeter Schaden");
			window.player.setDamage(100);
			break;
		// Setzt die Cheats wieder zurueck
		case "Normal":
			window.chat.chatOutput.append("Cheats sind deaktiviert");
			window.player.setHealth(200);
			window.player.setDamage(10);
			break;
		// Wird kein Cheat eingegeben, wird die Eingabe ausgegeben
		default:
			window.chat.chatOutput.append(messageContent);
			break;
		}
	}

}
