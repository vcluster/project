package vcluster.elements;
/**
 * A class representing the job queue status of the batch system.
 *
 */
public class QStatus {

	/**
	 *The constructor, to initialize the QStatus. 
	 */
	public QStatus(int totalJob, int completedJob, int removedJob, int idleJob,
			int runningJob, int heldJob, int suspendedJob) {
		super();
		this.totalJob = totalJob;
		this.completedJob = completedJob;
		this.removedJob = removedJob;
		this.idleJob = idleJob;
		this.runningJob = runningJob;
		this.heldJob = heldJob;
		this.suspendedJob = suspendedJob;
	}
	
	/**
	 * Get the number of total jobs
	 *@return the number of total jobs	 * 
	 */
	public int getTotalJob() {
		return totalJob;
	}
	
	/**
	 *Set the number of total jobs
	 *@param totaljob, the number of total jobs. 
	 */
	public void setTotalJob(int totalJob) {
		this.totalJob = totalJob;
	}
	
	/**
	 * Get the number of completed jobs
	 *@return the number of completed jobs	 * 
	 */
	public int getCompletedJob() {
		return completedJob;
	}
	
	/**
	 *Set the number of completed jobs
	 *@param completedJob, the number of completed jobs. 
	 */
	public void setCompletedJob(int completedJob) {
		this.completedJob = completedJob;
	}
	
	/**
	 * Get the number of removed jobs
	 *@return the number of removed jobs	 * 
	 */
	public int getRemovedJob() {
		return removedJob;
	}
	
	/**
	 *Set the number of removed jobs
	 *@param removedJob, the number of removed jobs. 
	 */
	public void setRemovedJob(int removedJob) {
		this.removedJob = removedJob;
	}
	/**
	 * Get the number of idle jobs
	 *@return the number of idle jobs	 * 
	 */
	public int getIdleJob() {
		return idleJob;
	}
	
	/**
	 *Set the number of idle jobs
	 *@param idleJob, the number of idle jobs. 
	 */
	public void setIdleJob(int idleJob) {
		this.idleJob = idleJob;
	}
	
	/**
	 * Get the number of running jobs
	 *@return the number of running jobs	 * 
	 */
	public int getRunningJob() {
		return runningJob;
	}
	
	/**
	 *Set the number of running jobs
	 *@param runningdJob, the number of running jobs. 
	 */
	public void setRunningJob(int runningJob) {
		this.runningJob = runningJob;
	}
	
	/**
	 * Get the number of held jobs
	 *@return the number of held jobs	 * 
	 */
	public int getHeldJob() {
		return heldJob;
	}
	
	/**
	 *Set the number of held jobs
	 *@param heldJob, the number of held jobs. 
	 */
	public void setHeldJob(int heldJob) {
		this.heldJob = heldJob;
	}
	
	/**
	 * Get the number of suspended jobs
	 *@return the number of suspended jobs	 * 
	 */
	public int getSuspendedJob() {
		return suspendedJob;
	}
	
	/**
	 *Set the number of suspended jobs
	 *@param suspendedJob, the number of suspended jobs. 
	 */
	public void setSuspendedJob(int suspendedJob) {
		this.suspendedJob = suspendedJob;
	}
	
	/**
	 *This is a function the output the job queue status,for command line console,print the result on the screeen. 
	 */
	public String printQStatus()
	{
		StringBuffer str = new StringBuffer();
		
		str.append("----------------------------------------"+System.getProperty("line.separator"));
		str.append("Queue Status"+System.getProperty("line.separator"));
		str.append("----------------------------------------"+System.getProperty("line.separator"));
		str.append(" Total Jobs : " + getTotalJob()+System.getProperty("line.separator"));
		str.append(""+System.getProperty("line.separator"));
		str.append("  Completed : " + completedJob+System.getProperty("line.separator"));
		str.append("    Removed : " + removedJob+System.getProperty("line.separator"));
		str.append("       Idle : " + getIdleJob()+System.getProperty("line.separator"));
		str.append("    Running : " + getRunningJob()+System.getProperty("line.separator"));
		str.append("       Held : " + getHeldJob()+System.getProperty("line.separator"));
		str.append("  Suspended : " + suspendedJob+System.getProperty("line.separator"));
		str.append("----------------------------------------"+System.getProperty("line.separator"));
		return str.toString();
	}
	
	
	private int totalJob = 0;
	private int completedJob = 0;
	private int removedJob = 0;
	private int idleJob = 0;
	private int runningJob = 0;
	private int heldJob = 0;
	private int suspendedJob = 0;
	
}
