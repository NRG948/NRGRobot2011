package nrg.cmd;

import nrg.*;

/**
 * @author Dustin
 */
public class RotateToCommand extends CommandBase
{
    private static final double MAX_OUTPUT = NRGDriveSettings.MAX_TWIST;	// |maximum output|
    private static final double MIN_OUTPUT = -NRGDriveSettings.MAX_TWIST;	// |minimum output|
    private static final double MIN_STATIC_TWIST = 0.34;   //the minimum twist value that actually moves our stationary robot
    private static final double P = 0.025;			// factor for "proportional" control
    private static final double I = 0.0025;			// factor for "integral" control
    private static final double D = 0.0025;			// factor for "derivative" control
    private double error;
    private double prevError;	    // the prior sensor input (used to compute velocity)
    private double totalError;	    //the sum of the errors for use in the integral calc

    public RotateToCommand(double err)
    {
	error = err;
    }

    public void init()
    {
	prevError = 0.0;
	totalError = 0.0;
	NRGSensors.gyro.setDesiredHeading(error);
    }

    public boolean run()
    {
	double currentHeading = NRGSensors.gyro.getHeading();
	error = NRGSensors.gyro.getDesiredHeading() - currentHeading;   // error = setPoint - actual
	if (((totalError + error) * I < MAX_OUTPUT) && ((totalError + error) * I > MIN_OUTPUT))
	{
	    totalError += error;
	}

	double twist = (P * error + I * totalError + D * (error - prevError));
	prevError = error;
	twist = MathHelper.clamp(twist, MIN_OUTPUT, MAX_OUTPUT);

	NRGRobot.getDrive().mecanumDrive_Cartesian(0, 0, twist, currentHeading);

	return error < 0.01 || Math.abs(twist) < MIN_STATIC_TWIST;
    }

    public void finalize()
    {
	NRGRobot.getDrive().mecanumDrive_Cartesian(0, 0, 0, NRGSensors.gyro.getHeading());
    }
}
