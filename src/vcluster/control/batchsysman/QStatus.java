package vcluster.control.batchsysman;

public class QStatus {
	
	
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
	public int getTotalJob() {
		return totalJob;
	}
	public void setTotalJob(int totalJob) {
		this.totalJob = totalJob;
	}
	public int getCompletedJob() {
		return completedJob;
	}
	public void setCompletedJob(int completedJob) {
		this.completedJob = completedJob;
	}
	public int getRemovedJob() {
		return removedJob;
	}
	public void setRemovedJob(int removedJob) {
		this.removedJob = removedJob;
	}
	public int getIdleJob() {
		return idleJob;
	}
	public void setIdleJob(int idleJob) {
		this.idleJob = idleJob;
	}
	public int getRunningJob() {
		return runningJob;
	}
	public void setRunningJob(int runningJob) {
		this.runningJob = runningJob;
	}
	public int getHeldJob() {
		return heldJob;
	}
	public void setHeldJob(int heldJob) {
		this.heldJob = heldJob;
	}
	public int getSuspendedJob() {
		return suspendedJob;
	}
	public void setSuspendedJob(int suspendedJob) {
		this.suspendedJob = suspendedJob;
	}
	public boolean printQStatus()
	{
		System.out.println("----------------------------------------");
		System.out.println("Queue Status");
		System.out.println("----------------------------------------");
		System.out.println(" Total Jobs : " + getTotalJob());
		System.out.println();
		System.out.println("  Completed : " + completedJob);
		System.out.println("    Removed : " + removedJob);
		System.out.println("       Idle : " + getIdleJob());
		System.out.println("    Running : " + getRunningJob());
		System.out.println("       Held : " + getHeldJob());
		System.out.println("  Suspended : " + suspendedJob);
		System.out.println("----------------------------------------");
		return true;
	}
	
	
	private int totalJob = 0;
	private int completedJob = 0;
	private int removedJob = 0;
	private int idleJob = 0;
	private int runningJob = 0;
	private int heldJob = 0;
	private int suspendedJob = 0;
	
}
