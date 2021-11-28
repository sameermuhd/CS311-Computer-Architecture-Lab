package processor;

public class Clock {
	static long currentTime = 0;
	
	public static void incrementClock()
	{
		currentTime++;
	}
	
	public static long getCurrentTime()
	{
		return currentTime;
	}
}
