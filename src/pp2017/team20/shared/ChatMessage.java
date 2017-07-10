package pp2017.team20.shared;

/**
 * 
 * Nachrichtenklasse, um Nachrichten im Chat anzeigen zu lassen und um Cheats zu verarbeiten
 * 
 * @author Wagner, Tobias, 5416213
 *
 */

public class ChatMessage extends Message {
	
	//Variable um den Inhalt des Chats anzugeben
	public String messageContent;
	
	//Variable fuer den zu benutzenden Cheat
	public String Cheat;
	
	/**
	 * 
	 * Konstruktor fuer die Nachricht Chat
	 * 
	 * @author Wagner, Tobias, 5416213
	 * 
	 */
	public ChatMessage(String newMessage){
		messageContent = newMessage;
	}
	
	//Getter und Setter
	public String getNMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
			

}
