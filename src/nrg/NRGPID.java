package nrg;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.parsing.IUtility;
import edu.wpi.first.wpilibj.util.BoundaryException;
import java.util.TimerTask;

/**
 *
 * @author Stephen
 */
public class NRGPID implements IUtility
{
    public static final double kDefaultPeriod = .05;
    private double P;			// factor for "proportional" control
    private double I;			// factor for "integral" control
    private double D;			// factor for "derivative" control
    private double maximumOutput = 1.0;	// |maximum output|
    private double minimumOutput = -1.0;	// |minimum output|
    private double maximumInput = 350.0;	// maximum input - limit setpoint to this
    private double minimumInput = 0.0;		// minimum input - limit setpoint to this
    private boolean continuous = false;	// do the endpoints wrap around? eg. Absolute encoder
    private boolean enabled = false; 			//is the pid controller enabled
    private double prevError = 0.0;	// the prior sensor input (used to compute velocity)
    private double totalError = 0.0; //the sum of the errors for use in the integral calc
    private double tolerance = 2.0;	//the percentage error that is considered on target
    private double setpoint = 0.0;
    private double error = 0.0;
    private double result = 0.0;
    private double period = kDefaultPeriod;
    private double iClamp = 1.0;
    PIDSource pidInput;
    PIDOutput pidOutput;
    //PIDOutput pidOutput2;
    java.util.Timer controlLoop;
    private boolean isShoulder;

    private class PIDTask extends TimerTask
    {
	private NRGPID controller;

	public PIDTask(NRGPID controller)
	{
	    if (controller == null)
	    {
		throw new NullPointerException("Given NRGPID was null");
	    }
	    this.controller = controller;
	}

	public void run()
	{
	    controller.calculate();
	}
    }

    /**
     * Allocate a PID object with the given constants for P, I, D
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output value
     * @param period the loop time for doing calculations. This particularly effects calculations of the
     * integral and differential terms. The default is 50ms.
     */
    public NRGPID(double Kp, double Ki, double Kd,
		  PIDSource source, PIDOutput output, double iclamp, boolean isShoulder)
    {
	if (source == null)
	    throw new NullPointerException("Null PIDSource was given");
	if (output == null)
	    throw new NullPointerException("Null PIDOutput was given");

	controlLoop = new java.util.Timer();

	P = Kp;
	I = Ki;
	D = Kd;
	this.isShoulder = isShoulder;
	pidInput = source;
	pidOutput = output;
	this.iClamp = iclamp;
	controlLoop.schedule(new PIDTask(this), 0L, (long) (period * 1000));
    }

    /**
     * Allocate a PID object with the given constants for P, I, D
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output value
     * @param period the loop time for doing calculations. This particularly effects calculations of the
     * integral and differential terms. The default is 50ms.
     */
    /*public NRGPID(double Kp, double Ki, double Kd,
    PIDSource source, PIDOutput output, PIDOutput output2)
    {
    if (source == null)
    throw new NullPointerException("Null PIDSource was given");
    if (output == null)
    throw new NullPointerException("Null PIDOutput was given");

    controlLoop = new java.util.Timer();

    P = Kp;
    I = Ki;
    D = Kd;

    pidInput = source;
    pidOutput = output;
    pidOutput = output2;

    controlLoop.schedule(new PIDTask(this), 0L, (long) (period * 1000));
    }*/
    /**
     * Free the PID object
     */
    protected void free()
    {
	controlLoop.cancel();
	controlLoop = null;
    }

    /**
     * Read the input, calculate the output accordingly, and write to the output.
     * This should only be called by the PIDTask
     * and is created during initialization.
     */
    private void calculate()
    {
	boolean isEnabled;
	PIDSource pidInput;

	synchronized (this)
	{
	    if (this.pidInput == null)
	    {
		return;
	    }
	    if (this.pidOutput == null)
	    {
		return;
	    }
	    isEnabled = enabled; // take snapshot of these values...
	    pidInput = this.pidInput;
	}

	if (isEnabled)
	{
	    double input = pidInput.pidGet();
	    double result;
	    PIDOutput pidOutput = null;

	    synchronized (this)
	    {
		error = setpoint - input;
		if (continuous)
		{
		    if (Math.abs(error) > (maximumInput - minimumInput) / 2)
		    {
			if (error > 0)
			{
			    error = error - maximumInput + minimumInput;
			}
			else
			{
			    error = error + maximumInput - minimumInput;
			}
		    }
		}

		if (((totalError + error) * I < iClamp * maximumOutput)
			&& ((totalError + error) * I > iClamp * minimumOutput))
		{
		    totalError += error;
		}

		this.result = (P * error + I * totalError + D * (error - prevError));
		prevError = error;

		if (this.result > maximumOutput)
		{
		    this.result = maximumOutput;
		}
		else if (this.result < minimumOutput)
		{
		    this.result = minimumOutput;
		}
		pidOutput = this.pidOutput;
		result = this.result;
	    }
	    pidOutput.pidWrite(result);
	}
    }

