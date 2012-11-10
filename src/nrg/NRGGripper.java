package nrg;

import edu.wpi.first.wpilibj.*;

/**
 * This class has something to do with the Gripper. I think.
 * @author Irving
 */
public class NRGGripper
{
    // possible states
    public static final int NEUTRAL = 0;
    public static final int POSSESS = 1;
    public static final int REPEL = 2;
    public static final int ROTATE_UP = 3;
    public static final int ROTATE_DOWN = 4;
    private int state;
    private Jaguar topMotor;
    private Jaguar bottomMotor;

    public NRGGripper()
    {
	state = NEUTRAL;
	topMotor = new Jaguar(NRGGripperSettings.TOP_MOTOR_CHANNEL);
	bottomMotor = new Jaguar(NRGGripperSettings.BOTTOM_MOTOR_CHANNEL);
    }

    public int getState()
    {
	return state;
    }

    public void setState(int newState)
    {
	switch (newState)
	{
	    // great idea Austin.
	    case POSSESS:
		if (NRGSensors.senseTube())
		{
		    state = NEUTRAL;
		    break;
		} // fall through
	    case NEUTRAL:
	    case REPEL:
	    case ROTATE_UP:
	    case ROTATE_DOWN:
		state = newState;
		break;
	    default:
		Debug.println(Debug.GRIPPER, "tried to NRGGripper.setState() with newState=" + newState);
	}
    }

    public void updateStateFromJoystick()
    {
	int newState = NRGJoystickInput.getGripperButton();
	Debug.println(Debug.GRIPPER, "in NRGGripper.updateStateFromJoystick(): NRGJoystickInput.getGripperButton() returned " + newState);

	if (newState > 0)
	    setState(newState);
	else
	    state = NEUTRAL;
    }

    public void update()
    {
	Debug.println(Debug.GRIPPER, "NRGGripper.update() with state=" + state);

	// decisions, decisions...could make these each separate methods
	switch (state)
	{
	    case NEUTRAL:
		// both motors have a coffee break
		topMotor.set(0);
		bottomMotor.set(0);
		break;
	    case POSSESS:
		// both motors in
		topMotor.set(NRGGripperSettings.POSSESS_MOTOR_SPEED * NRGGripperSettings.TOP_MOTOR_OUT_MULTIPLER);
		bottomMotor.set(NRGGripperSettings.POSSESS_MOTOR_SPEED * NRGGripperSettings.BOTTOM_MOTOR_OUT_MULTIPLIER);
		break;
	    case REPEL:
		// both motors out
		topMotor.set(NRGGripperSettings.REPEL_MOTOR_SPEED * NRGGripperSettings.TOP_MOTOR_OUT_MULTIPLER * -1);	// -1 for opposite
		bottomMotor.set(NRGGripperSettings.REPEL_MOTOR_SPEED * NRGGripperSettings.BOTTOM_MOTOR_OUT_MULTIPLIER * -1);
		break;
	    case ROTATE_UP:
		// to rotate up, top motor in, bottom out
		topMotor.set(NRGGripperSettings.ROTATE_MOTOR_SPEED * NRGGripperSettings.TOP_MOTOR_OUT_MULTIPLER * -1);
		bottomMotor.set(NRGGripperSettings.ROTATE_MOTOR_SPEED * NRGGripperSettings.BOTTOM_MOTOR_OUT_MULTIPLIER);
		break;
	    case ROTATE_DOWN:
		// to rotate down, top motor out, bottom in
		topMotor.set(NRGGripperSettings.ROTATE_MOTOR_SPEED * NRGGripperSettings.TOP_MOTOR_OUT_MULTIPLER);
		bottomMotor.set(NRGGripperSettings.ROTATE_MOTOR_SPEED * NRGGripperSettings.BOTTOM_MOTOR_OUT_MULTIPLIER * -1);
		break;
	    default:
		Debug.println(Debug.GRIPPER, "tried to NRGGripper.update() with state=" + state);
	}
    }
}
