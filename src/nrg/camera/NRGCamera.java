package nrg.camera;

import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.*;

import nrg.Debug;

/**
 * @author Brian, Dustin
 */
public class NRGCamera
{
    private static AxisCamera cam;
    /*private Servo horizontalServo;
    private Servo verticalServo;
    private double horizontalServoAngle = CameraSettings.HORIZ_SERVO_ANGLE;
    private static double verticalServoAngle = CameraSettings.VERT_SERVO_ANGLE;*/

    public NRGCamera()
    {
	cam = AxisCamera.getInstance();
	cam.writeResolution(CameraSettings.CAMERA_RESOLUTION);
	cam.writeMaxFPS(CameraSettings.CAMERA_FRAME_RATE);
	//cam.writeBrightness(CameraSettings.CAMERA_BRIGHTNESS);

	/*verticalServo = new Servo(CameraSettings.VERT_SERVO_SLOT, CameraSettings.VERT_SERVO_CHANNEL);
	verticalServo.set(verticalServoAngle);
	horizontalServo = new Servo(CameraSettings.HORIZ_SERVO_SLOT, CameraSettings.HORIZ_SERVO_CHANNEL);
	horizontalServo.set(horizontalServoAngle);*/
    }

    public void update()
    {
    }

    /*public void resetServo()
    {
    horizontalServoAngle = CameraSettings.HORIZ_SERVO_ANGLE;
    verticalServoAngle = CameraSettings.VERT_SERVO_ANGLE;
    verticalServo.set(verticalServoAngle);
    horizontalServo.set(horizontalServoAngle);
    }

    public double getHorizontalServoAngle()
    {
    return horizontalServoAngle;
    }

    public static double getVerticalServoAngle()
    {
    return verticalServoAngle;
    }*/
    public String toString()
    {
	return cam.toString();
    }
}
