package nrg;

/**
 * @author Stephen and Matthew
 */
public class NRGArmSettings
{
    public static final int SHOULDER_MOTOR_CHANNEL = 5;
    public static final int ELBOW_MOTOR_CHANNEL = 6;
    public static final int SHOULDER_MAE_CHANNEL = 3;
    public static final int ELBOW_MAE_CHANNEL = 4;
    public static final double SHOULDER_DRIVE_THRESHOLD = 0.1;
    public static final double ELBOW_DRIVE_THRESHOLD = 0.1;
    public static final double SHOULDER_ANGLE_MAX = 190.0; //158.0;
    public static final double SHOULDER_ANGLE_RANGE = 140.0;
    public static final double SHOULDER_ANGLE_MIN = SHOULDER_ANGLE_MAX - SHOULDER_ANGLE_RANGE; //8.0;

    
    public static final double ELBOW_ANGLE_MIN = 45.0;
    public static final double ELBOW_ANGLE_MAX = 336.0;
    public static final double ELBOW_ANGLE_RANGE = 285.0;

    //TODO: Calibrate these
    public static double SHOULDER_P = -0.07;// SHOULDER VALUES ALL MUST BE NEGATIVE
    public static double SHOULDER_I = -0.009;
    public static double SHOULDER_D = -0.009;

    public static double ELBOW_P = 0.04;
    public static double ELBOW_I = 0.002;
    public static double ELBOW_D = 0.0;

    public static final int POTENTIOMETER_OFF = 0;
    public static final int ELBOW_POTENTIOMETER = 1;
    public static final int SHOULDER_POTENTIOMETER = 2;
    public static final int ELBOW_POTENTIOMETER_PID = 3;
    public static final int SHOULDER_POTENTIOMETER_PID = 4;
    /****************************/
    /********Arm States**********/
    /****************************/
    public static final int STORED = 1;
    public static final int PICK_UP_GROUND = 2;
    public static final int PICK_UP_FEEDER = 3;
    public static final int MANUAL_CONTROL = 4;
    public static final int SEEKING_SIDE_LOW = 5;
    public static final int SEEKING_SIDE_MID = 6;
    public static final int SEEKING_SIDE_HIGH = 7;
    public static final int SEEKING_LOW = 8;
    public static final int SEEKING_MID = 9;
    public static final int SEEKING_HIGH = 10;
    /****************************/
    /********Arm Heights*********/
    /****************************/
    public static final double RETRACTED_ELBOW_SETPOINT = 0.9;
    public static final double STORED_ELBOW_SETPOINT = 0.9785;
    public static final double STORED_SHOULDER_SETPOINT = 0.05;
    public static final double PICK_UP_GROUND_ELBOW_SETPOINT = 0.5859;
    public static final double PICK_UP_GROUND_SHOULDER_SETPOINT = 0.1417;
    public static final double PICK_UP_FEEDER_ELBOW_SETPOINT = 0.8267;
    public static final double PICK_UP_FEEDER_SHOULDER_SETPOINT = 0.1235;
    public static final double SEEKING_SIDE_LOW_ELBOW_SETPOINT = 0.7908;
    public static final double SEEKING_SIDE_LOW_SHOULDER_SETPOINT = 0.0394;
    public static final double SEEKING_SIDE_MID_ELBOW_SETPOINT = 0.7853;
    public static final double SEEKING_SIDE_MID_SHOULDER_SETPOINT = 0.3628;
    public static final double SEEKING_SIDE_HIGH_ELBOW_SETPOINT = 0.6423;
    public static final double SEEKING_SIDE_HIGH_SHOULDER_SETPOINT = 0.6983 + .0288;
    public static final double SEEKING_LOW_ELBOW_SETPOINT = 0.814;
    public static final double SEEKING_LOW_SHOULDER_SETPOINT = 0.057;
    public static final double SEEKING_MID_ELBOW_SETPOINT = 0.7983;
    public static final double SEEKING_MID_SHOULDER_SETPOINT = 0.4044;
    public static final double SEEKING_HIGH_ELBOW_SETPOINT = 0.6868;
    public static final double SEEKING_HIGH_SHOULDER_SETPOINT = 0.7347 + .0288;
    /****************************/
    // chopped off three inches
    public static final double SHOULDER_LENGTH = 45.5 - 3.0;
    public static final double ELBOW_LENGTH = 32.4 - 3.0;
    public static final double SHOULDER_UPDATE_SCALE_FACTOR = 20.0;
    public static final double ELBOW_UPDATE_SCALE_FACTOR = 20.0;
    public static final double SHOULDER_MIN_ANGLE_ERROR = 7.0;
    public static final double SHOULDER_I_CLAMP = 0.6;
    public static final double ELBOW_I_CLAMP = 0.3;
    public static final double ELBOW_MIN_PERCENT_ERROR = 0.01;
    public static final int STORE_ELBOW = 0;
    public static final int MOVE_SHOULDER = 1;
    public static final int MOVE_ELBOW = 2;
}
