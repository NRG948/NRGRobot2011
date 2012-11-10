package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author sand
 */
public class NRGJoystickSettings
{
    //Joysticks, DRIVE_JOY is the joystick that we track x, y, and z to pass into the drive.
    public final static int DRIVE_JOY = 3;
    public final static int ELBOW_JOY = 2;
    public final static int SHOULDER_JOY = 1;
    public static final int GRIPPER_JOY = 1;
    public static final double DEAD_ZONE_THRESHOLD = 0.05;
    //StraightLineBtns
    public static final Joystick STRAIGHT_JOY = NRGJoystickInput.JOYSTICK3;
    public static final int STRAIGHT_Z_BTN = 3;
    public static final int STRAIGHT_X_BTN = 2;
    public static final int STRAIGHT_Y_BTN = 1;
    //Gyro reset
    public static final Joystick GYRO_RESET_JOY = NRGJoystickInput.JOYSTICK3;
    public static final int GYRO_RESET_BTN = 7;
    //Gyro rotate
    public static final Joystick GYRO_ROTATE_JOY = NRGJoystickInput.JOYSTICK3;
    public static final int GYRO_ROTATE_BTN = 8;
    // Arm controls
    public static final int SET_ARM_MAX = 9;
    public static final int POTENTIOMETER_OFF = 10;
    public static final int PID_ELBOW_POTENTIOMETER_BUTTON = 11;
    public static final int PID_SHOULDER_POTENTIOMETER_BUTTON = 12;
    // DRIVE JOY
    public static final int WALL_TRACK_FORWARD_LEFT = 5;
    public static final int WALL_TRACK_FORWARD_RIGHT = 6;
    public static final int WALL_TRACK_BACKWARD_LEFT = 3;
    public static final int WALL_TRACK_BACKWARD_RIGHT = 4;
    // JOY 1
    public static final int ENABLE_MANUAL_SHOULDER = 1;
    public static final int PICK_UP_GROUND = 2;
    public static final int STORE = 3;
    public static final int PICK_UP_FEEDER = 5;
    public static final int SET_MANUAL_TYPE = 4;
    public static final int SIDE_HIGH_PRESET_S = 6;
    public static final int SIDE_MID_PRESET_S = 7;
    public static final int SIDE_LOW_PRESET_S = 8;
    public static final int MID_LOW_PRESET_S = 9;
    public static final int MID_MID_PRESET_S = 10;
    public static final int MID_HIGH_PRESET_S = 11;
    //JOY 2
    public static final int ENABLE_MANUAL_ELBOW = 1;
    public static final int ROTATE_GRIPPER_UP = 2;
    public static final int ROTATE_GRIPPER_DOWN = 3;
    public static final int ROTATE_GRIPPER_IN = 4;
    public static final int ROTATE_GRIPPER_OUT = 5;
    public static final int MID_HIGH_PRESET_E = 6;
    public static final int MID_MID_PRESET_E = 7;
    public static final int MID_LOW_PRESET_E = 8;
    public static final int SIDE_LOW_PRESET_E = 9;
    public static final int SIDE_MID_PRESET_E = 10;
    public static final int SIDE_HIGH_PRESET_E = 11;
}
