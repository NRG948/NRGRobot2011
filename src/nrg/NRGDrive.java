package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author Austin, Dustin, Stephen, and Irving
 */
public class NRGDrive extends RobotDrive
{
    private static final Jaguar frontLeft = new Jaguar(NRGDriveSettings.MOTOR_SLOT, NRGDriveSettings.FRONT_LEFT_MOTOR_CHANNEL);
    private static final Jaguar backLeft = new Jaguar(NRGDriveSettings.MOTOR_SLOT, NRGDriveSettings.BACK_LEFT_MOTOR_CHANNEL);
    private static final Jaguar frontRight = new Jaguar(NRGDriveSettings.MOTOR_SLOT, NRGDriveSettings.FRONT_RIGHT_MOTOR_CHANNEL);
    private static final Jaguar backRight = new Jaguar(NRGDriveSettings.MOTOR_SLOT, NRGDriveSettings.BACK_RIGHT_MOTOR_CHANNEL);
    private static final MotorSafetyHelper frontLeftSafety = new MotorSafetyHelper(frontLeft);
    private static final MotorSafetyHelper backLeftSafety = new MotorSafetyHelper(backLeft);
    private static final MotorSafetyHelper frontRightSafety = new MotorSafetyHelper(frontRight);
    private static final MotorSafetyHelper backRightSafety = new MotorSafetyHelper(backRight);
    private NRGInputAverager inputAveragerX;
    private NRGInputAverager inputAveragerY;
    public static final double MAX_DELTA_HEADING = 3;
    //private double lastHeading;
    private double P = 0.025;			// factor for "proportional" control
    private double I = 0.0025;			// factor for "integral" control
    private double D = 0.0025;			// factor for "derivative" control
    private double maximumOutput = NRGDriveSettings.MAX_TWIST;	// |maximum output|
    private double minimumOutput = -NRGDriveSettings.MAX_TWIST;	// |minimum output|
    private double prevError = 0.0;	    // the prior sensor input (used to compute velocity)
    private double totalError = 0.0;	    //the sum of the errors for use in the integral calc
    private double minStaticTwist = 0.34;   //the minimum twist value that actually moves our stationary robot

    public NRGDrive()
    {
	super(frontLeft, backLeft, frontRight, backRight);

	setInvertedMotor(MotorType.kFrontLeft, true);
	setInvertedMotor(MotorType.kRearLeft, true);

	setInvertedMotor(MotorType.kFrontRight, false);
	setInvertedMotor(MotorType.kRearRight, false);

	inputAveragerX = new NRGInputAverager(NRGDriveSettings.INPUT_AVERAGER_ARRAY_LENGTH);
	inputAveragerY = new NRGInputAverager(NRGDriveSettings.INPUT_AVERAGER_ARRAY_LENGTH);
    }

    public void update()
    {
	double error = 0;
	double twist = NRGJoystickInput.getDriveTwist();
	inputAveragerX.addValue(NRGJoystickInput.getDriveX());
	inputAveragerY.addValue(NRGJoystickInput.getDriveY());

	double x = inputAveragerX.getAverageValueWeighted();
	double y = inputAveragerY.getAverageValueWeighted();
	NRGLCD.println3("X: " + MathHelper.round(x, 2) + " Y: " + MathHelper.round(y, 2) + " T:" + MathHelper.round(twist, 2));

	double currentHeading = NRGSensors.gyro.getHeading();

	// Check if driver is applying twist purposely
	if (twist != 0)
	{
	    NRGSensors.gyro.setDesiredHeading(currentHeading /* + deltaHeading */);
	    // Reset all PID values
	    error = 0;
	    totalError = 0;
	    prevError = 0;
	}
	else	// Driver is not applying twist, so hold a fixed heading using PID
	{
	    error = NRGSensors.gyro.getDesiredHeading() - currentHeading;   // error = setPoint - actual
	    if (((totalError + error) * I < maximumOutput) && ((totalError + error) * I > minimumOutput))
	    {
		totalError += error;
	    }

	    twist = (P * error + I * totalError + D * (error - prevError));
	    prevError = error;
	    twist = MathHelper.clamp(twist, minimumOutput, maximumOutput);

	    // Makes sure that robot doesn't always try to twist when error is small
	    if (Math.abs(x) < 0.01 && Math.abs(y) < 0.01 && Math.abs(twist) < minStaticTwist)
	    {
		x = 0.0;
		y = 0.0;
		twist = 0.0;
	    }
	}
	//If the yellow button/light is off, Cartesian. If the yellow button/light is on, Polar.
	if(NRGIO.getDriveType())
	    mecanumDrive_Polar(NRGJoystickInput.getDriveMagnitude(), NRGJoystickInput.getDriveDirection(), NRGJoystickInput.getDriveTwist());
	else
	{
	    Debug.printRoundln(Debug.DRIVE, "Error(%0) Drive(%1, %2, %3, %4)", new double[]
		{ error, x, y, twist, currentHeading });
	    mecanumDrive_Cartesian(x, y, twist, currentHeading);
	}
    }
}
