package nrg.cmd;

import nrg.*;

/**
 * Allows you to print debug lines in autonomous between commands and stuff
 * @author Matthew
 */
public class DebugPrintCommand extends CommandBase
{
    private String s;

    public DebugPrintCommand(String s)
    {
	this.s = s;
    }

    public void init()
    {
    }

    public boolean run()
    {
	Debug.println(true, s);
	return true;
    }

    public void finalize()
    {
    }
}
