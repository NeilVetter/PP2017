package pp2017.team20.shared;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 	type = Nachrichtent Typen, die für die Spieleweltverwaltung und die An- / und Abmeldung sind 
 * <p>
 * 
 *  subtype = Nachrichten Subtypen, dass die Nachrichten Typen spezifiert, s.B. moveCharacter, attackMonster, openDoor etc.
 * <p>
 * 
 * 
 * @author Yuxuan Kong 6019218
*/
public abstract class Message implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private int receiver; // dient zur Identifikation des Empfaengers

	public boolean success = false;
	
	//Getter und Setter
	
	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	
	private int clientID;
	
	public Message(int clientID) {
		this.clientID=clientID;
	}
	
	public int getclientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID=clientID;
		
	}
	
	
}