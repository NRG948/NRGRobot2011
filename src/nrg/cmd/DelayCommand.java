/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrg.cmd;

import nrg.*;

/**
 * Delays between commands in autonomous
 * @author Matthew
 */
public class DelayCommand extends CommandBase
{
    private long millisec;
    private long stopTime;

    public DelayCommand(long msec)
    {
	millisec = msec;
    }

    public void init()
    {
	stopTime = System.currentTimeMillis() + millisec;
    }

    public boolean run()
    {
	return System.currentTimeMillis() >= stopTime;
    }

    public void finalize()
    {
    }
}
