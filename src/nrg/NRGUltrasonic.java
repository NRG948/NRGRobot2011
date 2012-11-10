package nrg;

import edu.wpi.first.wpilibj.*;
import java.util.TimerTask;

/**
 * @author Matthew && Dustin
 */
public class NRGUltrasonic
{
    public static final boolean USING_ULTRASONIC = false;
    private Timer timer;
    private java.util.Timer controlLoop;
    private DigitalModule digMod;
    private I2C i2c;
    private static final int ULTRASONIC_SLOT = 4;
    private static final int ULTRASONIC_ADDRESS = 0xE0;
    private static final int ECHO_ADDRESS_HIGH = 2;
    private static final int ECHO_ADDRESS_LOW = 3;
    private int dist;
    private double period;
    private boolean writeFail;

    private class UltrasonicTask extends TimerTask
    {
	private NRGUltrasonic u;

	public UltrasonicTask(NRGUltrasonic ultrasonic)
	{
	    if (ultrasonic == null)
	    {
		throw new NullPointerException("Given Ultrasonic was null");
	    }
	    this.u = ultrasonic;
	}

	public void run()
	{
	    u.setDist();
	}
    }

    public NRGUltrasonic()
    {
	dist = 0;
	writeFail = false;
	if(USING_ULTRASONIC)
	{
	    digMod = DigitalModule.getInstance(ULTRASONIC_SLOT);
	    i2c = digMod.getI2C(ULTRASONIC_ADDRESS);
	    timer = new Timer();
	    controlLoop = new java.util.Timer();
	    if (i2c.addressOnly())
	    {
		NRGLCD.println4("ERR: No Ultrasonic!");
		Debug.println(Debug.FATAL_EXCEPTIONS, "ERROR: Could not find Ultrasonic.");
	    }
	    //Range: Three meters.
	    i2c.write(0x02, 0x46);
	    //Analogue Gain: 317
	    i2c.write(0x01, 0x18);
	    period = .05;
	    setDist();
	}
    }

    /**
     * Ends the control loop.
     */
    public void free()
    {
	controlLoop.cancel();
	controlLoop = null;
    }

    public int getDist()
    {
	return dist;
    }

    public boolean getWriteFail()
    {
	return writeFail;
    }

    //gets distance
    private void setDist()
    {
	byte high, low;

	try
	{
	    //setting range to centimeters
	    if (i2c.write(0x00, 0x51))
	    {
		writeFail = true;
		Debug.println(Debug.ULTRASONIC, "Attemted write to ultrasonic, failed.");
	    }
	    else
	    {
		writeFail = false;
		Debug.println(Debug.ULTRASONIC, "Wrote to ultrasonic, began rangefinding in centimeters.");
		timer.reset();
		timer.start();

		try
		{
		    boolean done = false;
		    Timer.delay(.065);
		    for (int i = 0; i < 10; i++)
		    {
			byte[] buffer = new byte[1];
			if (i2c.read(ECHO_ADDRESS_HIGH, 1, buffer))
			{
			    //Debug.println(Debug.ULTRASONIC, "Attempted read to ultrasonic high, failed.");
			}
			else
			{
			    high = buffer[0];
			    if (i2c.read(ECHO_ADDRESS_LOW, 1, buffer))
			    {
				//Debug.println(Debug.ULTRASONIC, "Attempted read to ultrasonic low, failed.");
			    }
			    else
			    {
				low = buffer[0];
				dist = high * 256 + ((int) low & 0xFF);
				Debug.println(Debug.ULTRASONIC, "Try: " + i + " Time: " + timer.get() + " High: " + high + " Low: " + low + " Dist: " + dist);
				done = true;
			    }
			}
			if (done)
			    break;
			//Debug.println(Debug.ULTRASONIC, "Try "+i+" failed.");
			if (i == 9)
			    Debug.println(Debug.ULTRASONIC, "TRIED 10 TIMES!");
			Timer.delay(.02);
		    }
		}
		catch (Exception e)
		{
		    Debug.println(Debug.FATAL_EXCEPTIONS, "Attempt to read ultrasonic caused a fatal exception.");
		}
	    }
	}
	catch (Exception e)
	{
	    Debug.println(Debug.FATAL_EXCEPTIONS, "Attempt to write to ultrasonic caused a fatal exception");
	}
	//Schedule next loop.
	controlLoop.schedule(new UltrasonicTask(this), (long) (period * 1000));

    }
}
