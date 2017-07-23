package pp2017.team20.server.engine;

import java.util.*;

import pp2017.team20.shared.*;
import pp2017.team20.server.comm.*;



/**
 * Klasse zur Verwaltung eingehender und zusendender Nachrichten
 * Sowie das  regelmässige Updaten des Clients von Level, Monster- und Spelerlisten
 * 
 * @author Neil, Vetter, 6021336
 * 
 */



public class MessageProcessing {

	private boolean neighbour;
	
	//Aufruf der kommunikation um den Server zu starten
	public ServerCommunication comm= new ServerCommunication();
	//Spielwelt
	public Levelmanagement lvl;
	
	//Eine Variable die prueft, ob schon eine LoginMessage gesendet wurde  
	static boolean lvlLoaded = false;
	
	
	// Leerer Konstruktor fuer MassageProcessing
	public MessageProcessing() {

	}
	
	/**
	 * Thread der die Verwaltung der Monster während des Spielens übernimmt
	 * Also das Updaten des Clients ueber Aenderungen an der Monsterliste
	 * Sowie den Aufruf des endlichen Automatens in der Monster Klasse
	 * 
	 * 
	 * @author Neil, Vetter, 6021336
	 * 
	 */
	
	public Thread t = new Thread(new Runnable() {
		@Override
		public void run(){
	
			//Sleep bis eine Anmeldung erfolgt ist und das Level geladen ist
			while(!lvlLoaded){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//Hilfsliste und Hilsobjekt um die Daten der Monster an den Client zu senden
			ArrayList<sendObject> monsterOld= new ArrayList<sendObject>();
			ArrayList<AttackMessage> attacks = new ArrayList<AttackMessage>();
			while(true){
				//Abfrage ob sich an dem Zustad min. eines Monsters etwas geaendert hat
				boolean change = false;
				
				try{
					Thread.sleep(50);
					// Lokale Listen für Monster und MonsterAttacks
					//Zum arbeiten im Thread
					AttackMessage tmp = null;
					Monster remMon = null;
					//Pruefe, ob in der Monsterliste der Spielwelt ein Element enthalten ist
					if (lvl.MonsterList != null && !(lvl.MonsterList.isEmpty())) {
						//Nehme jedes Monster nacheinander aus der Liste
						for (Monster monster : lvl.MonsterList) {
							
							boolean sendAttack = false;
							boolean noMatch = true;
							//Falls eines der Monster eine attacke gespeicher hat
							if (monster.am != null) {
								// gehe durch die LokaleListe der Attacken
								for (AttackMessage attack : attacks) {
									//neheme nur die Attacken die auf von dem gerade geprueften Monster kommen
									if (attack.attackID == monster.monsterID) {
										//Pruefe ob die Attacke schon vorher durchgeführt wurde
										//und wenn nicht setze sendAttack auf true und noMatch auf false 
										//um später die AttackMessage zu senden
										if (attack != monster.am) {
											sendAttack = true;
											tmp = attack;
										}
										noMatch = false;
									}
								}
								//Falls das Monster eine Attacke durchgeführt hat schicke eine Attackmessage an den Client
								if (sendAttack) {
									sendAttackMessage(monster.am);
									attacks.remove(tmp);
									attacks.add(monster.am);
								}
								if (noMatch) {
									sendAttackMessage(monster.am);
									attacks.add(monster.am);
								}

							}
							//Führe einen Schritt im endlichen Generator der Monsterklasse fuer das Monster durch 
							monster.tacticMonster();
							//Prueft, ob die Daten des Monster schon in der Hilfsliste enthalten ist
							boolean inList = false;
							
							for (sendObject m : monsterOld) {
								//nehme die Daten für das gerade betrachtete Monster aus der Liste
								if (m.ID == monster.getId()) {
									//Pruefe ob sich die Position des Monsters in tacticMonster() geaendert hat
									if ((m.posX != monster.getXPos()) || (m.posY != monster.getYPos())) {
										//Aendere die Positon im Hilfsobjekt
										m.posX = monster.getXPos();
										m.posY = monster.getYPos();
										change = true;
									}
									//Merken, dass das Monster schon in der Hilfslist vorhanden ist...
									inList = true;
								}
							}
							//...War es nicht vorhanden schreibe es in diese
							if (!inList) {
								monsterOld.add(new sendObject(0, monster.getXPos(), monster.getYPos(),
										monster.getStrength(), monster.getId()));
								change = true;
							}
							if (change) {
								//Sende, falls es eine Aenderung gab diese an den Client
								UpdateMonsterMessage msg = new UpdateMonsterMessage(1);
								msg.set(monster);
								comm.sendMessage(msg);
								Thread.sleep(100);
							}
							//falls das Monster keine Leben mehr übrig hat setzt es als zu entfernen
							if (monster.getHealth() <= 0) {
								remMon = monster;
							}

						} 
					}
					//entferne Monster falls eines entfernt werden muss
					if (remMon != null){
						sendDeathMessage(0, remMon.getId());
						lvl.MonsterList.remove(remMon);
					}
					
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}	
	});
	
	public void RecieveMessage() {
		
		//Starte den Thread für die Verwaltung der Monster
		t.start();
			// Empfange bis der Server beendet wird Nachrichten von der
			// Kommunikation
			while (true) {
				try {
					Thread.sleep(50);
					Message message = comm.receiveMessage();
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
			// Castet die Nachricht um sie als LoginMessage benutzen zu kï¿½nnen
			LogInMessage login = (LogInMessage) message;
			// ruft die zugehï¿½rige Processing-Methode auf
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
		}else if (message instanceof NextLevelMessage) {
			NextLevelMessage next = (NextLevelMessage) message;
			NextLevelMessageProcessing(next);
		} else if(message instanceof CollectPotionMessage){
			CollectPotionMessage pot = (CollectPotionMessage) message;
			CollectPotionMessageProcessing(pot);
		} else if(message instanceof CollectKeyMessage){
			CollectKeyMessage key = (CollectKeyMessage) message;
			CollectKeyMessageProccessing(key);
		}
	}

	// Methode, die die erhaltenen Daten an die Datenbank zur Pruefung gibt
	// und dann, falls von der Datenbank tru zurueckgegeben wird, die Spielwelt erstellt
	public void LogInMessageProcessing(LogInMessage message) {

		try {
			//Datenbank pruefung
			if((Loginengine.logIn(message.username,message.password)==true)){
				//Spielwelt wird erzeugt
				lvl = new Levelmanagement(new Player());
				//gebe fuer den Thread an dass die Spielwelt erstellt wurde und er weiter machen kann
 				lvlLoaded = true;
				//Füge den Spieler zur Playerlist hinzu
				lvl.PlayerList.add(lvl.player);
				Level level = new Level(lvl,lvl.getLvlMaze());
				
				// Spielwelt wird an Client übertragen mit successs=true
				LogInMessage login = new LogInMessage(1,message.username,message.password);
				login.setPlayer(lvl.getPlayer());
				login.setSuccess(true);
				login.setLevel(level);
				login.setPlayerID(lvl.player.getPlayerID());
				comm.sendMessage(login);
				
			}else{
				//Nachricht wird zurück gesendet mit success=false
				message.setSuccess(false);
				comm.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Methode für den Logout eines Spielers
	public void LogOutMessageProcessing(LogOutMessage message) {
		
		//Nimmt den richtigen Spieler aus dder Liste...
		for (int i = 0; i < lvl.PlayerList.size(); i++) {
			Player player = lvl.PlayerList.get(i);
			if (message.playerID == player.playerID) {
				// ... entfernt diesen Spieler aus der Liste der aktiven Spieler
				lvl.PlayerList.remove(player);

				//Sende dem Client die Info dass der Logout erfolgreich war
				LogOutMessage log = new LogOutMessage(1,player.playerID);
				log.setSuccess(true);
				comm.sendMessage(log);
			}
		}
	}

	public void MoveMessageProcessing(MoveMessage message) {
		//nehme den richtigen Spieler aus der Liste
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
						
						
						//Sende die neue Postion zurück an den Client
						MoveMessage move=new MoveMessage(1,player.getXPos(),player.getYPos(),player.getPlayerID());	
						move.setSuccess(true);
						comm.sendMessage(move);
						
					} else {
						MoveMessage move=new MoveMessage(1,player.getXPos(),player.getYPos(),player.getPlayerID());	
						move.setSuccess(false);
						comm.sendMessage(move);
					}
				}
			}
		}
	}

	// Nehmen eines Lebens-tranks
	public void UsePotionMessageProcessing(UsePotionMessage message) {
		//Nehme den richtigen Spieler aus der Playerlist
		for(Player p : lvl.PlayerList){
			//Pruefe ob er einen Trank zm verwenden hat
			if(p.playerID == message.playerID && p.healthPotNumber > 0){
				//Ziehe den Heiltrank ab und erhoehe sein leben
				p.healthPotNumber--;
				p.changeHealth(30);
				//Schicke dem Client das neue Lleben des Spielers
				UsePotionMessage msg = new UsePotionMessage(1, -1, p.getPlayerID());
				comm.sendMessage(msg);
			}
		}
		
	}

	// Methode zum angriff von einem Spieler auf ein Monster
	//wenn spieler monster angreift, anderer fall sollte hier nicht vorkommen
	public void AttackMessageProcessing(AttackMessage message) {
		//Prueft wer aggressor ist(eigentlich unnötig hier)
		if (message.attackType == 1){ 
			//Prüft alle kombinationen von spielern und monstern ob der angriff zwischen ihnen ist
			for(Player p :lvl.PlayerList){
				for(Monster m : lvl.MonsterList){
					if((Math.abs(p.getXPos() - m.getXPos()) + Math.abs(p.getYPos() - m.getYPos()) <= 1) &&(p.getPlayerID() == message.attackID) && (m.getId() == message.defendID)){
						//Aendere den Lebenswert des monsters
						m.changeHealth((-1) * 12); 
					}
				}
			}
		}
		
	}


	// Teste ob in der Konsole die Cheats aktiviert hat
	public void ChatMessageProcessing(ChatMessage message) {

		try {

			// Aktiviere Cheats
			if (message.messageContent == "CheatON") {
				// Deaktivier Cheats
			} else if (message.messageContent == "CheatOFF") {
				// Speichere den Text der Nachricht fï¿½r das messagesystem
				// z.B dem anderen Spielern anzeigen
			} else {
				
			}
		} catch (Exception e) {
			System.out.println("Unbkannter Konsolen-Befehl");
		}
	}

	//Falls der Spieler in ein neues Level wechselt
	public void NextLevelMessageProcessing(NextLevelMessage message) {
		
		
		
		for(Player p : lvl.PlayerList){
			//Prueft ob Spieler den Schlüssel besitzt und auf der tür steht
			if(p.ownsKey && (lvl.lvlMaze[p.getXPos()][p.getYPos()] == 3)){
				//erstelle neues Level mit LevelID++
				lvl.newLevel(++(lvl.LevelID), 19);
				//entferne alte Monster aus Liste
				for(Monster m : lvl.MonsterList){
				     sendDeathMessage(0, m.monsterID);
				    }
				    lvl.MonsterList = new ArrayList<Monster>();
				
				//Sende dem Client eine neue Loginmessage mit den neuen Daten
				LogInMessage login = new LogInMessage(1,"1","1");
				login.setPlayer(lvl.getPlayer());
				login.setSuccess(true);
				login.setLevel(new Level(lvl,lvl.getLvlMaze()));
				login.setPlayerID(lvl.player.getPlayerID());
				comm.sendMessage(login);
			}
		}
	}
	//Aendert den Zustand des Spielers auf ownsKey 
	private void CollectKeyMessageProccessing(CollectKeyMessage message) {
		for(Player p : lvl.PlayerList){
			if(p.getPlayerID() == message.playerId && lvl.lvlMaze[p.getXPos()][p.getYPos()] == 5){
				p.ownsKey = true;
				lvl.lvlMaze[p.getXPos()][p.getYPos()] = 1;
				comm.sendMessage(message);
			}
		}
	}
	//erhoeht die Anzahl der Heiltrnke um 1 wenn Spieler einen Schritt macht
	private void CollectPotionMessageProcessing(CollectPotionMessage message) {
		for(Player p : lvl.PlayerList){
			if(p.getPlayerID() == message.playerID && p.getXPos() == message.posX && p.getYPos() == message.posY){
				p.healthPotNumber++;
				lvl.lvlMaze[p.getXPos()][p.getYPos()] = 1;
				UsePotionMessage msg = new UsePotionMessage(1, 1, p.getPlayerID());
				comm.sendMessage(msg);
			}

		}
	}

// Sende Methoden für Monster an Client	
	public void sendAttackMessage(int type, int attackID, int defendID, int hpDefender){
		AttackMessage msg = new AttackMessage(1, type, attackID, defendID, hpDefender);
		comm.sendMessage(msg);
	}
	
	public void sendAttackMessage(AttackMessage msg){
		comm.sendMessage(msg);
	}
	
	public void sendDeathMessage(int type, int id){
		DeathMessage msg = new DeathMessage(1, type, id);
		comm.sendMessage(msg);
	}
	public Levelmanagement getLvl(){
		return lvl;
	}
	
}
