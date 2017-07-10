package pp2017.team20.server.engine;

import java.util.*;

import pp2017.team20.shared.*;

public class MessageProcessing {

//Database database = new database();
	//movement
	private boolean neighbour;
	private int Health;
	private int Mana;
	private boolean moveAllowed;

	Queue <Message> MessageQueue= new LinkedList<Message>();
	private LinkedList<Monster> MonsterList = new LinkedList<Monster>();
	public ArrayList <Player> PlayerList = new ArrayList<Player>();
	public ArrayList <String> ChatList = new ArrayList<String>();
	


	
	
	//Leerer Konstruktor f�r MassageProcessing
	public MessageProcessing(){
		
	}
	
	//Nimmt eine Message aus dem Queue und entscheidet welcher Unterklasse sie angeh�rt
	public void MessageProcess(Queue <Message> queue){
		
		
		
		
		try{
			//Pr�ft solange eine Message im Queue ist welchen Typ die Nachricht hat
			while(!queue.isEmpty()){
			
				Message message = queue.poll();
				WhatMessageType(message);
			}
		}catch(Exception e){
			
		}
	}
	//Wird in MessageProcess aufgerufen und entscheidet welchen Typs die Message ist 

	public void WhatMessageType(Message message) {
		
		//testet welchen typs die nachricht ist
		if(message instanceof LogInMessage){
			//Castet die Nachricht um sie als LoginMessage benutzen zu k�nnen
			LogInMessage login = (LogInMessage) message;
			//ruft die zugeh�rige Processing-Methode auf
			LogInMessageProcessing(login);
		}
		
		else if (message instanceof LogOutMessage){
			LogOutMessage logout = (LogOutMessage) message;
			LogOutMessageProcessing(logout);
		
		}else if (message instanceof MoveMessage){
			MoveMessage movement = (MoveMessage) message;
			MoveMessageProcessing(movement);
		
		}else if (message instanceof HealthPotMessage){
			HealthPotMessage healthpot = (HealthPotMessage) message;
			HealthPotMessageProcessing(healthpot);
		
		}else if (message instanceof ManaPotMessage){
			ManaPotMessage manapot = (ManaPotMessage) message;
			ManaPotMessageProcessing(manapot);
		
		}else if (message instanceof AttackMessage){
			AttackMessage playerattack = (AttackMessage) message;
			AttackMessageProcessing(playerattack);
		
		}else if (message instanceof PlayerDeadMessage){
			PlayerDeadMessage playerdead = (PlayerDeadMessage) message;
			PlayerDeadMessageProcessing(playerdead);
		
		}else if (message instanceof MonsterDeadMessage){
			MonsterDeadMessage monsterdead = (MonsterDeadMessage) message;
			MonsterDeadMessageProcessing(monsterdead);
		
		}else if(message instanceof ItemPickUpMessage){
			ItemPickUpMessage itempick = (ItemPickUpMessage) message;
			ItemPickUpMessageProcessing(itempick);
		}else if(message instanceof NextLevelMessage){
			NextLevelMessage next = (NextLevelMessage) message;
			NextLevelMessageProcessing(next);
		}else if(message instanceof LevelMessage){
			LevelMessage lvl = (LevelMessage) message;
			LevelMessageProcessing(lvl);
		}
	}
	
//Wird gesendet nachdem Levelgenerator in der Datenbank gepr�ft hat,
//ob der Nutzer schon existiert
//oder andernfalls neu eingetragen wurde


	public void LoginMessageProcessing(LogInMessage message){
		
		//wenn der Levelgenerator succsess==true angibt hat der login bei ihm funktioniert
		if(message.succsess==true){
			// �ndert den Zustand des Spieler zu LoggedIN so dass dies immer bekannt
			message.player.loggedIN=true;
			//F�gt den Spieler der Liste aktiver Spieler bei
			PlayerList.add(message.player);
			
			System.out.println(message.player.playername+"'s login war erfolgreich");
		}else{
			// meldet zur�ck, dass der login gescheitert ist
			message.player.loggedIN= false;
			
			System.out.println(message.player.playername+"'s login war nicht erfolgreich");
		}
	}
	
