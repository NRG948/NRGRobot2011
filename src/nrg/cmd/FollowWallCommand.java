package nrg.cmd;

import nrg.*;

/**
 * @author Stephen
 */
public class FollowWallCommand extends CommandBase
{
    NRGWallTracker tracker;
    double currentTime;

    public FollowWallCommand(NRGDrive nrgDrive, NRGGripper gripper)
    {
	tracker = new NRGWallTracker(nrgDrive, gripper);
    }

    public void init()
    {
    }

    public boolean run()
    {
	tracker.trackWallAutonomous(false);
	//int ultrasonicValue = (int) NRGSensors.ultrasonic.getDist();
	int sonarValue = (int) NRGSensors.sonar.getDistance();
	//return (ultrasonicValue <= NRGAutonomousSettings.FEEDER_STATION_STOP_DISTANCE);
	return (sonarValue <= NRGAutonomousSettings.FEEDER_STATION_STOP_DISTANCE);
    }

    public void finalize()
    {
    }
}
