package nrg;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 * This class encapsulates the digital and analog inputs and outputs from the
 * Cypress Enhanced IO module used in the custom manipulator driver station
 * and both the driver and manipulator HUDs.
 * ...or so says last year's comment.
 * @author Irving
 */
public class NRGIO
{
    private static final DriverStationEnhancedIO IO = DriverStation.getInstance().getEnhancedIO();

    private static boolean getDigital(int channel)
    {
	boolean isPressed = false;

	try
	{
	    isPressed = IO.getDigital(channel);
	}
	catch (EnhancedIOException ex)
	{
	    NRGLCD.println2(ex.getMessage());
	}

	return isPressed;
    }

    private static void setDigital(int channel, boolean value)
    {
	try
	{
	    IO.setDigitalOutput(channel, value);
	    if (channel <= NRGIOSettings.CYPRESS_BOARD_LED_COUNT)
		IO.setLED(channel, value);
	}
	catch (EnhancedIOException ex)
	{
	    NRGLCD.println2(ex.getMessage());
	}
    }

    public static boolean getTrackLeft()
    {
	return getDigital(NRGIOSettings.DIG_IN_TRACK_LEFT);
    }

    public static boolean getTrackRight()
    {
	return getDigital(NRGIOSettings.DIG_IN_TRACK_RIGHT);

    }

    //True is Cartesian, False is Polar
    public static boolean getDriveType()
    {
	return getDigital(NRGIOSettings.DIG_IN_DRIVE_TYPE);
    }

    public static boolean getMiniBotDeployment()
    {
	return getDigital(NRGIOSettings.DIG_IN_MINIBOT);
    }
}
