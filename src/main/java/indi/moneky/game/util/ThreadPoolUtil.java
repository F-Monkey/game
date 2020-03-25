package indi.moneky.game.util;

public class ThreadPoolUtil {
	private ThreadPoolUtil() {
	};

	public static int ioIntesivePoolSize() {
		double blockingCoefficient = 0.9;
		return poolSize(blockingCoefficient);
	}

	/**
	 * @param threadshol
	 * @return Thread pool size
	 */
	public static int poolSize(double blockingCoefficient) {
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		return poolSize;
	}
}
