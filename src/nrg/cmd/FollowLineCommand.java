package nrg.cmd;

import nrg.*;

/**
 * @author Stephen
 */
public class FollowLineCommand extends CommandBase
{
    NRGDrive nrgDrive;
    NRGLineTracker lineTracker;

    public FollowLineCommand(NRGDrive myNRGDrive)
    {
	nrgDrive = myNRGDrive;
    }

    public void init()
    {
	lineTracker = new NRGLineTracker(nrgDrive);
    }

    public boolean run()
    {
	lineTracker.trackLine();
	return lineTracker.atCross();
    }

    public void finalize()
    {
    }
}
