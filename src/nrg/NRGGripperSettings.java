package nrg;

/**
 * This class has something to do with that other class.
 * @author Irving
 */
public class NRGGripperSettings
{
    public static final int MOTOR_SLOT = 4;
    public static final int TOP_MOTOR_CHANNEL = 8;
    public static final int BOTTOM_MOTOR_CHANNEL = 9;
    // TODO: set motor speeds of gripper
    public static final double POSSESS_MOTOR_SPEED = 0.5;
    public static final double REPEL_MOTOR_SPEED = 0.5;
    public static final double ROTATE_MOTOR_SPEED = 0.25;
    // TODO: set these multipliers to be correct direction to make motor be rotating out (i.e. repelling)
    public static final int TOP_MOTOR_OUT_MULTIPLER = 1;
    public static final int BOTTOM_MOTOR_OUT_MULTIPLIER = 1;
}
