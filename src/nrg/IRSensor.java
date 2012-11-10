package nrg;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * @author Irving
 */
public class IRSensor extends SensorBase
{
    private double minRange;
    private double maxRange;
    // channel sensor is plugged into
    private AnalogChannel m_analogChannel;
    private double m_rawAngle;

    //Constructors
    public IRSensor(int channel)
    {
	//minRange = NRGIRSettings.MIN;
	minRange = 0.0;
	maxRange = NRGIRSettings.MAX;
	m_analogChannel = new AnalogChannel(channel);

	m_rawAngle = 0.0;
    }

    public double getVoltage()
    {
	if (m_analogChannel != null)
	{
	    double voltage = m_analogChannel.getAverageVoltage();
	    Debug.println(Debug.IR, "IR: " + voltage);
	    return voltage;
	}

	return 0.0;
    }

    public double getDistance() // Returns the distance to the wall
    {
	double averageVoltage = m_analogChannel.getAverageVoltage();

	Debug.print(Debug.IR, "IR Voltage: " + MathHelper.round(averageVoltage, 2) + "   ");

	// if the reading is unreasonable just return -1, handled in NRGWallTracker.trackWall()
	/*if (averageVoltage < NRGIRSettings.MIN || averageVoltage > NRGIRSettings.MAX)
	{
	Debug.println(Debug.IR, "voltage out of range");

	return -1;
	}*/
	// Don't divide by numbers (very close to) zero
	if (averageVoltage < 1E-3)
	{
	    Debug.println(Debug.IR, "voltage out of range");
	    return -1;
	}

	// plugged values into Excel, spat out regression equation: y = 7.4048 / (x ^ 0.809)
	// ...so we invert it.
	double distance = ((1 / averageVoltage) - NRGIRSettings.CAL_CONSTANT_B) / NRGIRSettings.CAL_CONSTANT_M;
	distance = MathHelper.clamp(distance, minRange, maxRange);// This is wrong -- cannot compare inches to volts.
	Debug.println(Debug.IR, "Distance (in.): " + MathHelper.round(distance, 2));

	return distance;
    }
}
