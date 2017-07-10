package pp2017.team20.shared;


public class ThreadWaitForMessage {
	/**
	 * Lässt den Thread für eine gewisse Zeit warten
	 * 
	 * @author Yuxuan Kong 6019218
	 * @param waitingTime
	 *            WarteZeit
	 * 
	 * 
	 */
	public static void waitFor(long waitingTime) {
		long currentTime = System.currentTimeMillis();
		Thread.currentThread().setPriority(1);

		while (currentTime + waitingTime > System.currentTimeMillis())
			Thread.yield();
	}
}