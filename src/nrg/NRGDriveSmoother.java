package nrg;

/**
 * @author sand
 */
public class NRGDriveSmoother
{
    public static double smoothX(double currentAverageX, double previousAverageX)
    {
	if (Math.abs(previousAverageX) - Math.abs(currentAverageX) > NRGDriveSettings.THRESHOLD)
	{
	    return currentAverageX;
	}
	else
	{
	    return NRGJoystickInput.getDriveX();
	}
    }

    public static double smoothY(double currentAverageY, double previousAverageY)
    {
	if (Math.abs(previousAverageY) - Math.abs(currentAverageY) > NRGDriveSettings.THRESHOLD)
	{
	    return currentAverageY;
	}
	else
	{
	    return NRGJoystickInput.getDriveY();
	}
    }
}
