package nrg;

import nrg.cmd.*;

/**
 * @author Stephen, Dustin
 */
public class NRGAutonomous
{
    private CommandBase cmd;
    private int cmdIndex;
    private boolean initialized;
    private CommandBase[] cmds;
    /**
     * An empty command base array, it does nothing
     */
    public static CommandBase[] cmdsEmpty =
    {
    };
    /**
     * A command base array that uses the line tracker to track the line
     */
    public static CommandBase[] followLine =
    {
	new FollowLineCommand(NRGRobot.getDrive()),
	new GripperExpelCommand(1000)
    };
    /**
     * A command base array that uses  the wall tracker to track the wall
     */
    public static CommandBase[] followWall =
    {
	new FollowWallCommand(NRGRobot.getDrive(), NRGRobot.getGripper())
    };
    /**
     * A command base array that runs a line tracker command and an arm set command in parallel
     * It sets the arm to the low peg setpoint while following the middle line
     */
    public static CommandBase[] parallelLFAL =
    {
	new ParallelCommands(new CommandBase[]
	{
	    new FollowLineCommand(NRGRobot.getDrive()),
	    new ArmSetCommand(NRGRobot.getArm(), NRGArmSettings.SEEKING_LOW)
	}),
	new GripperExpelCommand(1000)
    };
    /**
     * A command base array that runs a line tracker command and an arm set command in parallel
     * It sets the arm to the high peg setpoint while following the middle line
     */
    public static CommandBase[] parallelLFAM =
    {
	new ParallelCommands(new CommandBase[]
	{
	    new FollowLineCommand(NRGRobot.getDrive()),
	    new ArmSetCommand(NRGRobot.getArm(), NRGArmSettings.SEEKING_MID)
	}),
	new GripperExpelCommand(1000)
    };
    /**
     * A command base array that runs a line tracker command and an arm set command in parallel
     * It sets the arm to the high peg setpoint while following the middle line
     */
    public static CommandBase[] parallelLFAH =
    {
	new ParallelCommands(new CommandBase[]
	{
	    new FollowLineCommand(NRGRobot.getDrive()),
	    new ArmSetCommand(NRGRobot.getArm(), NRGArmSettings.SEEKING_SIDE_HIGH)
	}),
	new GripperExpelCommand(1000)
    };

    //Runs during autonomous to score the ubertube on a high peg.
    public static CommandBase[] autonomousHigh =
    {
	new FollowLineCommand(NRGRobot.getDrive()),
	new DriveCommand(1000, -0.4),
	//TODO:Uncomment after testing.
	//new ArmSetCommand(NRGRobot.getArm(), ((NRGIO.getTrackLeft() || NRGIO.getTrackRight()) ? NRGArmSettings.SEEKING_SIDE_HIGH : NRGArmSettings.SEEKING_HIGH)),
	new DriveCommand(600, 0.3),
	new ParallelCommands(new CommandBase[]
	{
	    new DriveCommand(400, -0.25),
	    new GripperExpelCommand(500)
	}),
	new DriveCommand(1600, -0.4),
	//new ArmSetCommand(NRGRobot.getArm(), NRGArmSettings.STORED)
	new RotateToCommand(0),
	new DriveCommand(3000, 0.4)
    };
    //tests the min value of twist or something like that
    public static CommandBase[] twistTest =
    {
	new RotateCommand(1000, -0.25),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.26),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.27),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.28),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.29),
	new DelayCommand(2000),
	new DebugPrintCommand("About to do -0.3 twist"),
	new RotateCommand(1000, -0.30),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.31),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.32),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.33),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.34),
	new DelayCommand(2000),
	new DebugPrintCommand("About to do -0.35 twist"),
	new RotateCommand(1000, -0.35),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.36),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.37),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.38),
	new DelayCommand(2000),
	new RotateCommand(1000, -0.39),
	new DelayCommand(2000),
	new DebugPrintCommand("Ended at -0.39 twist")
    };

    /**
     * @return A command base array that is used during autonomous based on whatever button you are holding
     * at the beginning of the autonomous loop. If nothing is held, it runs the cmdsDefault
     */
    public static CommandBase[] getCommandBase()
    {
	switch (NRGJoystickInput.getFirstButtonState(NRGJoystickInput.JOYSTICK1))
	{
	    case 1:
		return followLine;
	    case 2:
		return followWall;
	    case 3:
		return parallelLFAL;
	    case 4:
		return parallelLFAM;
	    case 5:
		return parallelLFAH;
	    case 6:
		return autonomousHigh;
	    case 7:
		return twistTest;
	    default:
		return cmdsDefault;
	}
    }
    // The command array that gets used in NRGRobot class in autonomous mode.
    public static CommandBase[] cmdsDefault = autonomousHigh;

    /********************************************************/
    public NRGAutonomous()
    {
	CommandBase[] cmds = getCommandBase();
	this.cmds = cmds;
	initialized = false;
	cmdIndex = 0;
	if (cmdIndex < cmds.length)
	    cmd = cmds[cmdIndex];
    }

    /**
     * @return return whether or not the current command has completed
     */
    public boolean commandsCompleted()
    {
	return (cmd == null);
    }

    public void update()
    {
	/*
	 * for autonomous wall tracker:

	// this stuff handles state - see definitions above
	if (state == 0)
	{
	if (wallTracker.trackWallAutonomous(false) == 0)
	state++;
	}
	else if (state == 1)
	{
	if (wallTracker.trackWallAutonomous(true) == 0)
	state++;
	}
	else
	{
	nrgDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
	}
	 */

	//If there are no more commands, do nothing
	if (commandsCompleted())
	    return;
	// Initializes commands if not already.
	if (!initialized)
	{
	    Debug.println(Debug.CMDDISPATCH, cmd.toString());
	    cmd.init();
	    initialized = true;
	}
	if (cmd.run())     // Run the command
	{
	    // The current command just finished, so go to next command.
	    initialized = false;
	    cmd.finalize();
	    cmdIndex++;
	    // Checks whether it is on last command or not.
	    if (cmdIndex < cmds.length)
	    {
		cmd = cmds[cmdIndex];
		Debug.println(Debug.CMDDISPATCH, "Autonomous: Moving to next command, index " + cmdIndex + ".");
	    }
	    else
	    {
		Debug.println(Debug.CMDDISPATCH, "Autonomous: No more commands.");
		cmd = null;
	    }
	}
    }
}
