package nrg;

import edu.wpi.first.wpilibj.Gyro;

/**
 * All angles are in degrees.
 * @author Dustin
 */
public class NRGGyro extends Gyro
{
    private int offset;
    private double desiredHeading;

    public NRGGyro(int offsetDegrees)
    {
	super(NRGDriveSettings.GYRO_SLOT, NRGDriveSettings.GYRO_DATA_CHANNEL);
	offset = offsetDegrees;
	desiredHeading = offset;
    }

    public double getHeading()
    {
	return super.getAngle() + getOffset();
    }

    public int getOffset()
    {
	return offset;
    }

    public void setOffset(int offset)
    {
	this.offset = offset;
    }

    public double getDesiredHeading()
    {
	return desiredHeading;
    }

    public void setDesiredHeading(double desiredHeading)
    {
	this.desiredHeading = desiredHeading;
    }

    public void reset()
    {
	super.reset();
	desiredHeading = offset;
    }

    public void rotate()
    {
	if (offset == 0)
	{
	    offset = 180;
	    desiredHeading = 180;
	}
	else
	{
	    offset = 0;
	    desiredHeading = 0;
	}
    }

    public void setGyroStandard()
    {
	offset = 180;
	reset();
    }

    public void setGyroReverse()
    {
	offset = 0;
	reset();
    }
}
