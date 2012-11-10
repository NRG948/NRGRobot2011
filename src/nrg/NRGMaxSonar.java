package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author Matthew
 */
public class NRGMaxSonar
{
    //private double minRange;
    //private double maxRange;
    // channel sensor is plugged into
    private AnalogChannel m_analogChannel;
    public static final int SONAR_CHANNEL = 2;
    //this is roughly what we saw during testing
    //public static final double MIN = 0.4;   // TODO: calibrate these values
    //public static final double MAX = 40;   // MIN is value at 0 inches, MAX at 40 inches
    public static final double CAL_CONSTANT_M = 0.0681;
    public static final double CAL_CONSTANT_B = 0.1561;

    public NRGMaxSonar(int channel)
    {
	m_analogChannel = new AnalogChannel(channel);
    }

    public double getVoltage()
    {
	if (m_analogChannel != null)
	{
	    double voltage = m_analogChannel.getAverageVoltage();
	    Debug.println(Debug.SONAR, "Sonar: " + voltage);
	    return voltage;
	}

	return 0.0;
    }

    public double getDistance() // Returns the distance to the wall
    {
	/*
	double averageVoltage = m_analogChannel.getAverageVoltage();

	Debug.print(Debug.SONAR, "Sonar Voltage: " + MathHelper.round(averageVoltage, 3) + "   ");

	// if the reading is unreasonable just return -1, handled in NRGWallTracker.trackWall()
	/*if (averageVoltage < MIN || averageVoltage > MAX)
	{
	    Debug.println(Debug.IR, "voltage out of range");
	    return -1;
	}*/
	/*
	// Don't divide by numbers (very close to) zero
	if (averageVoltage < 1E-3)
	{
	    Debug.println(Debug.SONAR, "voltage out of range");
	    return -1;
	}

	// plugged values into Excel, spat out regression equation: y = 7.4048 / (x ^ 0.809)
	// ...so we invert it.
	double distance = ((1 / averageVoltage) - NRGIRSettings.CAL_CONSTANT_B) / NRGIRSettings.CAL_CONSTANT_M;
	//distance = MathHelper.clamp(distance, minRange, maxRange);// This is wrong -- cannot compare inches to volts.
	Debug.println(Debug.SONAR, "Distance (in.): " + MathHelper.round(averageVoltage, 3));

	return averageVoltage;
	 */
	return 200;
    }
}
