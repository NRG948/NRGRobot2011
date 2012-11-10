package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * @author Matthew, Stephen, Dustin
 */
public class NRGLineTracker
{
    private static final double STOP_DIST = 20; //for ultrasonic in cm, TODO: Calibrate depending on ultrasonic or maxsonar
    private static final double SMALL_X_CHANGE = 0.2;
    private static final double LARGE_X_CHANGE = 0.48;
    private static final double ROT_LIMIT = 0.3;
    private static final double REVERSE_TIME = 0.6; // TODO: figure out if this is in seconds...?
    private NRGDrive nrgDrive;
    private Timer timer;
    private int binaryValue;
    private int previousValue;
    private double powerProfile[];
    private double time, minimumStopTime;
    private double speed, xValue, rotation;
    private boolean forkLeft;
    private boolean forkRight;
    private boolean straight;
    private boolean atCross;
    private static final double MAX_STRAFE_TIME = 2;
    private static double strafeStartTime;
    private static boolean strafeRight;
    private double desiredHeading = 180.0;

    public NRGLineTracker(NRGDrive drive)
    {
	nrgDrive = drive;
	previousValue = 0;
	strafeRight = true;
	//getRockerVals
	forkLeft = NRGIO.getTrackLeft();
	forkRight = NRGIO.getTrackRight();
	straight = !(forkLeft || forkRight);
	// the power profiles for the straight and forked robot path. They are
	// different to let the robot drive more slowly as the robot approaches
	// the fork on the forked line case.
	double forkProfile[] =
	{
	    0.3, 0.3, 0.25, 0.2, 0.2, 0.15, 0.15, 0.08
	};
	double straightProfile[] =
	{
	    0.4, 0.3, 0.2, 0.2, 0.2, 0.15, 0.15, 0.08
	};
	powerProfile = (straight) ? straightProfile : forkProfile;
	// when the robot should look for end
	minimumStopTime = (straight) ? 2.0 : 4.0;
	// if robot has arrived at end
	atCross = false;
	// time the path over the line
	timer = new Timer();
	timer.start();
	//timer.reset();
	Debug.println(Debug.LINE_FOLLOWING, "Line Following, Mode: Moving "
		+ (forkLeft ? "Left" : "") + (forkRight ? "Right" : "") + (straight ? "Straight" : ""));
	NRGLCD.println3("LineTracker Mode: " + (forkLeft ? "L" : "") + (forkRight ? "R" : "") + (straight ? "S" : ""));
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void trackLine()
    {
	// run until robot reaches "T"
	if ((time = timer.get()) >= 8.0 || atCross)
	{
	    // Done with loop - stop the robot. Robot ought to be at the end of the line
	    nrgDrive.mecanumDrive_Cartesian(0.0, 0.0, 0.0, NRGSensors.gyro.getHeading());
	    return;
	}
	int timeInSeconds = (int) time;

	//Check if close to end with ultrasonic/sonar.
	if (NRGSensors.sonar.getDistance() <= STOP_DIST)
	//if (NRGSensors.ultrasonic.getDist() <= STOP_DIST)
	{
	    Debug.println(Debug.LINE_FOLLOWING, "Time: " + MathHelper.round(time, 3) + "AT STOP DIST: " + STOP_DIST);
	    xValue = 0;
	    atCross = true;
	    double start = timer.get();
	    while (timer.get() - start < REVERSE_TIME)
	    {
		double yValue = speed > 0 ? (Math.abs(speed) > Math.abs(xValue) ? Math.sqrt(speed * speed - xValue * xValue) : 0.15) : 0;
		nrgDrive.mecanumDrive_Cartesian(0.0, -yValue, 0.0, (NRGSensors.gyro.getHeading() + 180) % 360);
	    }
	    speed = 0;
	}
	// read the sensors
	boolean left = NRGSensors.lineSensorLeft.get();
	boolean middle = NRGSensors.lineSensorMiddle.get();
	boolean right = NRGSensors.lineSensorRight.get();
	int leftValue = left ? 1 : 0;
	int middleValue = middle ? 1 : 0;
	int rightValue = right ? 1 : 0;
	// compute the single value from the 3 sensors. Notice that the bits
	// for the outside sensors are flipped depending on left or right
	// fork. Also the sign of the steering direction is different for left/right.
	if (forkRight || straight)
	{
	    binaryValue = leftValue * 4 + middleValue * 2 + rightValue;
	    xValue = 1;
	}
	else
	{
	    binaryValue = leftValue + middleValue * 2 + rightValue * 4;
	    xValue = -1;
	}

	speed = powerProfile[timeInSeconds] * 1.6;

	switch (binaryValue)
	{
	    case 7:
		if (time > minimumStopTime)
		{
		    Debug.println(Debug.LINE_FOLLOWING, "Time: " + MathHelper.round(time, 3) + "AT CROSS");
		    xValue = 0;
		    atCross = true;
		    double start = timer.get();
		    while (timer.get() - start < REVERSE_TIME)
		    {
			double yValue = speed > 0 ? (Math.abs(speed) > Math.abs(xValue) ? Math.sqrt(speed * speed - xValue * xValue) : 0.15) : 0;
			nrgDrive.mecanumDrive_Cartesian(0.0, -yValue, 0.0, NRGSensors.gyro.getHeading());
		    }
		    speed = 0;
		    break;
		}
	    // else, go in the chosen direction
	    case 5: //|X X|
	    case 0: //|   |
		if (previousValue == 1)
		    xValue *= LARGE_X_CHANGE;
		else
		{
		    xValue *= -LARGE_X_CHANGE;
		}
		break;
	    case 1: //|  X| or |X  | (on line)
		xValue = 0;
		break;
	    default:
		xValue *= -SMALL_X_CHANGE;
	}
	// Correct the heading so that we end up at desiredHeading
	rotation = MathHelper.clamp(((desiredHeading - NRGSensors.gyro.getHeading() % 360) / 30.0), -ROT_LIMIT, ROT_LIMIT);

	double yValue = speed > 0 ? (Math.abs(speed) > Math.abs(xValue) ? Math.sqrt(speed * speed - xValue * xValue) : 0.1) : 0;
	//if (binaryValue != previousValue)
	{
	    // print current status for debugging
	    Debug.println(Debug.LINE_FOLLOWING, "Time: " + MathHelper.round(time, 3) + " Sensors: |" + (left ? "X" : " ") + (middle ? "X" : " ") + (right ? "X" : " ")
		    + "| XVal: " + -xValue + " YVal: " + yValue + " Heading: " + rotation + " Gyro: " + MathHelper.round(NRGSensors.gyro.getHeading(), 3));
	}
	// set the robot strafing, speed, and heading
	nrgDrive.mecanumDrive_Cartesian(-xValue, yValue, rotation, NRGSensors.gyro.getHeading() % 360);
	if (binaryValue != 0)
	{
	    previousValue = binaryValue;
	}
	Timer.delay(0.01);
    }

    public void strafeLine()
    {
	if ((time = timer.get()) >= 8.0 || atCross)
	{
	    nrgDrive.mecanumDrive_Cartesian(0.0, 0.0, 0.0, NRGSensors.gyro.getHeading());
	    return;
	}
	boolean left = NRGSensors.lineSensorLeft.get();
	boolean middle = NRGSensors.lineSensorMiddle.get();
	boolean right = NRGSensors.lineSensorRight.get();
	int leftValue = left ? 1 : 0;
	int middleValue = middle ? 1 : 0;
	int rightValue = right ? 1 : 0;
	binaryValue = leftValue + middleValue * 2 + rightValue * 4;

	switch (binaryValue)
	{
	    case 2:
		Debug.println(Debug.LINE_FOLLOWING, "Time: " + MathHelper.round(time, 3) + "STRAFED TO LINE");
		xValue = 0;
		atCross = true;
		break;
	    case 1:
		xValue = LARGE_X_CHANGE;
		break;
	    case 3:
		xValue = SMALL_X_CHANGE;
		break;
	    case 4:
		xValue = -LARGE_X_CHANGE;
		break;
	    case 6:
		xValue = -SMALL_X_CHANGE;
		break;
	    case 0: //|   |
		if (previousValue == 1 || previousValue == 3)
		    xValue = LARGE_X_CHANGE;
		else if (previousValue == 4 || previousValue == 6)
		    xValue = -LARGE_X_CHANGE;
		else
		{
		    if (strafeStartTime < 0.00001)
			strafeStartTime = time;
		    if (strafeRight)
		    {
			xValue = LARGE_X_CHANGE;
			if (time - strafeStartTime > MAX_STRAFE_TIME)
			{
			    xValue = 0;
			    strafeRight = false;
			}
		    }
		    else
		    {
			xValue = -LARGE_X_CHANGE;
			if (time - strafeStartTime > MAX_STRAFE_TIME * 2)
			{
			    xValue = 0;
			    atCross = true;
			}
		    }
		}
		break;
	    default:
		xValue = 0;
		atCross = true;
	}
	// Correct the heading so that we end up at 0 degrees
	rotation = MathHelper.clamp(((desiredHeading - NRGSensors.gyro.getHeading() % 360) / 30.0), -ROT_LIMIT, ROT_LIMIT);
	//if (binaryValue != previousValue)
	{
	    Debug.println(Debug.LINE_FOLLOWING, "Time: " + MathHelper.round(time, 3) + " Sensors: |" + (left ? "X" : " ") + (middle ? "X" : " ") + (right ? "X" : " ")
		    + "| XVal: " + -xValue + " Heading: " + rotation + " Gyro: " + MathHelper.round(NRGSensors.gyro.getHeading(), 3));
	}
	nrgDrive.mecanumDrive_Cartesian(-xValue, 0.0, rotation, NRGSensors.gyro.getHeading());

	if (binaryValue != 0)
	{
	    previousValue = binaryValue;
	    strafeStartTime = 0.0;
	}

	Timer.delay(0.01);
    }

    public boolean atCross()
    {
	return atCross;
    }
}
