package nrg.minibot;

/**
 * This class basically runs the servo(lock and set angle) and the minibot goes! Yay!
 * @author Matthew
 */
import edu.wpi.first.wpilibj.*;

public class NRGMINIServo
{
    private Servo miniServo;

    public NRGMINIServo()
    {
	miniServo = new Servo(NRGMINISettings.SERVO_SLOT, NRGMINISettings.SERVO_CHANNEL);
    }

    public void reset()
    {
	miniServo.setAngle(0);
    }

    public boolean run()
    {
	if (miniServo.getAngle() < 90)
	{
	    miniServo.setAngle(miniServo.getAngle() + NRGMINISettings.SERVO_ADJUST);
	    return true;
	}
	else
	    return false;
    }

    public double getAngle()
    {
	return miniServo.getAngle();
    }
}