    /**
     * Set the PID Controller gain parameters.
     * Set the proportional, integral, and differential coefficients.
     * @param p Proportional coefficient
     * @param i Integral coefficient
     * @param d Differential coefficient
     */
    public synchronized void setPID(double p, double i, double d)
    {
	P = p;
	I = i;
	D = d;
    }

    /**
     * Get the Proportional coefficient
     * @return proportional coefficient
     */
    public double getP()
    {
	return P;
    }

    /**
     * Get the Integral coefficient
     * @return integral coefficient
     */
    public double getI()
    {
	return I;
    }

    /**
     * Get the Differential coefficient
     * @return differential coefficient
     */
    public synchronized double getD()
    {
	return D;
    }

    /**
     * Return the current PID result
     * This is always centered on zero and constrained the the max and min outs
     * @return the latest calculated output
     */
    public synchronized double get()
    {
	return result;
    }

    /**
     *  Set the PID controller to consider the input to be continuous,
     *  Rather then using the max and min in as constraints, it considers them to
     *  be the same point and automatically calculates the shortest route to
     *  the setpoint.
     * @param continuous Set to true turns on continuous, false turns off continuous
     */
    public synchronized void setContinuous(boolean continuous)
    {
	this.continuous = continuous;
    }

    /**
     *  Set the PID controller to consider the input to be continuous,
     *  Rather then using the max and min in as constraints, it considers them to
     *  be the same point and automatically calculates the shortest route to
     *  the setpoint.
     */
    public synchronized void setContinuous()
    {
	this.setContinuous(true);
    }

    /**
     * Sets the maximum and minimum values expected from the input.
     *
     * @param minimumInput the minimum value expected from the input
     * @param maximumInput the maximum value expected from the output
     */
    public synchronized void setInputRange(double minimumInput, double maximumInput)
    {
	if (minimumInput > maximumInput)
	{
	    throw new BoundaryException("Lower bound is greater than upper bound");
	}
	this.minimumInput = minimumInput;
	this.maximumInput = maximumInput;
	setAngleSetpoint(setpoint);
    }

    /**
     * Sets the minimum and maximum values to write.
     *
     * @param minimumOutput the minimum value to write to the output
     * @param maximumOutput the maximum value to write to the output
     */
    public synchronized void setOutputRange(double minimumOutput, double maximumOutput)
    {
	if (minimumOutput > maximumOutput)
	{
	    throw new BoundaryException("Lower bound is greater than upper bound");
	}
	this.minimumOutput = minimumOutput;
	this.maximumOutput = maximumOutput;
    }

    /**
     * Set the setpoint for the PIDController
     * @param newSetpoint the desired setpoint
     */
    public synchronized void setAngleSetpoint(double newSetpoint)
    {
	setpoint = MathHelper.clamp(newSetpoint, minimumInput, maximumInput);
    }

    /**
     * Returns the current setpoint of the PIDController
     * @return the current setpoint
     */
    public synchronized double getAngleSetpoint()
    {
	return setpoint;
    }

    public synchronized double getProportionSetpoint()
    {
	if (isShoulder)
	{
	    return NRGArm.shoulderSetpointAngleToProportion(setpoint);
	}
	else
	{
	    return NRGArm.elbowSetpointAngleToProportion(setpoint);
	}
    }

    /**
     * Returns the current difference of the input from the setpoint
     * @return the current error
     */
    public synchronized double getError()
    {
	return error;
    }

    /**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Input of 15.0 = 15 percent)
     * @param percent error which is tolerable
     */
    public synchronized void setTolerance(double percent)
    {
	tolerance = percent;
    }

    /**
     * Return true if the error is within the percentage of the total input range,
     * determined by setTolerance. This assumes that the maximum and minimum input
     * were set using setInput.
     * @return true if the error is less than the tolerance
     */
    public synchronized boolean onTarget()
    {
	return (Math.abs(error) < tolerance / 100
		* (maximumInput - minimumInput));
    }

    /**
     * Begin running the PIDController
     */
    public synchronized void enable()
    {
	if (!enabled)
	{
	    prevError = 0.0;
	    totalError = 0.0;
	}
	enabled = true;
    }

    /**
     * Stop running the PIDController, this sets the output to zero before stopping.
     */
    public synchronized void disable()
    {
	pidOutput.pidWrite(0);
	/*if (!pidOutput2.equals(null))
	{
	pidOutput2.pidWrite(0);
	}*/
	enabled = false;
    }

    /**
     * Return true if PIDController is enabled.
     */
    public synchronized boolean isEnable()
    {
	return enabled;
    }

    /**
     * Reset the previous error,, the integral term, and disable the controller.
     */
    public synchronized void reset()
    {
	disable();
	prevError = 0;
	totalError = 0;
	result = 0;
    }

    public synchronized double getResult()
    {
	return result;
    }
}
