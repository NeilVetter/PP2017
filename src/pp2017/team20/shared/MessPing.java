package pp2017.team20.shared;

import java.io.Serializable;

/**
 * 
 * Diese Klasse beinhaltet den Ping-Signal 
 * 
 * 
 * @author Yuxuan Kong 6019218
 */
public class MessPing extends Message implements Serializable {

	private static final long serialVersionUID = -4125004909828171573L;

	
	public MessPing(int type, int subType) {
		super(type, subType);
	}

}
