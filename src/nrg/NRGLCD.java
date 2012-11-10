package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * NRGLCD contains all the println methods that were poorly implemented by DriverStationLCD
 * They print string commands to the LCD screen on the Classmate
 * @author LinE
 */
public class NRGLCD
{
    private final static DriverStationLCD LCD = DriverStationLCD.getInstance();

    /**
     * This method clears the screen DERP
     */
    public static void clear()
    {
	String empty = "";
	for (byte a = 0; a < DriverStationLCD.kLineLength; a++)
	{
	    empty += " ";
	}
	println1(empty);
	println2(empty);
	println3(empty);
	println4(empty);
	println5(empty);
	println6(empty);
    }

    /**
     * These methods printlnX prints to line X on the driver station.
     * @param s is what you want to print.
     */
    public static void println1(String s)
    {
	LCD.println(DriverStationLCD.Line.kMain6, 1, s);
    }

    public static void println2(String s)
    {
	LCD.println(DriverStationLCD.Line.kUser2, 1, s);
    }

    public static void println3(String s)
    {
	LCD.println(DriverStationLCD.Line.kUser3, 1, s);
    }

    public static void println4(String s)
    {
	LCD.println(DriverStationLCD.Line.kUser4, 1, s);
    }

    public static void println5(String s)
    {
	LCD.println(DriverStationLCD.Line.kUser5, 1, s);
    }

    public static void println6(String s)
    {
	LCD.println(DriverStationLCD.Line.kUser6, 1, s);
    }

    /**
     * This method updates the screen,
     * without calling this function the screen will not print the strings
     * sent to the println methods
     */
    public static void update()
    {
	LCD.updateLCD();
    }
}
