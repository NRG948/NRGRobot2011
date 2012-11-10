package nrg.cmd;

import nrg.*;

/**
 * @author Stephen
 */
public class ArmSetCommand extends CommandBase
{
    private NRGArm arm;
    private int state;

    public ArmSetCommand(NRGArm arm, int state)
    {
	this.arm = arm;
	this.state = state;
    }

    public void init()
    {
	arm.setArmState(state);
    }

    public boolean run()
    {
	arm.update();
	//arm.update2();
	return arm.elbowAtSetpoint() && arm.shoulderAtSetpoint();
    }

    public void finalize()
    {
	arm.setArmState(NRGArmSettings.MANUAL_CONTROL);
    }
}
