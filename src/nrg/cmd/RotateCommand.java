/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrg.cmd;

import nrg.*;

/**
 * This class was made to test the minimum value the robot twists at(so far, we think about 0.35)
 * @author Matthew
 */
public class RotateCommand extends CommandBase
{
    private long time;
    private double twist;
    private long startTime;

    public RotateCommand(long msec, double t)
    {
	time = msec;
	twist = t;
    }

    public void init()
    {
	startTime = System.currentTimeMillis();
    }

    public boolean run()
    {
	NRGRobot.getDrive().mecanumDrive_Cartesian(0, 0, twist, NRGSensors.gyro.getHeading());
	return (System.currentTimeMillis() - startTime > time);
    }

    public void finalize()
    {
	NRGRobot.getDrive().mecanumDrive_Cartesian(0, 0, 0, NRGSensors.gyro.getHeading());
    }
}
