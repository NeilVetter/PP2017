package pp2017.team20.server;

import pp2017.team20.server.engine.MessageProcessing;

public class ServerMain {
//	public static void main(String[] args) {
//
//		System.out.println("test");
//
//		MessageProcessing messageProcessing = new MessageProcessing();
//
//		messageProcessing.RecieveMessage();
//	}
	public static void main(String args[]) {

		MessageProcessing verwaltung = new MessageProcessing();
		verwaltung.RecieveMessage();
	}
}
