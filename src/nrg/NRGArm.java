package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author Stephen, Matthew, and Dustin
 */
public class NRGArm
{
    private Jaguar shoulderJoint;
    private Victor elbowJoint;
    private MAE shoulderEncoder;
    private MAE elbowEncoder;
    private NRGPID shoulderPID;
    private NRGPID elbowPID;
    private int potentiometerState;
    private int state;
    private final boolean USING_SHOULDER = true;
    private final boolean USING_ELBOW = true;
    private boolean USING_PID_MANUAL = false;
    private static double elbowAngleMax = NRGArmSettings.ELBOW_ANGLE_MAX;
    private static double elbowAngleRange = NRGArmSettings.ELBOW_ANGLE_RANGE;
    private static double elbowAngleMin = elbowAngleMax - elbowAngleRange;
    private static double shoulderAngleMax = NRGArmSettings.SHOULDER_ANGLE_MAX;
    private static double shoulderAngleRange = NRGArmSettings.SHOULDER_ANGLE_RANGE;
    private static double shoulderAngleMin = shoulderAngleMax - shoulderAngleRange;
    private NRGArmState armState;
    private boolean wasElbowFullManual;
    private boolean wasShoulderFullManual;

    public NRGArm()
    {
	shoulderJoint = new Jaguar(NRGArmSettings.SHOULDER_MOTOR_CHANNEL);
	elbowJoint = new Victor(NRGArmSettings.ELBOW_MOTOR_CHANNEL);
	shoulderEncoder = new MAE(NRGDriveSettings.GYRO_SLOT, NRGArmSettings.SHOULDER_MAE_CHANNEL, false);
	elbowEncoder = new MAE(NRGDriveSettings.GYRO_SLOT, NRGArmSettings.ELBOW_MAE_CHANNEL, true);
	shoulderPID = new NRGPID(NRGArmSettings.SHOULDER_P, NRGArmSettings.SHOULDER_I, NRGArmSettings.SHOULDER_D, shoulderEncoder, shoulderJoint, NRGArmSettings.SHOULDER_I_CLAMP, true);
	shoulderPID.setOutputRange(-1.0, 0.5);
	shoulderPID.setInputRange(shoulderAngleMin, shoulderAngleMax);
	elbowPID = new NRGPID(NRGArmSettings.ELBOW_P, NRGArmSettings.ELBOW_I, NRGArmSettings.ELBOW_D, elbowEncoder, elbowJoint, NRGArmSettings.ELBOW_I_CLAMP, false);
	elbowPID.setInputRange(elbowAngleMin, elbowAngleMax);
	armState = null;
	initArm();
    }

    public void initArm()
    {
	setElbowAngleSetpoint(getElbowMAE());
	setShoulderAngleSetpoint(getShoulderMAE());
	wasElbowFullManual = false;
	wasShoulderFullManual = false;
	state = NRGArmSettings.MANUAL_CONTROL;
	potentiometerState = 0;
    }

    public double getElbowMAE()
    {
	return elbowEncoder.getAngle();
    }

    public double getShoulderMAE()
    {
	return shoulderEncoder.getAngle();
    }

    /**
     * Decides whether or not you want to change the PID values with the potentiometers
     * Then sets the PID values for the elbow or shoulder.
     */
    public void updatePIDValues()
    {
	int newState = NRGJoystickInput.getPotentiometerState();
	if (newState >= 0)
	{
	    potentiometerState = newState;
	}
	switch (potentiometerState)
	{
	    case NRGArmSettings.ELBOW_POTENTIOMETER_PID:
		setElbowPID();
		break;
	    case NRGArmSettings.SHOULDER_POTENTIOMETER_PID:
		setShoulderPID();
		break;
	    default:
	    // Don't Change the PID from the potentiometer
	}
    }

    public boolean getManualControl()
    {
	return USING_PID_MANUAL;
    }

    public void setManualControl(boolean pidManual)
    {
	USING_PID_MANUAL = pidManual;
    }

    public void updateArmStateFromJoystick()
    {
	int newState = NRGJoystickInput.getArmState();
	if (newState > 0 && newState != state)
	{
	    state = newState;
	    armState = new NRGArmState(state, elbowPID, shoulderPID);
	}
	if (NRGJoystickInput.setManualType())
	    setManualControl(!getManualControl());
    }

    public void setArmMaxima()
    {
	elbowAngleMax = elbowEncoder.getAngle();
	shoulderAngleMax = shoulderEncoder.getAngle();
	elbowAngleMin = elbowAngleMax - elbowAngleRange;
	shoulderAngleMin = shoulderAngleMax - shoulderAngleRange;
    }

