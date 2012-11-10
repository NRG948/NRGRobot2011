package nrg;

/**
 * @author Eric, Austin
 */
public class Debug
{
    public static final boolean FATAL_EXCEPTIONS = true;
    public static final boolean ROBOT_MAIN_ROUTINES = true;

    public static final boolean DRIVE = false;
    public static final boolean AUTONOMOUS_ROUTINES = false;
    public static final boolean GYRO = false;
    public static final boolean LINE_FOLLOWING = false;

    public static final boolean MAE = false;
    public static final boolean ULTRASONIC = false;
    public static final boolean GRIPPER = false;

    public static final boolean ELBOW = true;
    public static final boolean SHOULDER = true;
    public static final boolean ELBOW_PID = true;
    public static final boolean SHOULDER_PID = true;
    public static final boolean IR = false;
    public static final boolean CMDDISPATCH = true;
    public static final boolean WALL_TRACKER = false;
    public static final boolean MINIBOT = false;
    public static final boolean TENSIONING = false;
    public static final boolean SONAR = false;

    /**
     * This method can be switched on or off with the class' boolean settings (for debug purposes).
     * @param debugEnabled
     * @param s
     */
    public static void println(boolean debugEnabled, String s)
    {
	if (debugEnabled)
	    System.out.println(s);
    }

    public static void print(boolean debugEnabled, String s)
    {
	if (debugEnabled)
	    System.out.print(s);
    }

    // argc - argument count, args - arguments
    // format of str: "argument 0=%0 argument 1=%1" etc, %n corresponds to args[n]
    public static void printRound(boolean debugEnabled, String str, double[] args)
    {
	if (!debugEnabled)
	    return;

	String newString = str;

	for (int i = 0; i < args.length; i++)
	{
	    String searchString = "%" + i;

	    newString = replaceAll(newString, searchString, Double.toString(MathHelper.round(args[i], 2)));
	}

	System.out.print(newString);
    }

    public static void printRoundln(boolean debugEnabled, String str, double[] args)
    {
	if (!debugEnabled)
	    return;

	String newString = str;

	for (int i = 0; i < args.length; i++)
	{
	    String searchString = "%" + i;

	    newString = replaceAll(newString, searchString, Double.toString(MathHelper.round(args[i], 2)));
	}

	System.out.println(newString);
    }

    private static String replaceAll(String str, String searchString, String replaceString)
    {
	String newString = str;

	int index = newString.indexOf(searchString);

	while (index >= 0)
	{
	    String str1, str2;

	    str1 = newString.substring(0, index);
	    str2 = newString.substring(index + searchString.length());

	    newString = str1 + replaceString + str2;

	    index = newString.indexOf(searchString);
	}

	return newString;
    }
}
