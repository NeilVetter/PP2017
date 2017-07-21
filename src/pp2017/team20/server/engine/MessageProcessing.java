package pp2017.team20.server.engine;

import java.util.*;

import pp2017.team20.shared.*;
import pp2017.team20.server.comm.*;

public class MessageProcessing {

	// Database database = new database();
	// movement
	private boolean neighbour;
	private int Health;
	private int Mana;
	private boolean moveAllowed;

	Queue<Message> MessageQueue = new LinkedList<Message>();
	
	
	public ServerKommunikation comm= new ServerKommunikation();
	public Levelmanagement lvl;
	static boolean lvlLoaded = false;
	
//	 Message message=comm.getMessageFromClient();
//	 MessageQueue.add(message);
//	 MessageProcess(MessageQueue);

	// LKonstruktor fuer MassageProcessing

	public MessageProcessing() {

	}
	public Thread t = new Thread(new Runnable() {
		@Override
		public void run(){
			System.out.println("Monsterspawn0");
			while(!lvlLoaded){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Monsterspawn1");
			while(true){
				try{
					Thread.sleep(50);
					for(Monster monster: lvl.MonsterList){
						monster.tacticMonster();
						//tacticmon
					}
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}	
	});
	
	public void RecieveMessage() {
		
		t.start();
			// Empfange bis der Server beendet wird Nachrichten von der
			// Kommunikation
			while (true) {
				try {
					Thread.sleep(50);
					Message message = comm.erhalteNachricht();
					WhatMessageType(message);
				} catch (Exception e) {
					
				}
			}
	}
	// Nimmt eine Message aus dem Queue und entscheidet welcher Unterklasse sie
	// angehoert
	public void MessageProcess(Queue<Message> queue) {

		try {
			// Prueft solange eine Message im Queue ist welchen Typ die
			// Nachricht hat
			while (!queue.isEmpty()) {

				Message message = queue.poll();
				WhatMessageType(message);
			}
		} catch (Exception e) {

		}
	}

	// Wird in MessageProcess aufgerufen und entscheidet welchen Typs die
	// Message ist

	public void WhatMessageType(Message message) {

		// testet welchen typs die nachricht ist
		if (message instanceof LogInMessage) {
			// Castet die Nachricht um sie als LoginMessage benutzen zu k�nnen
			LogInMessage login = (LogInMessage) message;
			// ruft die zugeh�rige Processing-Methode auf
			LogInMessageProcessing(login);
		}else if (message instanceof LogOutMessage) {
			LogOutMessage logout = (LogOutMessage) message;
			LogOutMessageProcessing(logout);
		}else if (message instanceof MoveMessage) {
			MoveMessage movement = (MoveMessage) message;
			MoveMessageProcessing(movement);
		}else if (message instanceof UsePotionMessage) {
			UsePotionMessage pot = (UsePotionMessage) message;
			UsePotionMessageProcessing(pot);
		}else if (message instanceof AttackMessage) {
			AttackMessage playerattack = (AttackMessage) message;
			AttackMessageProcessing(playerattack);
		}else if (message instanceof DeathMessage) {
			DeathMessage dead = (DeathMessage) message;
			DeathMessageProcessing(dead);
		}else if (message instanceof ItemPickUpMessage) {
			ItemPickUpMessage itempick = (ItemPickUpMessage) message;
			ItemPickUpMessageProcessing(itempick);
		}else if (message instanceof NextLevelMessage) {
			NextLevelMessage next = (NextLevelMessage) message;
			NextLevelMessageProcessing(next);
		}else if (message instanceof SendLevelMessage) {
			SendLevelMessage lvl = (SendLevelMessage) message;
			SendLevelMessageProcessing(lvl);
		}
	}

	// Wird gesendet nachdem Levelgenerator in der Datenbank gepr�ft hat,
	// ob der Nutzer schon existiert
	// oder andernfalls neu eingetragen wurde

	public void LogInMessageProcessing(LogInMessage message) {

		try {
			if((Loginengine.logIn(message.username,message.password)==true)){
				
				lvl = new Levelmanagement(new Player());
				lvlLoaded = true;
				System.out.println("lvl erstellt");
				lvl.PlayerList.add(lvl.player);
				Level level = new Level(lvl,lvl.getLvlMaze());
				
				
				LogInMessage login = new LogInMessage(1,message.username,message.password);
				login.setPlayer(lvl.getPlayer());
				login.setSuccess(true);
				login.setLevel(level);
				login.setPlayerID(lvl.player.getPlayerID());
				System.out.println();
				comm.sendeNachricht(login);
				
			}else{
				message.setSuccess(false);
				comm.sendeNachricht(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void LogOutMessageProcessing(LogOutMessage message) {
		
		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);
			if (message.playerID == player.playerID) {
				// Aendert den Zustand des Spielers zu LoggedOFF
				player.loggedIN = false;
				// Entfernt den Spieler aus der Liste der aktiven Spieler
				lvl.PlayerList.remove(player);

				System.out.println(player.playername + " " + "ist abgemeldet");
				// sendMessageToClient(message);
			}
		}
	}

	public void MoveMessageProcessing(MoveMessage message) {

		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);
			if (message.playerID == player.playerID) {
				
				// Test, ob das Feld benachbart zur vorherigen Position des
				// Spielers

				if (Math.abs(message.xPos - player.getXPos())
						+ Math.abs(message.yPos - player.getYPos()) <= 1) {

					neighbour = true;

					// falls das feld benachbart ist...
					if (neighbour == true) {

						// ...Teste ob angesprochenes Feld begehbar ist
						if (lvl.getLvlMazePosition(message.xPos,message.yPos) != 0);

						// Setze die Position des Spielers auf die aus der
						// Message
						player.setXPos(message.xPos);
						player.setYPos(message.yPos);
						// Gebe zurueck dass der Schritt erfolgreich
						
						System.out.println(player.playername + " "+ "Hat sich ein Feld bewegt");
						MoveMessage move=new MoveMessage(1,player.getXPos(),player.getYPos(),player.getPlayerID());
						
						move.setSuccess(true);
						comm.sendeNachricht(move);
						
					} else {
						// Gebe zurueck das der Schritt gesscheitert
						message.success = false;
						System.out.println(player.playername + " "
								+ "Hat sich nicht bewegt");

					}
				}
				// Position des Spielers nach dem Schritt(unveraendert falls
				// fehlgeschlagen
				System.out.println(player.getXPos());
				System.out.println(player.getYPos());
			}
		}
	}

	// Nehmen eines Lebens-trankts
	public void UsePotionMessageProcessing(UsePotionMessage message) {

		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);
			if (message.playerID == player.playerID) {

				switch (message.id) {

				case 0:

					// falls der Spieler beim aktiviern des Tranks volles Leben
					// hat
					// passiert nichts, kein Trank wird verbraucht
					if (player.getHealth() == 100) {
						System.out.println("Leben ist schon voll");

						// Testet ob der Spieler einen Trank besitzt
					} else if (player.healthPotNumber > 0) {

						// Erhoeht das Leben des Spielers
						player.setHealth(player.getHealth() + 30);
						// Testet ob leben das maxleben uebersteigt undd falls
						// dem so ist
						// setzte Leben gleich Maxeben
						if (player.getHealth() >= 100) {
							player.setHealth(100);
						}
						// Nach erfolgreicher benutzung des Tranks reduziere die
						// anzahl der traenke um 1
						// Soll noch geaendert werden um mit der Itemliste zu
						// funktionieren
						player.healthPotNumber--;
					}
					System.out.println(player.playername + " "
							+ "wurde geheilt");
					System.out.println(Health);

					System.out.println(player.healthPotNumber);

					break;

				case 1:

					if (player.getMana() == 100) {
						System.out.println("Mana ist schon voll");
						// Testet ob der Spieler einen Trank besitzt
					} else if (player.manaPotNumber > 0) {

						// Erhoeht das Mana des Spielers
						player.setMana(player.getMana() + 30);
						// Testet ob Mana das Maxmana uebersteigt undd falls
						// dem so ist
						// setzte Mana gleich Maxmana
						if (player.getMana() >= 100) {
							player.setMana(100);
						}
						// Nach erfolgreicher benutzung des Tranks reduziere die
						// anzahl der traenke um 1
						// Soll noch geaendert werden um mit der Itemliste zu
						// funktionieren
						player.manaPotNumber--;
					}
				}
			}
		}
	}

	// Methode zum angriff von einem Spielr auf ein Monster
	// Funktioniert noch nicht, da Monster Klasse nicht eingebungden
	public void AttackMessageProcessing(AttackMessage message) {

		if (message.attackID == 1) {

			for (int i = 0; i < lvl.MonsterList.size(); i++) {
				Monster monster = lvl.MonsterList.get(i);
				if (message.monsterID == monster.monsterID) {

					// Monster prueft ob der Anngriff erfolgen darf und schickt
					// dann die Nachricht
					if (moveAllowed == true) {

						// fuege dem Monster schaden zu
						monster.setDamage(10);

						// Falls das Monster auf <=0 Hp faellt entferne es aus
						// dem Spiel
						if (monster.getHealth() <= 0) {
							lvl.MonsterList.remove(monster);
							System.out.println("Monster gestorben");
						}
					}
				}
			}

		} else if (message.attackID == 0) {

			for (int i = 0; i < lvl.PlayerList.size(); i++) {
				Player player = lvl.PlayerList.get(i);
				if (message.playerID == player.playerID) {

					if (moveAllowed == true) {

						// fuege dem Player Schaden zu
						player.setDamage(10);

						// Falls der Player <=0 Hp faellt entferne Ihn aus der
						// Liste
						if (player.getHealth() <= 0) {
							Player.LevelList.remove(player);
							// Benutze Methode aus Levelmanagement um den
							// Spieler an das anfangsfeld zu stellen
							Levelmanagement l = new Levelmanagement(player);
							l.placePlayer(player.playerID);
							// regeneriere die stats des spielers
							player.setHealth(100);
							player.setMana(100);
						}
					}
				}
			}
		}
	}

	// Methode falls der Spieler noch durch andere Einwirkung sterben kann
	public void DeathMessageProcessing(DeathMessage message) {

		if (message.id == 0) {

			for (int i = 0; i < lvl.PlayerList.size(); i++) {
				Player player = lvl.PlayerList.get(i);

				if (message.playerID == player.playerID) {
					// entferne Spieler aus der Liste
					lvl.PlayerList.remove(player);
					// Benutze Methode aus Levelmanagement um den Spieler an das
					// Anfangsfeld zu stellen
					Levelmanagement l = new Levelmanagement(player);
					l.placePlayer(player.playerID);
					// regeneriere die stats des spielers
					player.setHealth(100);
					player.setMana(100);
				}
			}
		} else if (message.id == 1) {
			for (int i = 0; i < lvl.MonsterList.size(); i++) {
				Monster monster = lvl.MonsterList.get(i);
				if (message.monsterID == monster.monsterID) {
					lvl.MonsterList.remove(monster);
				}
			}
		}
	}

	// Teste ob in der Konsole die Cheats aktiviert hat
	public void ChatMessageProcessing(ChatMessage message) {

		try {

			// Aktiviere Cheats
			if (message.messageContent == "CheatON") {
				System.out.println("Cheats aktiviert");
				// Deaktivier Cheats
			} else if (message.messageContent == "CheatOFF") {
				System.out.println("Cheats deaktiviert");
				// Speichere den Text der Nachricht f�r das messagesystem
				// z.B dem anderen Spielern anzeigen
			} else {
				
			}
		} catch (Exception e) {
			System.out.println("Unbkannter Befehl");
		}
	}

	// Unterscheidet unter den verfuegbaren Items und fuegt sie dem inventar
	// hinzu
	public void ItemPickUpMessageProcessing(ItemPickUpMessage message) {

		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);

			if (message.playerID == player.playerID) {

				switch (message.ItemID) {

				// Healthpot aufnehmen
				case 0:
					// erzeugt einen HealthPot und fuegt ihm dem Inventar hinzu
					player.healthPotNumber++;
					// player.ItemList.add(h);
					System.out.println("HealthPot aufgenommen");
					break;

				// Manapot aufnehmen
				case 1:
					// erzeugt einen ManaPot und fuegt ihm dem Inventar hinzu
					// ManaPot m = new ManaPot();
					player.manaPotNumber++;
					// player.ItemList.add(m);
					System.out.println("ManaPot aufgenommen");
					break;
				// Schluessel aufnehmen
				case 2:
					// Aendert den Zustand des Spielers so dass er den
					// schluessel besitzt
					// So kann er tueren oeffnen
					player.ownsKey = true;
				}
			}
		}
	}

	public void NextLevelMessageProcessing(NextLevelMessage message) {
		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);

			if (message.playerID == player.playerID) {

				
				// Setzt den spieler in naechstes level
				lvl.newLevel(player.getPlayerID()+1,19);
				// Setze den Spieler an den Eingang
				lvl.placePlayer(player.playerID);
			}
		}
	}

	public void SendLevelMessageProcessing(SendLevelMessage message) {

		System.out.println("Eine Kopie des Levels wurde dem Client geschickt");

	}
	public Levelmanagement getLvl(){
		return lvl;
	}
	
}
