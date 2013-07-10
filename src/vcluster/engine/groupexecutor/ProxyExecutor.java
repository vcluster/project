package vcluster.engine.groupexecutor;

/**
 * ProxyExecutor interface,all the batch system plug-in should implements this interface.
 *
 */
public interface ProxyExecutor {

	public boolean check_pool();
	public boolean check_q();
	public boolean condor(String cmdLine);
	public boolean onevm(String cmdLine);
	public int getTotalJob();
	public int getIdleJob();
	public int getRunningJob();
	public int getHeldJob();
	public double getRatio();
	public String getInfo();

}
