package nrg.cmd;

/**
 * @author Stephen
 */
public abstract class CommandBase
{
    public abstract void init();

    /**
     * Runs the command
     * @return whether the command was completed
     */
    public abstract boolean run();

    public boolean initAndRun()
    {
	init();
	boolean done = run();
	if (done)
	    finalize();
	return done;

    }

    public abstract void finalize();

    public String toString()
    {
	String name = this.getClass().getName();
	return name.substring(name.lastIndexOf('.') + 1);
    }
}
