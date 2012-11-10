package nrg;

/**
 *
 * @author Irving
 */
public class NRGIRSettings
{
    public static final int IR_LEFT_CHANNEL = 6;
    public static final int IR_RIGHT_CHANNEL = 7;
    //this is roughly what we saw during testing
    //public static final double MIN = 0.4;   // TODO: calibrate these values
    public static final double MAX = 40;   // MIN is value at 0 inches, MAX at 40 inches
    public static final double CAL_CONSTANT_M = 0.0681;
    public static final double CAL_CONSTANT_B = 0.1561;
}