    public void update2()
    {
	if (state == NRGArmSettings.MANUAL_CONTROL)
	{
	    /*if (!USING_PID_MANUAL)
	    {*/
	    updateMotorSpeedFromJoystick();
	    /*}
	    else
	    {
	    updateSetpointFromJoystick();
	    }*/
	}
	else
	{
	    goToSetpoint();
	}
    }

    /**
     * THE UPDATE METHOD!1!
     * It switches the state, and carries out one of the methods used to get the arm to a certain
     * preset setpoint. If going from two setpoints that are far away, such as from the ground to the
     * highest peg, it also goes to the stored position before going to its final position. The default
     * is manual control from direct joystick update.
     */
    public void update()
    {
	switch (state)
	{
	    case NRGArmSettings.STORED:
		storeArm();
		break;
	    case NRGArmSettings.PICK_UP_GROUND:
		pickUpGround();
		break;
	    case NRGArmSettings.PICK_UP_FEEDER:
		pickUpFeeder();
		break;

	    case NRGArmSettings.SEEKING_SIDE_LOW:
		seekSideLowPreset();
		break;
	    case NRGArmSettings.SEEKING_SIDE_MID:
		seekSideMidPreset();
		break;
	    case NRGArmSettings.SEEKING_SIDE_HIGH:
		seekSideHighPreset();
		break;
	    case NRGArmSettings.SEEKING_LOW:
		seekMidLowPreset();
		break;
	    case NRGArmSettings.SEEKING_MID:
		seekMidMidPreset();
		break;
	    case NRGArmSettings.SEEKING_HIGH:
		seekMidHighPreset();
		break;
	    case NRGArmSettings.MANUAL_CONTROL:
		/*if (USING_PID_MANUAL)
		{
		updateSetpointFromJoystick();
		}
		else
		{*/
		updateMotorSpeedFromJoystick();
		//}
		break;
	    default:
		Debug.println(true, "There is no arm state");
		break;
	}
	Debug.printRound(Debug.ELBOW, "Elbow: Err: %0 Res: %1 Set: %2 Ang: %3", new double[]
		{
		    elbowPID.getError(), elbowPID.getResult(), elbowPID.getAngleSetpoint(), elbowEncoder.getAngle()
		});
	Debug.printRoundln(Debug.SHOULDER, "Shoulder: Err: %0 Res: %1 Set: %2 Ang: %3", new double[]
		{
		    shoulderPID.getError(), shoulderPID.getResult(), shoulderPID.getAngleSetpoint(), shoulderEncoder.getAngle()
		});
	if (shoulderAtSetpoint())
	{
	    Debug.println(Debug.SHOULDER, "Shoulder at setpoint");
	}
	if (elbowAtSetpoint())
	{
	    Debug.println(Debug.ELBOW, "Elbow at setpoint");
	}
	if (NRGJoystickInput.setArmMax())
	{
	    setArmMaxima();
	}
    }

    public void setArmState(int state)
    {
	this.state = state;
	this.armState = new NRGArmState(state, elbowPID, shoulderPID);

    }

    public static double shoulderSetpointProportionToAngle(double proportion)
    {
	double angle = proportion;
	angle *= shoulderAngleRange;
	angle += shoulderAngleMin;
	return angle;
    }

    public static double elbowSetpointProportionToAngle(double proportion)
    {
	double angle = proportion;
	angle *= elbowAngleRange;
	angle += elbowAngleMin;
	return angle;
    }

    public static double shoulderSetpointAngleToProportion(double angle)
    {
	double proportion = angle;
	proportion -= shoulderAngleMin;
	proportion /= shoulderAngleRange;
	return proportion;
    }

    public static double elbowSetpointAngleToProportion(double angle)
    {
	double proportion = angle;
	proportion -= elbowAngleMin;
	proportion /= elbowAngleRange;
	return proportion;
    }

    /**
     * @return The proportional coefficient for the PID, read from the joystick
     */
    public double potentiometerToP()
    {
	double potentValue = NRGJoystickInput.getPotentiometerValues()[0];
	potentValue += 1.0;
	potentValue /= 2.0; // 0 to 1
	potentValue /= 10.0; // 0 to 0.1
	return potentValue;
    }

    /**
     * @return The integral coefficient for the PID, read from the joystick
     */
    public double potentiometerToI()
    {
	double potentValue = NRGJoystickInput.getPotentiometerValues()[2];
	potentValue += 1.0;
	potentValue /= 2.0; // 0 to 1
	potentValue /= 10.0; // 0 to 0.1
	return potentValue;
    }

    /**
     * @return The derivative coefficient for the PID, read from the joystick
     */
    public double potentiometerToD()
    {
	double potentValue = NRGJoystickInput.getPotentiometerValues()[1];
	potentValue += 1.0;
	potentValue /= 2.0; // 0 to 1
	potentValue /= 10.0; //0 to 0.1
	return potentValue;
    }

