package nrg.cmd;

/**
 *
 * @author Stephen
 */
public class ParallelCommands extends CommandBase
{
    CommandBase[] cmds;
    boolean[] cmdFinished;

    public ParallelCommands(CommandBase[] cmds)
    {
	this.cmds = cmds;
    }

    public void init()
    {
	cmdFinished = new boolean[cmds.length];
	for (int i = 0; i < cmds.length; i++)
	{
	    cmds[i].init();
	    cmdFinished[i] = false;
	}
    }

    public boolean run()
    {
	boolean result = true;
	for (int i = 0; i < cmds.length; i++)
	{
	    if (!cmdFinished[i])
	    {
		result = result && cmds[i].run();
	    }
	}
	return result;
    }

    public void finalize()
    {
	for (int i = 0; i < cmds.length; i++)
	{
	    cmds[i].finalize();
	}
    }
}
