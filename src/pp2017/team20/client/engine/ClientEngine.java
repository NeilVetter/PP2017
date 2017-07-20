package pp2017.team20.client.engine;

import java.util.*;

import javax.crypto.SecretKey;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pp2017.team20.shared.*;
import pp2017.team20.client.gui.*;
import pp2017.team20.client.comm.*;
import pp2017.team20.server.engine.*;
import pp2017.team20.server.map.*;

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
	ClientKommunikation communication;
	// Spielfenster erstellen
	public GamingArea window;
	public Level level;
	public GamingArea getWindow(){
		return window;
	}
//	Queue <Message> MessageQueue = new LinkedList<Message>();

	/**
	 * 
	 * Konstruktor fuer die Klasse CLientEngine
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public ClientEngine(ClientKommunikation communication, GamingArea window) {
		this.communication = communication;
		this.window = window;
	}
	
//	ClientHandler comm;
//	Message msg = comm.getMessageFromServer();
//	MessageQueue.add(msg);
//	MessageProcess(MessageQueue);
//	
//	// Nimmt eine Message aus dem Queue und entscheidet welcher Unterklasse sie
//		// angeh�rt
//		public void MessageProcess(Queue<Message> queue) {
//
//			try {
//				// Pr�ft solange eine Message im Queue ist welchen Typ die
//				// Nachricht hat
//				while (!queue.isEmpty()) {
//
//					Message msg = queue.poll();
//					receiveRequest(msg);
//				}
//			} catch (Exception e) {
//
//			}
//		}
	
	
	/**
	 * 
	 * Methode um Nachricht zu empfangen
	 * 
	 * 
	 * @author Wagner, Tobias, 5416213
	 */
	public void receiveMessage() {
		Message msg = communication.erhalteNachricht();
		receiveRequest(msg);
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

	public void sendLogInMessage(int clientID, String username, String password) {
//		if (password.equals(keylog)) {
			LogInMessage message = new LogInMessage(clientID, username, password);
			communication.sendeNachricht(message);
//		}
	}

	/**
	 * 
	 * Erstellung einer Auslognachricht
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendLogOutMessage(int clientID) {
		LogOutMessage message = new LogOutMessage(clientID);
		communication.sendeNachricht(message);
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

	public void sendMoveMessage(int clientID, int xPos, int yPos, int id) {
		
		//instanceof wall ersetzt durch 0
		if (!(window.level.getlvlMazePostion(window.player.getXPos(), window.player.getYPos() ) == 0)) {
			MoveMessage message = new MoveMessage(clientID, xPos, yPos, id);
			communication.sendeNachricht(message);
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

	public void sendAttackMessage(int clientID, int attackID, int playerID, int monsterID) {
		AttackMessage message = new AttackMessage(clientID, attackID, playerID, monsterID);
		communication.sendeNachricht(message);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der ein Trank aufgenommen bzw.
	 * eingesammelt wurde
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendCollectPotionMessage(int clientID) {
		CollectPotionMessage message = new CollectPotionMessage(clientID);
		communication.sendeNachricht(message);
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
		// instanceof Key
		if (window.level.getlvlMazePostion(window.player.getXPos(), window.player.getYPos() ) == 5) {
			CollectKeyMessage message = new CollectKeyMessage(clientID);
			communication.sendeNachricht(message);
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
	
//	public void sendOpenDoorMessage(int clientID) {
//		//instanceof door
//		if (window.level[window.player.getXPos()][window.player.getXPos()] instanceof Door && 
//				//door
//				//((ddor) window...
//				(window.level[window.player.getXPos()][window.player.getYPos()]).key) {
//					OpenDoorMessage message = new OpenDoorMessage(clientID);
//					communication.sendMessageToServer(message);
//		}
//	}

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

	public void sendUsePotionMessage(int clientID, int id, int playerID) {
		if (id == -1) {
			if (window.player.getHealthPotNumber() > 0) {
				UsePotionMessage message = new UsePotionMessage(clientID, -1, playerID);
				communication.sendeNachricht(message);
			} else {
				UsePotionMessage message = new UsePotionMessage(clientID, id, playerID);
				communication.sendeNachricht(message);
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

	public void sendNextLevelMessage(int clientID) {
		NextLevelMessage message = new NextLevelMessage(clientID);
		communication.sendeNachricht(message);
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
		communication.sendeNachricht(message);
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
		communication.sendeNachricht(message);
	}

	/**
	 * 
	 * Erstellung einer Nachricht, in der ein neues Spiel erzeugt wird.
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */

	public void sendNewGameMessage(int clientID) {
		NewGameMessage message = new NewGameMessage(clientID);
		communication.sendeNachricht(message);
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
//		else if (msg instanceof OpenDoorMessage) {
//			OpenDoorMessage message = (OpenDoorMessage) msg;
//			receiveOpenDoorMessage(message);
//		}

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
//		if (message.getSuccess()) {
//			window.success = true;
			// Laedt das Level
//			System.out.println(message.getLevel().getlvlMaze(1, 1));
			window.level = message.getLevel();
//			System.out.println(window.level.getlvlMaze(1, 1));
			// Laedt die Startposition des Spielers
			window.xPos = message.getLevel().getxPos();
			window.yPos = message.getLevel().getyPos();
			// Laedt die Monster des Levels
			window.buffermonsterList = message.getLevel().monsterList;
			// Wen die Spielfeldkachel nicht vom Spieler belegt wird, also
			// ungleich -1 ist, dann wird ein Monster platziert
//			for (int i = 0; i < message.getLevel().getlvlMaze().length; i++) {
//				for (int j = 0; j < message.getLevel().getlvlMaze().length; j++) {
//					if (message.getLevel().getlvlMazePostion(i, j) != -1) {
//						window.buffermonsterList.get(message.getLevel().monsterfield[i][j]).setPos(i, j);
//					}
//				}
//			}
			// Anzeigen der Spielwelt
			window.showGamingWorld();
			window.setVisible(true);
			window.startNewGame();

			// Wenn man sich nicht zum ersten Mal einloggt, muss kein komplett
			// neues Spiel gestartet werden
//			if (window.firstLogIn = true) {
//				window.firstLogIn = false;
//			} else {
//				window.startNewGame();
//			}
//		}
		// Waren die eingegebenen Daten nicht korrekt, wird eine Fehlermeldung
		// angezeigt
//		else {
//			JOptionPane.showMessageDialog(window, "Wrong user or wrong password");
//			window.login();
//		}
		
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
			window.login();
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
			if (message.id == 1) {
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
//				if (message.hpDefender == 0) {
//					window.maze[message.xPos][message.yPos] = new Potion();
//				}
//				// Andernfalls werden die Lebenspunkte des Monsters angepasst
//				else {
//					window.monsterListe.get(message.defendID).setHealth(message.hpDefender);
//				}
			if (message.hpDefender != 0) {
				window.monsterList.get(message.defendID).setHealth(message.hpDefender);
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
					window.gameEnd = true;
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
			int xPos = window.player.getXPos();
			int yPos = window.player.getYPos();
			// Steht der Spieler auf einem Trank, so wird dieser aufgenommen
			//Potion hat Zahl 4
			//instanceof Potion
			if (window.level.getlvlMazePostion(window.player.getXPos(), window.player.getYPos() ) == 4) {
				window.player.collectPotion();
				// An die Stelle des Trankes wird eine leere Spielkachel
				// platziert
				window.level.setLvlMaze(xPos, yPos,4); //new Ground(); // @tobi so sollte es jetzt gehen. 
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
			int xPos = window.player.getXPos();
			int yPos = window.player.getYPos();
			// Steht der Spieler auf einem Feld mit einem Schluessel, so wird
			// dieser aufgenommen
			//instanceof Key
			if (window.level.getlvlMazePostion(window.player.getXPos(), window.player.getYPos() ) == 5) {
				//collectKey()
				window.player.ownsKey();
				// An der Stelle des Schluessels wird eine leere Spielkachel
				// platziert
				// new Ground()
				window.level.setLvlMaze(xPos, yPos, 5); //Same gru� Hamid
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

//	public void receiveOpenDoorMessage(OpenDoorMessage message) {
//		if (message.success) {
//			// Nach dem Oeffnen der Tuere wird ein neues Level geladen
//			window.player.openDoor();
//			window.NextLevel();
//		}
//	}

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
				window.level = message.getLevel();
				// Laedt die Startposition des Spielers
				window.xPos = message.getLevel().getxPos();
				window.yPos = message.getLevel().getyPos();
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
			window.level = message.getLevel();
			window.xPos = message.getLevel().getxPos();
			window.yPos = message.getLevel().getyPos();
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

		window.resetGame();
		window.showGamingWorld();
	}

}
