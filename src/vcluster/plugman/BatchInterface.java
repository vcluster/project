package vcluster.plugman;

import java.io.File;

/**
 * BatchInterface interface,all the batch system plug-in should implements this interface.
 *
 */
public interface BatchInterface{
	//connect to batch system,conf is a file includes the connection informations.
	public boolean ConnectTo(File conf);
	public boolean check_pool();
	public boolean check_q();
	public int getTotalJob();
	public int getIdleJob();
	public int getRunningJob();
	public int getHeldJob();


}
