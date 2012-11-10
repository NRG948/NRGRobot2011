package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author Stephen and Dustin
 */
public class NRGSensors
{
    public static DigitalInput lineSensorRight;
    public static DigitalInput lineSensorMiddle;
    public static DigitalInput lineSensorLeft;
    public static NRGGyro gyro;
    public static DigitalInput gyroTemp;
    public static IRSensor leftIRSensor;
    public static IRSensor rightIRSensor;
    public static NRGUltrasonic ultrasonic;
    public static NRGMaxSonar sonar;
    private static DigitalInput limitSwitch;

    public static void initSensors()
    {
	initGyro();
	lineSensorLeft = new DigitalInput(NRGDriveSettings.LINE_SENSOR_LEFT_CHANNEL);
	lineSensorMiddle = new DigitalInput(NRGDriveSettings.LINE_SENSOR_MIDDLE_CHANNEL);
	lineSensorRight = new DigitalInput(NRGDriveSettings.LINE_SENSOR_RIGHT_CHANNEL);
	leftIRSensor = new IRSensor(NRGIRSettings.IR_LEFT_CHANNEL);
	rightIRSensor = new IRSensor(NRGIRSettings.IR_RIGHT_CHANNEL);
	ultrasonic = new NRGUltrasonic();
	sonar = new NRGMaxSonar(NRGMaxSonar.SONAR_CHANNEL);
	limitSwitch = new DigitalInput(NRGDriveSettings.LIMIT_SWITCH_CHANNEL);
    }

    protected static void initGyro()
    {
	try
	{
	    gyro = new NRGGyro(NRGDriveSettings.GYRO_OFFSET);
	    //gyroTemp = new AnalogChannel(NRGDriveSettings.GYRO_SLOT, NRGDriveSettings.GYRO_TEMP_CHANNEL);
	    gyro.setSensitivity(NRGDriveSettings.GYRO_SENSITIVITY);
	    gyro.reset();
	}
	catch (Exception e)
	{
	    Debug.println(Debug.FATAL_EXCEPTIONS, "Gyro Error: " + e.getMessage());
	}
    }

    public static void update()
    {
	if (NRGJoystickInput.getGyroReset())
	    gyro.setGyroStandard();
	if (NRGJoystickInput.getGyroReverse())
	    gyro.setGyroReverse();
    }

    public static boolean senseTube()
    {
	return !limitSwitch.get();
    }
}
