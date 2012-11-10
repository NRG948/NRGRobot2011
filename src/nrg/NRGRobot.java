/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package nrg;

import edu.wpi.first.wpilibj.*;
import nrg.camera.*;
import nrg.minibot.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class NRGRobot extends SimpleRobot
{
    public final String VERSION = "3/12 DL1";
    private static NRGDrive nrgDrive;
    private NRGCamera nrgCam;
    private NRGWallTracker wallTracker;
    //private NRGDashboard dash;
    private static NRGArm arm;
    private static NRGGripper gripper;
    private NRGMINIServo miniBot;
    private NRGAutonomous autonomous;
    private NRGInputAverager averager;
    private NRGLineTracker tracker;
    private static final Timer teleopTimer = new Timer();

    private boolean didAutonomous;

    protected void robotInit()
    {
	NRGSensors.initSensors();
	nrgDrive = new NRGDrive();
	if (CameraSettings.INIT_CAMERA)
	    nrgCam = new NRGCamera();
	arm = new NRGArm();
	gripper = new NRGGripper();
	//dash = new NRGDashboard();
	miniBot = new NRGMINIServo();
	averager = new NRGInputAverager(4);
	didAutonomous = false;
    }

    /**
     * This function is called once each loop while the robot is disabled.
     */
    public void disabled()
    {
	NRGSensors.update();
	NRGLCD.println3("Sho: " + MathHelper.round(arm.getShoulderMAE(), 1) + " Elb: " + MathHelper.round(arm.getElbowMAE(), 5));
	//if (NRGSensors.ultrasonic.getWriteFail())
	//NRGLCD.println4("DIST: WRITE FAIL");
	//else
	//NRGLCD.println4("DIST: " + NRGSensors.ultrasonic.getDist());
	double dist = NRGSensors.sonar.getDistance();
	averager.addValue(dist);
	NRGLCD.println4("dist Av:" + MathHelper.round(averager.getAverageValue(), 3) + " raw:" + MathHelper.round(NRGSensors.sonar.getDistance(), 3));
	NRGLCD.println5("Gyr: " + MathHelper.round(NRGSensors.gyro.getHeading(), 2));
	NRGLCD.println6("IR L: " + MathHelper.round(NRGSensors.leftIRSensor.getDistance(), 2) + " R: " + MathHelper.round(NRGSensors.rightIRSensor.getDistance(), 2));
    }

    /**
     * This function is called once each loop while the robot is enabled and in autonomous mode.
     */
    public void autonomous()
    {
	NRGSensors.update();
	autonomous.update();
	//tracker.trackLine();
	NRGLCD.println5("Gyr: " + MathHelper.round(NRGSensors.gyro.getHeading(), 2));
    }

    /**
     * This function is called once each loop while the robot is enabled and in operator control mode.
     */
    public void operatorControl()
    {
	NRGLCD.println2("X" + MathHelper.round(NRGJoystickInput.getDriveX(), 2)
		+ " Y" + MathHelper.round(NRGJoystickInput.getDriveY(), 2)
		+ " T" + MathHelper.round(NRGJoystickInput.getDriveTwist(), 2));
	//NRGLCD.println4("Ultrasonic: " + NRGSensors.ultrasonic.getDist());
	double dist = NRGSensors.sonar.getDistance();
	averager.addValue(dist);

	NRGLCD.println4("distAv:" + MathHelper.round(averager.getAverageValue(), 3) + " raw:" + MathHelper.round(NRGSensors.sonar.getDistance(), 3));
	NRGLCD.println5("Gyr: " + MathHelper.round(NRGSensors.gyro.getHeading(), 2));
	NRGLCD.println6("sh: " + MathHelper.round(NRGArm.shoulderSetpointAngleToProportion(arm.getShoulderMAE()), 4) + " el: " + MathHelper.round(NRGArm.elbowSetpointAngleToProportion(arm.getElbowMAE()), 4)/* + (arm.getManualControl() ? "S" : " M")*/);

	//NRGHeadlights.set(NRGIO.getHeadlightButton());
	NRGSensors.update();

	if (NRGJoystickInput.getWallTrackForwardLeft())
	    wallTracker.trackWall(false, NRGWallTrackerSettings.SENSOR_FACING_LEFT);
	else if (NRGJoystickInput.getWallTrackForwardRight())
	    wallTracker.trackWall(false, NRGWallTrackerSettings.SENSOR_FACING_RIGHT);
	else if (NRGJoystickInput.getWallTrackBackwardLeft())
	    wallTracker.trackWall(true, NRGWallTrackerSettings.SENSOR_FACING_LEFT);
	else if (NRGJoystickInput.getWallTrackBackwardRight())
	    wallTracker.trackWall(true, NRGWallTrackerSettings.SENSOR_FACING_RIGHT);
	else
	{
	    nrgDrive.update();
	    arm.updateArmStateFromJoystick();
	    gripper.updateStateFromJoystick();
	}
	arm.update();
	//arm.update2();
	gripper.update();
	arm.updatePIDValues();
	//dash.updateDefaultDashboard();
	if (NRGIO.getMiniBotDeployment() && (!didAutonomous || NRGRobot.getTeleOpTime() > NRGMINISettings.ALLOW_MINIBOT_DEPLOYMENT_TIME))
	{
	    while (miniBot.run())
	    {
		Debug.println(Debug.MINIBOT, "Minibot is setting angle. Angle: " + miniBot.getAngle());
	    }
	}
	else
	{
	    miniBot.reset();
	}
    }

    /**
     * This controls the program flow for the robot class. Normally it calls the
     * disabled, autonomous, and operatorControl methods only once when the robot
     * enters each mode, but we have overridden it so that the code for each mode
     * is called once each loop while the robot is in that mode.
     */
    public void robotMain()
    {
	Debug.println(Debug.ROBOT_MAIN_ROUTINES, "I AM INITIALISING! I CANNOT BE STOPPED.");
	robotInit();
	Debug.println(Debug.ROBOT_MAIN_ROUTINES, "I HAVE FINISHED INITIALISING. I AM ETERNAL!\nTHE SUN NEVER SETS ON THE BRITISH EMPIRE.");
	NRGLoopTimer timer = new NRGLoopTimer();
	while (true)
	{
	    if (isDisabled())
	    {
		Debug.println(Debug.ROBOT_MAIN_ROUTINES, "Entering Disabled Mode");
		timer.startRoutine();
		while (isDisabled())
		{
		    getWatchdog().feed();
		    NRGLCD.update();
		    NRGLCD.clear();
		    long time = timer.nextLoop();
		    NRGLCD.println1("DSBL " + VERSION + " " + time + "/" + timer.getAverageLoopTime());
		    disabled();
		}
		System.out.println("Leaving Disabled Mode");
		
	    }
	    if (isEnabled() && isAutonomous())
	    {
		Debug.println(Debug.AUTONOMOUS_ROUTINES, "Entering Autonomous Mode");
		initAutonomous();
		timer.startRoutine();
		while (isEnabled() && isAutonomous())
		{
		    getWatchdog().feed();
		    NRGLCD.update();
		    NRGLCD.clear();
		    long time = timer.nextLoop();
		    NRGLCD.println1("AUTO " + VERSION + " " + time + "/" + timer.getAverageLoopTime());
		    autonomous();
		}
		System.out.println("Leaving Autonomous Mode");
		didAutonomous = true;
	    }
	    if (isEnabled() && isOperatorControl())
	    {
		Debug.println(Debug.DRIVE, "Entering Operator Control Mode");
		initTeleOp();
		timer.startRoutine();
		while (isEnabled() && isOperatorControl())
		{
		    getWatchdog().feed();
		    NRGLCD.update();
		    NRGLCD.clear();
		    long time = timer.nextLoop();
		    NRGLCD.println1("TELE " + VERSION + " " + time + "/" + timer.getAverageLoopTime());
		    operatorControl();
		}
		System.out.println("Leaving Operator Control Mode");
		didAutonomous = false;
	    }
	}
    }

    public void initTeleOp()
    {
	wallTracker = new NRGWallTracker(nrgDrive, gripper);
	arm.initArm();
	teleopTimer.reset();
	teleopTimer.start();
    }

    /**
     * @return seconds after the start of teleop
     */
    public static double getTeleOpTime()
    {
	//must convert from microseconds
	return teleopTimer.get() * 1000000;
    }

    public void initAutonomous()
    {
	NRGSensors.gyro.reset();
	arm.initArm();
	autonomous = new NRGAutonomous();
    }

    public static NRGDrive getDrive()
    {
	return nrgDrive;
    }

    public static NRGArm getArm()
    {
	return arm;
    }

    public static NRGGripper getGripper()
    {
	return gripper;
    }
}
