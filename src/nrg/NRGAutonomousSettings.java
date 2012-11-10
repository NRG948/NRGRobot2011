package nrg;

/**
 * @author Stephen
 */
public class NRGAutonomousSettings
{
    // Ultrasonic Rangefinder: distance from Feeder Station wall at which we should stop
    // so that the arm doesn't crash into the end wall.
    // Depending on whether we use ultrasonic or sonar, change this value
    public static final int FEEDER_STATION_STOP_DISTANCE = 1; // centimeters
}