    /**
     * Sets the PID values on the elbowPID
     */
    public void setElbowPID()
    {
	elbowPID.setPID(potentiometerToP(), potentiometerToI(), potentiometerToD());
	Debug.println(Debug.ELBOW_PID, "P: " + MathHelper.round(elbowPID.getP(), 3) + " I: " + MathHelper.round(elbowPID.getI(), 3) + " D: " + MathHelper.round(elbowPID.getD(), 3));
    }

    /**
     * Sets the PID values on the shoulderPID
     */
    public void setShoulderPID()
    {
	shoulderPID.setPID(-potentiometerToP(), -potentiometerToI(), -potentiometerToD());
	Debug.println(Debug.SHOULDER_PID, "P: " + MathHelper.round(shoulderPID.getP(), 3) + " I: " + MathHelper.round(shoulderPID.getI(), 3) + " D: " + MathHelper.round(shoulderPID.getD(), 3));
    }

    /**
     * This method gets joystick y-values and uses them to control the motor speed of
     * the shoulder joint and elbow joint.
     */
    public void updateMotorSpeedFromJoystick()
    {
	// Make shoulder speed negative so pulling back on the stick makes the arm go up.
	double shoulderSpeed = -1 * NRGJoystickInput.getYValue(NRGJoystickSettings.SHOULDER_JOY);
	double elbowSpeed = NRGJoystickInput.getYValue(NRGJoystickSettings.ELBOW_JOY);
	double angleE = getElbowMAE();
	double angleS = getShoulderMAE();
	if (Math.abs(elbowSpeed) > NRGArmSettings.ELBOW_DRIVE_THRESHOLD)
	{
	    elbowPID.disable();
	    //Debug.printRoundln(Debug.ELBOW, "elbow val=%0", new double[] { elbowSpeed } );
	    if (angleE <= elbowAngleMin + 6)
		elbowSpeed = MathHelper.clamp(elbowSpeed, 0.0, 1.0);
	    else if (angleE >= elbowAngleMax - 4)
		elbowSpeed = MathHelper.clamp(elbowSpeed, -1.0, 0.0);
	    elbowJoint.set(elbowSpeed);
	    wasElbowFullManual = true;
	}
	else
	{
	    if(wasElbowFullManual)
	    {
		setElbowAngleSetpoint(angleE);
	    }
	    wasElbowFullManual = false;
	    elbowPID.enable();
	}
	if (Math.abs(shoulderSpeed) > NRGArmSettings.SHOULDER_DRIVE_THRESHOLD)
	{
	    shoulderPID.disable();
	    //Debug.printRoundln(Debug.SHOULDER, "shoulder val=%0", new double[] { shoulderSpeed } );
	    if (angleS <= shoulderAngleMin + 6)
		shoulderSpeed = MathHelper.clamp(shoulderSpeed, -1.0, 0.0);
	    else if (angleS >= shoulderAngleMax - 4)
		shoulderSpeed = MathHelper.clamp(shoulderSpeed, 0.0, 1.0);
	    shoulderJoint.set(shoulderSpeed);
	    wasShoulderFullManual = true;
	}
	else
	{
	    if(wasShoulderFullManual)
	    {
		setShoulderAngleSetpoint(angleS);
	    }
	    wasShoulderFullManual = false;
	    shoulderPID.enable();
	}


    }

    public void updateSetpointFromJoystick()
    {
	double currentElbowAngle = getElbowMAE();
	double currentShoulderAngle = getShoulderMAE();
	double elbowModifier = NRGJoystickInput.getYValue(NRGJoystickSettings.ELBOW_JOY) * NRGArmSettings.ELBOW_UPDATE_SCALE_FACTOR;
	double shoulderModifier = -NRGJoystickInput.getYValue(NRGJoystickSettings.SHOULDER_JOY) * NRGArmSettings.SHOULDER_UPDATE_SCALE_FACTOR;
	setElbowAngleSetpoint(currentElbowAngle + elbowModifier);
	enableElbow();
	setShoulderAngleSetpoint(currentShoulderAngle + shoulderModifier);
	enableShoulder();
    }

    /**
     * @param proportion The proportion to set the elbow setpoint as in the elbowPID.
     */
    public void setElbowProportionSetpoint(double proportion)
    {
	setElbowAngleSetpoint(elbowSetpointProportionToAngle(proportion));
    }

    /**
     * @param proportion The proportion to set the shoulder setpoint as in the shoulderPID.
     */
    public void setShoulderProportionSetpoint(double proportion)
    {
	setShoulderAngleSetpoint(shoulderSetpointProportionToAngle(proportion));
    }