	public void LogoutMessageProcessing(LogOutMessage message){
		
		//�ndert den Zustand des Spielers zu LoggedOFF
		message.player.loggedIN=false;
		//Entfernt den Spieler aus der Liste der aktiven Spieler
		PlayerList.remove(message.player);
		
		System.out.println(message.player.playername+" "+"ist abgemeldet");
	}

	public void MovementMessageProcessing(MoveMessage message){
			
		//Test, ob das Feld benachbart zur vorherigen Position des Spielers
			
		if(Math.abs(message.DPosX-message.player.PosX)+Math.abs(message.DPosY-message.player.PosY)<=1){
			
			neighbour = true;
			
			//falls das feld benachbart ist...
			if(neighbour == true){
					
				//...Teste ob angesprochenes Feld begehbar ist
				if(message.player.PlayerMap[message.DPosX][message.DPosY]!=0);
										
					//Setze die Position des Spielers auf die aus der Message
					message.player.PosX=message.DPosX;
					message.player.PosY=message.DPosY;
					//Gebe zur�ck dass der Schritt erfolgreich
					message.succsess =true;
					System.out.println(message.player.playername+" "+"Hat sich ein Feld bewegt");
						
			}else{
				//Gebe zur�ck das der Schritt gesscheitert
				message.succsess=false;
				System.out.println(message.player.playername+" "+"Hat sich nicht bewegt");

			}
		}
		//Position des Spielers nach dem Schritt(unver�ndert falls fehlgeschlagen
		System.out.println(message.player.PosX);
		System.out.println(message.player.PosY);
	}
	
		//Nehmen eines Lebens-trankts
	public void HealthPotMessageProcessing(HealthPotMessage message){
	
		// falls der Spieler beim aktiviern des Tranks volles Leben hat
		// passiert nichts, kein Trank wird verbraucht 
		if(message.Health==100){
			System.out.println("Leben ist schon voll");

			//Testet ob der Spieler einen Trank besitzt
		}else if(message.HealthPotNumber>0){
			
			//Erh�ht das Leben des Spielers
			Health = message.Health +30;
			//Testet ob leben das maxleben �bersteigt undd falls dem so ist
			//setzte Leben gleich Maxeben
			if (Health>=100){
				Health=100;
			}
			//Nach erfolgreicher benutzung des Tranks reduziere die anzahl der tr�nke um 1
			//Soll noch ge�ndert werden um mit der Itemliste zu funktionieren
			message.HealthPotNumber--;
		}
		System.out.println(message.player.playername+" "+"wurde geheilt");
		System.out.println(Health);
		
		System.out.println(message.HealthPotNumber);

	}

	public void ManaPotMessageProcessing(ManaPotMessage message){
	
		// falls der Spieler beim aktiviern des Tranks volles Mana hat
		// passiert nichts, kein Trank wird verbraucht
		if(message.Mana==100){
			System.out.println("Mana ist schon voll");
		//Testet ob der Spieler einen Trank besitzt
		}else if(message.ManaPotNumber>0){
		
			//Erh�ht das Mana des Spielers
			Mana = message.Mana +30;
			//Testet ob Mana das Maxmana �bersteigt undd falls dem so ist
			//setzte Mana gleich Maxmana
			if (Mana>=100){
				Mana=100;
			}
			//Nach erfolgreicher benutzung des Tranks reduziere die anzahl der tr�nke um 1
			//Soll noch ge�ndert werden um mit der Itemliste zu funktionieren
			message.ManaPotNumber--;
		}
	}

