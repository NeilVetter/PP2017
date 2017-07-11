package pp2017.team20.shared;

import pp2017.team20.server.map.*;

public class SendLevelMessage extends Message {

public Maze maze;
	
	
	public SendLevelMessage(int ClientID,Maze maze){
		super(ClientID);
		this.maze=maze;
	}
	public Maze getMaze(){
		return maze;
	}

}