    public void setElbowAngleSetpoint(double angle)
    {
	elbowPID.setAngleSetpoint(MathHelper.clamp(angle, elbowAngleMin + 4, elbowAngleMax - 4));
    }

    public void setShoulderAngleSetpoint(double angle)
    {
	shoulderPID.setAngleSetpoint(MathHelper.clamp(angle, shoulderAngleMin + 6, shoulderAngleMax - 4));
    }

    /**
     * Enables the elbow's PID task
     */
    public void enableElbow()
    {
	if (USING_ELBOW)
	    elbowPID.enable();
    }

    /**
     * Enables the shoulder's PID task
     */
    public void enableShoulder()
    {
	if (USING_SHOULDER)
	    shoulderPID.enable();
    }

    /**
     * @return Whether or not the elbow has reached it's target setpoint
     */
    public boolean elbowAtSetpoint()
    {
	return elbowPID.onTarget();
    }

    /**
     * @return Whether or not the shoulder has reached it's target setpoint
     */
    public boolean shoulderAtSetpoint()
    {
	return shoulderPID.onTarget();
    }

    /**
     * Disables the shoulder's PID task
     */
    public void disableShoulder()
    {
	shoulderPID.disable();
    }

    /**
     * Disables the elbow's PID task
     */
    public void disableElbow()
    {
	elbowPID.disable();
    }

    public void goToSetpoint()
    {
	setElbowProportionSetpoint(armState.getElbowSetpoint());
	setShoulderProportionSetpoint(armState.getShoulderSetpoint());
    }

    /**
     * Sets the elbow and shoulder setpoints to the stored position and enables the PID tasks in case they weren't
     */
    public void storeArm()
    {
	Debug.println(Debug.ELBOW_PID, "Storing the Arm");
	setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	setShoulderProportionSetpoint(NRGArmSettings.STORED_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to picking up off of the ground and enables the PID tasks in case they weren't
     */
    public void pickUpGround()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow picking up from ground");
	    setElbowProportionSetpoint(NRGArmSettings.PICK_UP_GROUND_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder picking up from ground.");
	setShoulderProportionSetpoint(NRGArmSettings.PICK_UP_GROUND_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to picking up from the feeder and enables the PID tasks in case they weren't
     */
    public void pickUpFeeder()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow picking up from feeder");
	    setElbowProportionSetpoint(NRGArmSettings.PICK_UP_FEEDER_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder picking up from feeder");
	setShoulderProportionSetpoint(NRGArmSettings.PICK_UP_FEEDER_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to side, high peg and enables the PID tasks in case they weren't
     */
    public void seekSideHighPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking side high setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_SIDE_HIGH_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking side high setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_SIDE_HIGH_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to side, middle peg and enables the PID tasks in case they weren't
     */
    public void seekSideMidPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking side mid setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_SIDE_MID_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking side mid setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_SIDE_MID_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to side, low peg and enables the PID tasks in case they weren't
     */
    public void seekSideLowPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking side low setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_SIDE_LOW_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking side low setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_SIDE_LOW_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to middle, high peg and enables the PID tasks in case they weren't
     */
    public void seekMidHighPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking center high setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_HIGH_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking center high setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_HIGH_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to middle, middle peg and enables the PID tasks in case they weren't
     */
    public void seekMidMidPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking center mid setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_MID_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking center mid setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_MID_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /**
     * Sets the elbow and shoulder setpoints to middle, low peg and enables the PID tasks in case they weren't
     */
    public void seekMidLowPreset()
    {

	if (Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	{
	    Debug.println(Debug.ELBOW_PID, "Elbow seeking center low setpoint");
	    setElbowProportionSetpoint(NRGArmSettings.SEEKING_MID_ELBOW_SETPOINT);
	}
	else
	{
	    Debug.println(Debug.ELBOW_PID, "Storing Elbow");
	    setElbowProportionSetpoint(NRGArmSettings.STORED_ELBOW_SETPOINT);
	}
	Debug.print(Debug.SHOULDER_PID, " Shoulder seeking center low setpoint.");
	setShoulderProportionSetpoint(NRGArmSettings.SEEKING_LOW_SHOULDER_SETPOINT);
	enableElbow();
	enableShoulder();
    }

    /*public boolean illegalConfiguration()
    {
    double theta = getShoulderMAE();
    double phi = getElbowMAE();
    double x1 = NRGArmSettings.SHOULDER_LENGTH*Math.sin((theta * Math.PI)/180.0);
    double x2 = NRGArmSettings.ELBOW_LENGTH*Math.sin((phi * Math.PI)/180.0);
    return (x1 + x2) > 60.0;
    }*/
}
