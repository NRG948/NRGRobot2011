package nrg.cmd;

import nrg.*;

/**
 * @author Dustin
 */
public class StrafeLineCommand extends CommandBase
{
    NRGDrive nrgDrive;
    NRGLineTracker lineTracker;

    public StrafeLineCommand(NRGDrive myNRGDrive)
    {
	nrgDrive = myNRGDrive;
    }

    public void init()
    {
	lineTracker = new NRGLineTracker(nrgDrive);
    }

    public boolean run()
    {
	lineTracker.strafeLine();
	return lineTracker.atCross();
    }

    public void finalize()
    {
    }
}
