package nrg;

/**
 * @author Stephen and Dustin
 */
public class NRGDriveSettings
{
    public static final int MOTOR_SLOT = 4;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 1;
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 2;
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 3;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 4;
    //Gyro stuff.
    public static final int GYRO_SLOT = 1;
    public static final int GYRO_DATA_CHANNEL = 1;
    public static final int GYRO_TEMP_CHANNEL = 2;
    //Offset 180 since starts facing driver.
    public static final int GYRO_OFFSET = 180;
    // Volts per degree per second
    // TODO: CALIBRATE FOR THIS YEAR
    // TODO: compensate gyro readings for temperature changes
    public static final double GYRO_SENSITIVITY = 0.007022; //0.007326667// or .007 * 376.8 / 360;// roughly at 70ish degrees
    // Digital channels for the three line sensor inputs
    public static final int LINE_SENSOR_RIGHT_CHANNEL = 3;
    public static final int LINE_SENSOR_MIDDLE_CHANNEL = 4;
    public static final int LINE_SENSOR_LEFT_CHANNEL = 5;
    // Increase value for smoothness and delay
    public static final int INPUT_AVERAGER_ARRAY_LENGTH = 4;
    // Threshold for using the input averager
    public static final double THRESHOLD = .1;
    // Values for twist correction
    public static final double MAX_TWIST = 0.5;
    public static final int LIMIT_SWITCH_CHANNEL = 13;
    public static final double TWIST_SCALING = 40.0;
    public static final int NO_INPUT = 0;
    public static final int DRIVE_CHANGES = 1;
}
