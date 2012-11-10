package nrg;

/**
 * @author Stephen
 */
public class NRGInputAverager
{
    double[] arr;
    int index;
    double sum;

    public NRGInputAverager(int length)
    {
	arr = new double[length];
	index = 0;
	sum = 0.0;
    }

    public void addValue(double value)
    {
	index += 1;
	index %= arr.length;
	sum += value - arr[index];
	arr[index] = value;
    }

    public double getAverageValue()
    {
	return sum / arr.length;
    }

    // Gives a double weight to the most recently added value -- to help with smoothing
    public double getAverageValueWeighted()
    {
	return (sum + arr[index]) / (arr.length + 1);
    }
}
