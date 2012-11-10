package nrg.camera;

import edu.wpi.first.wpilibj.camera.AxisCamera.ResolutionT;

/**
 * @author Brian, Dustin
 */
public class CameraSettings
{
    public static final boolean INIT_CAMERA = false;
    //Camera Calibration
    public static final ResolutionT CAMERA_RESOLUTION = ResolutionT.k160x120;
    public static final int CAMERA_FRAME_RATE = 25;
    public static final int CAMERA_BRIGHTNESS = 0;
    //Servos
    public static final int VERT_SERVO_SLOT = 4;
    public static final int VERT_SERVO_CHANNEL = 9;
    public static final double VERT_SERVO_ANGLE = 0.5;
    public static final double VERT_SERVO_ERROR_ADJUST = 8.0 / 180;
    public static final int HORIZ_SERVO_SLOT = 4;
    public static final int HORIZ_SERVO_CHANNEL = 10;
    public static final double HORIZ_SERVO_ANGLE = 0.5;
    public static final double HORIZ_SERVO_ERROR_ADJUST = 8.0 / 180;
    public static final double SERVO_ANGLE_MIN = 0.0;
    public static final double SERVO_ANGLE_MAX = 1.0;
}
