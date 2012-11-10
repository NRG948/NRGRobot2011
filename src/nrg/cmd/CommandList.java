package nrg.cmd;

/**
 * Represents a list of autonomous commands to be run in sequential order.
 * @author Stephen
 */
public class CommandList extends CommandBase
{
    CommandBase[] cmds;
    private int commandIndex;

    /**
     * Constructs a new instance of command base
     * @param cmds A commandbase array that is run through in sequence. It better not be null.
     */
    public CommandList(CommandBase[] cmds)
    {
	if (cmds == null)
	    throw new IllegalArgumentException("cmds cannot be null.");
	this.cmds = cmds;
    }

    /**
     * Initializes the first command base if there is one.
     */
    public void init()
    {
	commandIndex = 0;
	if (cmds.length > 0)
	    cmds[commandIndex].init();
    }

    /**
     * Runs through the current command base to check if it has completed. If it has completed it finalizes
     * the command base, increments the index of the command, and initializes the next command
     * @return whether or not we are done with the sequence of commands
     */
    public boolean run()
    {
	boolean done = cmds[commandIndex].run();
	if (done)
	{
	    cmds[commandIndex].finalize();
	    if (++commandIndex < cmds.length)
		cmds[commandIndex].init();
	}
	return commandIndex >= cmds.length;
    }

    /**
     * Don't do anything.
     */
    public void finalize()
    {
    }
}
