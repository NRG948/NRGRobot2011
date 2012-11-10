package nrg.cmd;

import nrg.*;

/**
 *
 * @author Stephen
 */
public class DriveCommand extends CommandBase
{
    private long msec;
    private long startTime;
    private double motorSpeed;

    public DriveCommand(long milliseconds, double motorSpeed)
    {
	msec = milliseconds;
	this.motorSpeed = motorSpeed; // negative means drive away from the driver station
    }

    public void init()
    {
	startTime = System.currentTimeMillis();
    }

    public boolean run()
    {
	NRGRobot.getDrive().mecanumDrive_Cartesian(0.0, motorSpeed, 0.0, NRGSensors.gyro.getHeading());
	return (System.currentTimeMillis() - startTime > msec);
    }

    public void finalize()
    {
    }
}
