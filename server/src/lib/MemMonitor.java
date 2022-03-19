package lib;

public class MemMonitor extends Thread {
	/**
	 * If mem usage higher than or equal 150MB, GC every 50ms until goes down
	 */
	@Override
	public void run() {
		while (true) {
			double usage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000;
			if (usage >= 512.0) {
				System.gc();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
