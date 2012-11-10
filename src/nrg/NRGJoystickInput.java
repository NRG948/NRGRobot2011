package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * Represents input from the joysticks. Has general helper methods to get
 * specified values from any joystick, then uses specific ports and buttons or
 * axes from NRGJoystickSettings to get specific values from the joysticks.
 * @author Austin, Brian, Matthew, Eric
 */
public class NRGJoystickInput
{
    final static Joystick JOYSTICK1 = new Joystick(1);
    final static Joystick JOYSTICK2 = new Joystick(2);
    final static Joystick JOYSTICK3 = new Joystick(3);
    final static Joystick JOYSTICK4 = new Joystick(4);
    private static boolean setManualWasPressed = false;
    
    private static Joystick getJoystickAtPort(int port)
    {
	switch (port)
	{
	    case 1:
		return JOYSTICK1;
	    case 2:
		return JOYSTICK2;
	    case 3:
		return JOYSTICK3;
	    case 4:
		return JOYSTICK4;
	    default:
		throw new IllegalArgumentException("Joystick port is not valid.");
	}
    }

    private static boolean getButtonState(int port, int button)
    {
	return getJoystickAtPort(port).getRawButton(button);
    }

    private static boolean getTriggerState(int port)
    {
	return getJoystickAtPort(port).getTrigger();
    }

    private static double getXValue(int port)
    {
	return getJoystickAtPort(port).getX();
    }

    public static double getYValue(int port)
    {
	return getJoystickAtPort(port).getY();
    }

    private static double getZValue(int port)
    {
	return getJoystickAtPort(port).getZ();
    }

    private static double getTwistValue(int port)
    {
	return getJoystickAtPort(port).getTwist();
    }

    private static double getMagnitude(int port)
    {
	return getJoystickAtPort(port).getMagnitude();
    }

    private static double getDirection(int port)
    {
	return getJoystickAtPort(port).getDirectionDegrees();
    }

    private static double getThrottleValue(int port)
    {
	return getJoystickAtPort(port).getThrottle();
    }

    public static double getDriveX()
    {
	double xValue = getXValue(NRGJoystickSettings.DRIVE_JOY);
	if (getStraightY() || Math.abs(xValue) <= NRGJoystickSettings.DEAD_ZONE_THRESHOLD)
	{
	    xValue = 0.0;
	}
	return xValue;

    }

    public static double getDriveY()
    {
	double yValue = getYValue(NRGJoystickSettings.DRIVE_JOY);
	if (getStraightX() || Math.abs(yValue) <= NRGJoystickSettings.DEAD_ZONE_THRESHOLD)
	{
	    yValue = 0.0;
	}
	return yValue;
    }

    public static double getDriveTwist()
    {
	double twistValue = getTwistValue(NRGJoystickSettings.DRIVE_JOY);
	if (Math.abs(twistValue) <= NRGJoystickSettings.DEAD_ZONE_THRESHOLD)
	{
	    twistValue = 0.0;
	}
	return twistValue * Math.abs(twistValue);
    }

    public static double getDriveMagnitude()
    {
	double magValue = getMagnitude(NRGJoystickSettings.DRIVE_JOY);
	if (Math.abs(magValue) <= NRGJoystickSettings.DEAD_ZONE_THRESHOLD)
	    magValue = 0.0;
	return magValue;
    }

    public static double getDriveDirection()
    {
	double dirValue = getDirection(NRGJoystickSettings.DRIVE_JOY);
	return dirValue;
    }

    public static boolean getDigitalValue(Joystick joy, int button)
    {
	return joy.getRawButton(button);
    }

    public static boolean getStraightX()
    {
	return getDigitalValue(NRGJoystickSettings.STRAIGHT_JOY, NRGJoystickSettings.STRAIGHT_X_BTN);
    }

    public static boolean getStraightY()
    {
	return getDigitalValue(NRGJoystickSettings.STRAIGHT_JOY, NRGJoystickSettings.STRAIGHT_Y_BTN);
    }

    public static boolean getStraightZ()
    {
	return getDigitalValue(NRGJoystickSettings.STRAIGHT_JOY, NRGJoystickSettings.STRAIGHT_Z_BTN);
    }

    public static boolean getGyroReset()
    {
	return getDigitalValue(NRGJoystickSettings.GYRO_RESET_JOY, NRGJoystickSettings.GYRO_RESET_BTN);
    }

    public static boolean getGyroReverse()
    {
	return getDigitalValue(NRGJoystickSettings.GYRO_ROTATE_JOY, NRGJoystickSettings.GYRO_ROTATE_BTN);
    }

    public static double[] getPotentiometerValues()
    {
	double[] values = new double[3];

	values[0] = getThrottleValue(NRGJoystickSettings.DRIVE_JOY);
	values[1] = getZValue(NRGJoystickSettings.ELBOW_JOY);
	values[2] = getZValue(NRGJoystickSettings.SHOULDER_JOY);

	return values;
    }

    public static int getGripperButton()
    {
	if (getGripperOut())
	    return NRGGripper.REPEL;

	if (getGripperIn())
	    return NRGGripper.POSSESS;

	if (getGripperUp())
	    return NRGGripper.ROTATE_UP;

	if (getGripperDown())
	    return NRGGripper.ROTATE_DOWN;

	return -1;
    }