	//Methode zum angriff von einem Spielr auf ein Monster
	//Funktioniert noch nicht, da Monster Klasse nicht eingebungden
		public void PlayerAttackMessageProcessing(AttackMessage message){
	
			if(Attacker is Human){
				
			
			//Monster pr�ft ob der Anngriff erfolgen darf und schickt dann die Nachricht
			if(moveAllowed==true){
				
				//f�ge dem Monster schaden zu
				message.monster.setDamage(10);
				
				//Falls das Monster auf <=0 Hp f�llt entferne es aus dem Spiel
				if(message.monster.health<=0){
					MonsterList.remove(message.monster);
					System.out.println("Monster gestorben");
				}
			}
			}else{
				if(moveAllowed==true){
					
					//f�ge dem Player Schaden zu
					message.player.setDamage(10);
					
					//Falls der Player <=0 Hp f�llt entferne Ihn aus der Liste 
					if(message.player.health<=0){
						Player.LevelList.remove(message.player);
						//Benutze Methode aus Levelmanagement um den Spieler an das anfangsfeld zu stellen
						Levelmanagement l = new Levelmanagement(message.player);
						l.placePlayer(message.player);
						//regeneriere die stats des spielers
						message.player.setHealth(100);
						message.player.setMana(100);
					}
				}
			}
	}
			
	//Methode falls der Spieler noch durch andere Einwirkung sterben kann
	public void PlayerDeadMessageProcessing(PlayerDeadMessage message){
		
		//entferne Spieler aus der Liste
		PlayerList.remove(message.player);
		//Benutze Methode aus Levelmanagement um den Spieler an das Anfangsfeld zu stellen
		Levelmanagement l = new Levelmanagement(message.player);
		l.placePlayer(message.player);
		//regeneriere die stats des spielers
		message.player.setHealth(100);
		message.player.setMana(100);
		
	}
	
	//Methode Falls Monster durch anderes Event sterben kann
	public void MonsterDeadMessageProcessing(MonsterDeadMessage message){
		
		//Entferne Monster
		MonsterList.remove(message.monster);
		
	}
	
	//F�gt einen Spieler der Datenbank zu falls er noch keinen Eintrag hat
	
	//Teste ob in der Konsole die Cheats aktiviert hat
	public void ChatMessageProcessing(ChatMessage message){
		
		try{
		
			// Aktiviere Cheats
			if(message.getChat()=="CheatON"){
				System.out.println("Cheats aktiviert");
			//Deaktivier Cheats
			}else if(message.getChat()=="CheatOFF"){
				System.out.println("Cheats deaktiviert");
			//Speichere den Text der Nachricht f�r das messagesystem
			//z.B dem anderen Spielern anzeigen
			}else{
				ChatList.add(message.chat);
			}
		}catch(Exception e){
			System.out.println("Unbkannter Befehl");
		}
	}
	//Unterscheidet unter den verf�gbaren Items und f�gt sie dem inventar hinzu
	public void ItemPickUpMessageProcessing(ItemPickUpMessage message){
		
		switch(message.ItemID){
		
		//Healthpot aufnehmen
		case 0:
			//erzeugt einen HealthPot und f�gt ihm dem Inventar hinzu
			HealthPot h = new HealthPot();
			message.player.HealthPotNumber++;
			message.player.ItemList.add(h);
			System.out.println("HealthPot aufgenommen");
			break;
			
		//Manapot aufnehmen 
		case 1:
			//erzeugt einen ManaPot und f�gt ihm dem Inventar hinzu
			ManaPot m = new ManaPot();
			message.player.ManaPotNumber++;
			message.player.ItemList.add(m);
			System.out.println("ManaPot aufgenommen");
			break;
		//Schl�ssel aufnehmen
		case 2:
			//�ndert den Zustand des Spielers so dass er den schl�ssel besitzt
			//So kann er t�ren �ffnen
			message.player.GotKey=true;
		}
	}
	
	public void NextLevelMessageProcessing(NextLevelMessage message){
		
		Levelmanagement level = new Levelmanagement(message.player);
		//Setzt den spieler in n�chstes level
		level.newLevel(message.player.PlayerLvl+1);
		//Setze den Spieler an den Eingang
		level.placePlayer(message.player);
	}
	public void LevelMessageProcessing(LevelMessage message){
		
		System.out.println("Eine Kopie des Levels wurde dem Client geschickt");
		
	}
}
