package nrg.cmd;

import nrg.*;

/**
 * This class does not need to really be used anymore, look to DriveCommand
 * @author Matthew & Eric
 */
public class GripperExpelCommand extends CommandBase
{
    private long msec;
    private long startTime;

    public GripperExpelCommand(long millisec)
    {
	msec = millisec;
    }

    public void init()
    {
	startTime = System.currentTimeMillis();
    }

    public boolean run()
    {
	NRGRobot.getGripper().setState(NRGGripper.REPEL);
	return (System.currentTimeMillis() - startTime > msec);
    }

    public void finalize()
    {
	NRGRobot.getGripper().setState(NRGGripper.NEUTRAL);
    }
}
