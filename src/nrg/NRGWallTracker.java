package nrg;

import edu.wpi.first.wpilibj.Timer;

/**
 * WallTracker aids teleop in the obtaining of new tubes. By driving down the lane.
 * TODOs:
 *  -change arm height as it goes (PICK_UP_FEEDER as it approaches, STORED as it leaves)
 *  -add two PIDs, one for distance from wall and one for rotation
 *  -stop before hitting wall using NRGSensors.ultrasonic.getDist() or something
 *
 * @author Irving
 */
public class NRGWallTracker
{
    // passed in from NRGRobot.java
    private NRGDrive nrgDrive;
    private NRGGripper gripper;
    // currently assigned values in constructor
    private double targetHeading;
    private double targetSideDistance;
    // see trackWall()
    private double lastSideDistance;
    private Timer timer;
    // didn't really know what to call this - it causes the 1 second pause before direction reverse
    private boolean justReversed = false;

    public NRGWallTracker(NRGDrive newDrive, NRGGripper newGripper)
    {
	nrgDrive = newDrive;
	gripper = newGripper;
	targetSideDistance = 14.0;	// 10 inches is about how much distance we will have at competition (plus 4 for bumper, etc.)
	targetHeading = 0.0;	// 0 degree heading is facing away from driver station, toward feeder station
	// initial value
	lastSideDistance = targetSideDistance;

	Debug.println(Debug.WALL_TRACKER, "WallTracker() | targetDistance=" + targetSideDistance + " | targetHeading=" + targetHeading);

	timer = new Timer();
	timer.start();
    }

    private double getCurrentDistance(double sensorFacing)
    {
	if (sensorFacing == NRGWallTrackerSettings.SENSOR_FACING_LEFT)
	    return NRGSensors.leftIRSensor.getDistance();
	else if (sensorFacing == NRGWallTrackerSettings.SENSOR_FACING_RIGHT)
	    return NRGSensors.rightIRSensor.getDistance();
	else
	{
	    Debug.println(Debug.WALL_TRACKER, "in NRGWallTracker.getCurrentDistance() sensorFacing=" + MathHelper.round(sensorFacing, 2));
	    return targetSideDistance;
	}
    }

    // legacy version of function (used in testing)
    public int trackWallAutonomous(boolean reverse)
    {
	// if we just switched direction
	if (reverse && !justReversed)
	{
	    justReversed = true;

	    timer.reset();

	    while (timer.get() < 1.0)
	    {
		// wait one second
	    }

	    timer.reset();
	}
	// if we run past time
	if (timer.get() > NRGWallTrackerSettings.runTime)
	{
	    nrgDrive.mecanumDrive_Cartesian(0, 0, 0, NRGSensors.gyro.getHeading());
	    return NRGWallTrackerSettings.RETURN_TIME_OUT;
	}

	trackWall(reverse, NRGWallTrackerSettings.SENSOR_FACING_RIGHT);

	return NRGWallTrackerSettings.RETURN_STILL_RUNNING;
    }

    public int trackWall(boolean reverse, double sensorFacing)
    {
	if (!reverse)
	{
	    NRGRobot.getArm().setArmState(NRGArmSettings.PICK_UP_FEEDER);
	    gripper.setState(NRGGripper.POSSESS);
	}
	else
	{
	    NRGRobot.getArm().setArmState(NRGArmSettings.STORED);
	    gripper.setState(NRGGripper.NEUTRAL);
	}
	/* // if we run past time
	if (timer.get() > NRGWallTrackerSettings.runTime)
	{
	nrgDrive.mecanumDrive_Cartesian(0, 0, 0, NRGSensors.gyro.getHeading());
	return NRGWallTrackerSettings.RETURN_TIME_OUT;
	} */

	// stop if we get close enough to the wall...
	//int distToWall = (int) NRGSensors.ultrasonic.getDist();
	//if ( !reverse && distToWall <= NRGAutonomousSettings.FEEDER_STATION_STOP_DISTANCE)
	//{
	//    nrgDrive.mecanumDrive_Cartesian(0, 0, 0, NRGSensors.gyro.getHeading());
	//    return NRGWallTrackerSettings.RETURN_STILL_RUNNING;
	//}

	double currentHeading = NRGSensors.gyro.getHeading();
	currentHeading = currentHeading % 360;	// gyro value CAN exceed 0/360

	double rotation = targetHeading - currentHeading;
	//Debug.println(Debug.WALL_TRACKER, "targetHeading=" + MathHelper.round(targetHeading, 2) + " currentHeading=" + MathHelper.round(currentHeading, 2) + " diff=" + MathHelper.round(rotation, 2));
	rotation = rotation / 30.0;   // we'll say if 30 degrees off then use max rotation - same as line tracker
	rotation = MathHelper.clamp(rotation, NRGWallTrackerSettings.max_rotation);
	//Debug.println(Debug.WALL_TRACKER, "after clamp=" + MathHelper.round(rotation, 2));

	double currentSideDistance = getCurrentDistance(sensorFacing);
	if (currentSideDistance < 0)    // if the IR takes a fail pill
	    currentSideDistance = lastSideDistance; // assume it's last value
	else
	    lastSideDistance = currentSideDistance;

	// TODO: WHEN REVERSE SIDE, THIS IS WHAT NEEDS TO CHANGE
	// probably just make it targetDistance - currentDistance
	double xdrive = currentSideDistance - targetSideDistance;
	xdrive = xdrive / 10.0;	// we'll say 10 inches off is 1.0 drive
	xdrive = MathHelper.clamp(xdrive, NRGWallTrackerSettings.max_xdrive);

	//Debug.println(Debug.WALL_TRACKER, "xdrive=" + xdrive + " rotation=" + rotation);

	// I have no idea what I'm doing so I'll just put in random values and test them later
	double ySpeed = reverse ? -NRGWallTrackerSettings.yspeed : NRGWallTrackerSettings.yspeed;
	nrgDrive.mecanumDrive_Cartesian(xdrive * sensorFacing, ySpeed, rotation, NRGSensors.gyro.getHeading());

	//Debug.println(Debug.WALL_TRACKER, MathHelper.round(timer.get(), 4) + ": IR=" + MathHelper.round(currentDistance, 2) + " mecanumDrive_Cartesian(" + MathHelper.round(xdrive, 2) + ", " + MathHelper.round(NRGWallTrackerSettings.yspeed, 2) + ", " + MathHelper.round(rotation, 2) + ", " + MathHelper.round(NRGSensors.gyro.getAngle(), 2) + ")");
	// 2/17: why use MathHelper.round() when we can use the mighty Debug.printRound()? hehehehe
	Debug.printRoundln(Debug.WALL_TRACKER, "%0: IR=%1 ULTRASONIC=%6 mecanumDrive_Cartesian(%2, %3, %4, %5)", new double[]
		{
		    timer.get(), currentSideDistance, xdrive * sensorFacing, NRGWallTrackerSettings.yspeed, rotation, NRGSensors.gyro.getHeading(), NRGSensors.ultrasonic.getDist()
		});

	// for when returning to regular teleop, make sure P doesn't correct for old heading
	NRGSensors.gyro.setDesiredHeading(targetHeading);

	return NRGWallTrackerSettings.RETURN_STILL_RUNNING;
    }
}
