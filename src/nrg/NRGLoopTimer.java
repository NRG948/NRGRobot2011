package nrg;

/**
 * Times how long the program takes to complete a loop.
 * @author Austin
 */
public class NRGLoopTimer
{
    private long startTime;
    private long previousTime;
    private int loops;

    /**
     * Should be called once when the program enters a new mode (disabled, autonomous, or operatorControl).
     */
    public void startRoutine()
    {
	startTime = System.currentTimeMillis();
	previousTime = startTime;
	loops = 0;
    }

    /**
     * Starts a new loop within a given routine.
     * @return the time in milliseconds for the previous loop
     */
    public long nextLoop()
    {
	loops++;
	long tempTime = System.currentTimeMillis();
	long dTime = tempTime - previousTime;
	previousTime = tempTime;
	return dTime;
    }

    /**
     * Gets the average loop time, in milliseconds, for the current routine
     * @return the average loop time
     */
    public long getAverageLoopTime()
    {
	return (long) ((previousTime - startTime + 0.5f) / loops);
    }
}
