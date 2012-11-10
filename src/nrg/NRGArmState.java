package nrg;

/**
 *
 * @author Stephen
 */
public class NRGArmState
{
    //private double currentElbowSetpoint;
    private double currentShoulderSetpoint;
    private NRGPID elbowPID;
    private NRGPID shoulderPID;
    private double desiredShoulderSetpoint;
    private double desiredElbowSetpoint;
    double[] elbowSetpoints;
    double[] shoulderSetpoints;
    int intermediateState;

    public NRGArmState(int desired, NRGPID elbowPID, NRGPID shoulderPID)
    {
	currentShoulderSetpoint = NRGArm.shoulderSetpointAngleToProportion(shoulderPID.getAngleSetpoint());
	this.elbowPID = elbowPID;
	this.shoulderPID = shoulderPID;
	intermediateState = NRGArmSettings.STORE_ELBOW;
	switch (desired)
	{
	    case NRGArmSettings.STORED:
		desiredElbowSetpoint = NRGArmSettings.STORED_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.STORED_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.PICK_UP_GROUND:
		desiredElbowSetpoint = NRGArmSettings.PICK_UP_GROUND_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.PICK_UP_GROUND_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.PICK_UP_FEEDER:
		desiredElbowSetpoint = NRGArmSettings.PICK_UP_FEEDER_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.PICK_UP_FEEDER_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_LOW:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_LOW_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_LOW_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_MID:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_MID_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_MID_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_HIGH:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_HIGH_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_HIGH_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_SIDE_LOW:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_SIDE_LOW_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_SIDE_LOW_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_SIDE_MID:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_SIDE_MID_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_SIDE_MID_SHOULDER_SETPOINT;
		break;
	    case NRGArmSettings.SEEKING_SIDE_HIGH:
		desiredElbowSetpoint = NRGArmSettings.SEEKING_SIDE_HIGH_ELBOW_SETPOINT;
		desiredShoulderSetpoint = NRGArmSettings.SEEKING_SIDE_HIGH_SHOULDER_SETPOINT;
		break;
	}
    }

    public double getElbowSetpoint()
    {
	if (intermediateState != NRGArmSettings.MOVE_ELBOW)
	{
	    if (intermediateState == NRGArmSettings.MOVE_SHOULDER && Math.abs(shoulderPID.getError()) < NRGArmSettings.SHOULDER_MIN_ANGLE_ERROR)
	    {
		intermediateState = NRGArmSettings.MOVE_ELBOW;
		return desiredElbowSetpoint;
	    }
	    return NRGArmSettings.RETRACTED_ELBOW_SETPOINT;
	}
	else
	{
	    return desiredElbowSetpoint;
	}
    }

    public double getShoulderSetpoint()
    {
	if (intermediateState != NRGArmSettings.STORE_ELBOW)
	{
	    return desiredShoulderSetpoint;
	}
	else
	{
	    if (elbowPID.onTarget())
	    {
		intermediateState = NRGArmSettings.MOVE_SHOULDER;
		return desiredShoulderSetpoint;
	    }
	    return currentShoulderSetpoint;
	}
    }
}
