package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * MAE - Magnetic Angle Encoder. Find angle of something.
 * @author Irving
 */
public class MAE extends SensorBase implements PIDSource
{
    private AnalogChannel m_analog;
    private double m_rawAngle;
    private double m_offset;
    private boolean m_reverse;
    private double prevAngle;
    private double maxPeriod;
    private Timer maeTimer;
    private static final double VOLTS_TO_DEGREES = 72.0d * 360.0 / 366.1;

    public MAE(int slot, int channel, boolean reverse)
    {
	m_analog = new AnalogChannel(slot, channel);
	m_rawAngle = 0.0;
	m_offset = 0.0;
	m_reverse = reverse;
	prevAngle = 0.0;
	maxPeriod = 0.0;
	maeTimer = new Timer();
    }

    /* getRawAngle() returns the raw angle indicated by a kicker MAE (note that we invert the
     * angle if m_reverse is set, so that increasing angles always mean more kick power no
     * matter which way the MAE is oriented). Output shall ALWAYS be between 0 and 360 degrees.
     */
    private double getRawAngle()
    {
	if (m_analog != null)
	{
	    double volts = m_analog.getAverageVoltage();

	    // This test was added because we were getting spurious zero volt samples once in
	    // a while. So if this happens, we just return the last 'good' angle we sampled.
	    if (volts < 0.1)
		return m_rawAngle;

	    m_rawAngle = volts * VOLTS_TO_DEGREES;

	    if (this.m_reverse)
		m_rawAngle = 360.0 - m_rawAngle;

	    Debug.println(Debug.MAE, "  " + MathHelper.round(volts, 2) + "V = " + MathHelper.round(m_rawAngle, 1) + "deg  ");
	}
	return m_rawAngle;
    }

    /* getAngle returns the zero-based angle of the kicker (zero is fully relaxed, wound-up angles are always positive)
     */
    public double getAngle()
    {
	double angle = getRawAngle() - m_offset;
	Debug.println(Debug.MAE, "m_offset=" + MathHelper.round(m_offset, 1) + " angle=" + MathHelper.round(angle, 1));
	/* Note: we hand adjust the MAEs so they operate in the approximate middle of their range so we don't have
	 * to deal with any mathematical weirdness and electrical noise when they wrap around from 360 to 0 degrees.
	 * The kicker should never move more than about 120 degrees, so this test corrects for the case
	 * where MAE slippage results in "negative" or wrap-around angle values.
	if (angle > 355.0 || angle < -1.0)
	{
	zero();
	angle = 0.0;
	} */
	return angle;
    }

    // This method is intended to be run whenever we need to reset the MAE relaxed angle back to zero.
    // Don't call this method if the kicker dog is engaged!
    public void zero()
    {
	m_offset = this.getRawAngle();
    }

    public double pidGet()
    {
	return getAngle();
    }

    //These next few methods are all an experiment with getStopped() for encoders, comment out if it stops anything from working
    //I do hope that I'm not just crazily writing methods, someone implement these please.
    //everything is in seconds..... well besides get()

    public boolean getDirection()
    {
	return true; //Again, no idea, supposedly the direction is + when true? and what does direction do?
    }

    public double getPeriod()
    {
	if (getAngle() == prevAngle)
	{
	    while (getAngle() == prevAngle)
	    {
		maeTimer.start();
	    }
	    maeTimer.stop();
	}
	double time = maeTimer.get() / 1000.0;
	maeTimer.reset();
	return time;
    }

    public boolean getStopped() // main purpose
    {
	if (maeTimer.get() / 1000.0 > maxPeriod && getAngle() == prevAngle)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
    public void setMaxPeriod(double max)
    {
	maxPeriod = max;
    }

    //fairly useless methods, i just implememnted them because i was forced to -.-
    public void reset()
    {
	prevAngle = getAngle();
	//double time = maeTimer.get() / 1000.0;, nvm, useless
	maeTimer.reset();
    }

    public void start()
    {
	maeTimer.start();
    }

    public void stop()
    {
	maeTimer.stop();
	//double time = maeTimer.get() / 1000.0; nvm, useless
    }
}
