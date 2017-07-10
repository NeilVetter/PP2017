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
	
	// timestamp
//	private final long timestamp;
	
//	private int type;
//	
//	private int subType;
	

//	public Message(int type, int subType){
//		this.timestamp = System.currentTimeMillis();
//		this.type = type;
//		this.subType = subType;
//
//	}
	
//	
//	public void setType(int type){
//		this.type = type;
//	}
//	
//	
//	public int getType(){
//		return type;
//	}
//
//	public void setSubType(int subType){
//		this.subType = subType;
//	}
//	
//	public int getSubType(){
//		return subType;
//	}

	
//	public String toString() {
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"yyyy-MM-DD hh:mm:ss");
//		return (dateFormat.format(new Date(this.timestamp))
//				+ " Type: " + this.getType() + " Subtype: " + this.getSubType());
//	}
	
	private int receiver; // dient zur Identifikation des Empfaengers

	public boolean success = false;
	
	//Getter und Setter
	
	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	
	
}