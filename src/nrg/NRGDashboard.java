/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package nrg;

import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Provide a method to update dashboard data.
 * @author dtjones, Dustin Lee
 */
public class NRGDashboard
{
    private Solenoid sole;

    public NRGDashboard()
    {

	sole = new Solenoid(8, 1);
	//Timer dashTimer = new Timer();
	//dashTimer.schedule(new TimerTask()
	//{
	//public void run()
	//{
	//updateDefaultDashboard();
	//}
	//}, 0, 100);

    }

    void updateDefaultDashboard()
    {
	Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
	lowDashData.addCluster();
	{     //analog modules
	    {
		lowDashData.addCluster();
		{
		    for (int i = 1; i <= 8; i++)
		    {
			lowDashData.addFloat((float) AnalogModule.getInstance(1).getAverageVoltage(i));
		    }
		}
		lowDashData.finalizeCluster();
		lowDashData.addCluster();
		{
		    for (int i = 1; i <= 8; i++)
		    {
			lowDashData.addFloat((float) AnalogModule.getInstance(2).getAverageVoltage(i));
		    }
		}
		lowDashData.finalizeCluster();
	    }
	    lowDashData.finalizeCluster();

	    lowDashData.addCluster();
	    { //digital modules
		lowDashData.addCluster();
		{
		    lowDashData.addCluster();
		    {
			int module = 4;
			lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
			lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
			lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
			lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
			lowDashData.addCluster();
			{
			    for (int i = 1; i <= 10; i++)
			    {
				lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
			    }
			}
			lowDashData.finalizeCluster();
		    }
		    lowDashData.finalizeCluster();
		}
		lowDashData.finalizeCluster();

		lowDashData.addCluster();
		{
		    lowDashData.addCluster();
		    {
			int module = 6;
			lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
			lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
			lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
			lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
			lowDashData.addCluster();
			{
			    for (int i = 1; i <= 10; i++)
			    {
				lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
			    }
			}
			lowDashData.finalizeCluster();
		    }
		    lowDashData.finalizeCluster();
		}
		lowDashData.finalizeCluster();

	    }
	    lowDashData.finalizeCluster();

	    //lowDashData.addByte(Solenoid.getAll());
	    lowDashData.addByte(sole.getAll());
	}
	lowDashData.finalizeCluster();
	lowDashData.commit();
    }
    Date time = new Date();
}
