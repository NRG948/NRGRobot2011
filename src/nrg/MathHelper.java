package nrg;

import com.sun.squawk.util.MathUtils;

/**
 * Contains helpful math methods.
 * @author Dustin, Eric
 */
public class MathHelper
{
    public static double round(double x, int digits)
    {
	int num = 1;
	for (int i = 0; i < digits; i++)
	{
	    num *= 10;
	}
	return ((double) (MathUtils.round(x * num))) / num;
    }

    /**
     * If the given value is greater than max, max is returned.
     * If the value is less than min, min is returned.
     * Otherwise the given value is returned.
     */
    public static double clamp(double val, double min, double max)
    {
	if (val > max)
	    return max;
	if (val < min)
	    return min;
	return val;
    }

    public static double clamp(double val, double maxAbs)
    {
	return clamp(val, -maxAbs, maxAbs);
    }

    public static int clamp(int val, int min, int max)
    {
	if (val > max)
	    return max;
	if (val < min)
	    return min;
	return val;
    }

    /**
     * This is a method for evaluating exponential expressions
     * @param val the base
     * @param exp the exponent. precondition: it must be positive
     * @return the result of val^exp
     */
    public static double pow(double val, int exp)
    {
	double result = 1.0;
	for (int i = 0; i <= exp; i++)
	{
	    result *= val;
	}
	return result;
    }
}
