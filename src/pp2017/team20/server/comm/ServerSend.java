package pp2017.team20.server.comm;


import java.io.BufferedOutputStream; 
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import pp2017.team20.shared.Message;

/**
 * Threads, die das Senden von Nachrichten zum Clienten ermoeglicht
 * 
 * @author Kong, Yuxuan, 6019218
 * 
 */

public class ServerSend extends Thread {

	/**
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public Socket clientSocket;
	private LinkedBlockingQueue<Message> outputMessage = new LinkedBlockingQueue<Message>();
	public ObjectOutputStream output;
	public boolean truth = true;

	public ServerSend(Socket clientSocket, int count, LinkedBlockingQueue<Message> messageQueue) {

		this.outputMessage = messageQueue;

		try {
			output = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			output.flush();
			System.out.println("Sendthread des Servers wurde gestartet!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sendthread des Servers konnte nicht gestartet werden");
		}
	}

	@Override
	public void run() {
		while (truth) {
			messageCheck();
		}
	}

	/**
	 * Diese Methode ueberprueft, ob die Queue leer ist. Falls nicht, dann wird gesendet.
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void messageCheck() {
		if (outputMessage.isEmpty() != true)
			sendMessage();
	}

	/**
	 * Methode, die die Nachrichten zum Clienten in einem OutputStream schreibt
	 * 
	 * @author Kong, Yuxuan, 6019218
	 */
	public void sendMessage() {

		try {
			// holt sich die Nachricht aus der Queue und schickt Sie weiter
			output.writeObject(outputMessage.poll());
			output.flush();

		} catch (IOException e) {
			System.out.println("Die Nachricht konnte nicht zum Clienten gesendet werden" + e);
		}

	}

	public LinkedBlockingQueue<Message> outputqueue() {

		return outputMessage;
	}

}