    public static int getPotentiometerState()
    {
	if (getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.POTENTIOMETER_OFF))
	    return NRGArmSettings.POTENTIOMETER_OFF;
	if (getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.PID_ELBOW_POTENTIOMETER_BUTTON))
	    return NRGArmSettings.ELBOW_POTENTIOMETER_PID;
	if (getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.PID_SHOULDER_POTENTIOMETER_BUTTON))
	    return NRGArmSettings.SHOULDER_POTENTIOMETER_PID;
	return -1;
    }

    /**
     * Gets the value of the first button being pressed (checking from 1 to 12)
     * For testing purposes
     * @param joy the joystick
     * @return the value of the first button detected, 0 if none are pressed
     */
    public static int getFirstButtonState(Joystick joy)
    {
	for (int i = 1; i <= 12; i++)
	{
	    if (joy.getRawButton(i))
		return i;
	}
	return 0;
    }

    public static boolean getElbowManualEnabled()
    {
	return getDigitalValue(JOYSTICK2, NRGJoystickSettings.ENABLE_MANUAL_ELBOW);
    }

    public static boolean getShoulderManualEnabled()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.ENABLE_MANUAL_SHOULDER);
    }

    public static boolean getGripperDown()
    {
	return getDigitalValue(JOYSTICK2, NRGJoystickSettings.ROTATE_GRIPPER_DOWN);
    }

    public static boolean getGripperUp()
    {
	return getDigitalValue(JOYSTICK2, NRGJoystickSettings.ROTATE_GRIPPER_UP);
    }

    public static boolean getGripperOut()
    {
	return getDigitalValue(JOYSTICK2, NRGJoystickSettings.ROTATE_GRIPPER_OUT);
    }

    public static boolean getGripperIn()
    {
	return getDigitalValue(JOYSTICK2, NRGJoystickSettings.ROTATE_GRIPPER_IN);
    }

    public static boolean getSeekingSideHighPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.SIDE_HIGH_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.SIDE_HIGH_PRESET_E);
    }

    public static boolean getSeekingSideMidPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.SIDE_MID_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.SIDE_MID_PRESET_E);
    }

    public static boolean getSeekingSideLowPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.SIDE_LOW_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.SIDE_LOW_PRESET_E);
    }

    public static boolean getSeekingMidHighPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.MID_HIGH_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.MID_HIGH_PRESET_E);
    }

    public static boolean getSeekingMidMidPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.MID_MID_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.MID_MID_PRESET_E);
    }

    public static boolean getSeekingMidLowPreset()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.MID_LOW_PRESET_S) || getDigitalValue(JOYSTICK2, NRGJoystickSettings.MID_LOW_PRESET_E);
    }

    public static boolean getPickUpGround()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.PICK_UP_GROUND);
    }

    public static boolean getStoreArm()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.STORE);
    }

    public static boolean getPickUpFeeder()
    {
	return getDigitalValue(JOYSTICK1, NRGJoystickSettings.PICK_UP_FEEDER);
    }

    public static int getArmState()
    {
	if (getElbowManualEnabled() || getShoulderManualEnabled())
	    return NRGArmSettings.MANUAL_CONTROL;
	if (getSeekingSideHighPreset())
	    return NRGArmSettings.SEEKING_SIDE_HIGH;
	if (getSeekingSideMidPreset())
	    return NRGArmSettings.SEEKING_SIDE_MID;
	if (getSeekingSideLowPreset())
	    return NRGArmSettings.SEEKING_SIDE_LOW;
	if (getSeekingMidHighPreset())
	    return NRGArmSettings.SEEKING_HIGH;
	if (getSeekingMidMidPreset())
	    return NRGArmSettings.SEEKING_MID;
	if (getSeekingMidLowPreset())
	    return NRGArmSettings.SEEKING_LOW;
	if (getPickUpGround())
	    return NRGArmSettings.PICK_UP_GROUND;
	if (getStoreArm())
	    return NRGArmSettings.STORED;
	if (getPickUpFeeder())
	    return NRGArmSettings.PICK_UP_FEEDER;
	return -1;
    }

    public static boolean getWallTrackForwardLeft()
    {
	return getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.WALL_TRACK_FORWARD_LEFT);
    }

    public static boolean getWallTrackForwardRight()
    {
	return getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.WALL_TRACK_FORWARD_RIGHT);
    }

    public static boolean getWallTrackBackwardLeft()
    {
	return getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.WALL_TRACK_BACKWARD_LEFT);
    }

    public static boolean getWallTrackBackwardRight()
    {
	return getButtonState(NRGJoystickSettings.DRIVE_JOY, NRGJoystickSettings.WALL_TRACK_BACKWARD_RIGHT);
    }

    /*public static boolean getPIDOutput()
    {
    return getDigitalValue(JOYSTICK1, NRGJoystickSettings.PID_OUTPUT);
    }*/
    public static boolean setArmMax()
    {
	return getDigitalValue(JOYSTICK3, NRGJoystickSettings.SET_ARM_MAX);
    }

    public static boolean setManualType()
    {
	//sets wasPressed to previous value
	boolean wasPressed = setManualWasPressed;
	setManualWasPressed = getDigitalValue(JOYSTICK1, NRGJoystickSettings.SET_MANUAL_TYPE);
	//returns boolean
	return (!wasPressed && setManualWasPressed);

    }
}
