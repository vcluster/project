package vcluster.monitoring;

import java.util.concurrent.BlockingQueue;

import vcluster.engine.groupexecutor.ProxyExecutor;
import vcluster.global.Config;
import vcluster.global.QStatus;
import vcluster.monitoring.MonMessage.MonMsgType;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class QStatusChecker extends Thread {

	public QStatusChecker(int sec, BlockingQueue <MonMessage> queue) 
	{
		msgQueue = queue;
		
		if (sec <= 0) 
			sleepSec = Config.DEFAULT_SLEEP_SEC;
		else
			sleepSec = sec;
		
		done = false;
	}

	public void setSleepTime(int sec) 
	{
		sleepSec =  sec;
	}
	
	public int getSleepTime()
	{
		return sleepSec;
	}

	public void run() 
	{
		while(!done) {
			try {
				PrintMsg.print(DMsgType.MSG, "giving some delay for "+sleepSec+"....");
				Thread.sleep(sleepSec * 1000);
				PrintMsg.print(DMsgType.MSG, "checking q status....");
				getQStatus();
				
				/* engineering mode */
				//ENGgetQStatus();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void shutDwon() 
	{
		done = true;
	}
	
	private void incSleepSec() 
	{
		sleepSec += Config.SLEEP_SEC_INC;
		
		if (sleepSec > Config.MAX_SLEEP_SEC)
			sleepSec = Config.MAX_SLEEP_SEC;
	}

	
	private synchronized void getQStatus() 
	{
		try {
			ProxyExecutor.condor("condor_q");
			
			int runningJobs = QStatus.getRunningJob();
			int idleJobs = QStatus.getIdleJob();
			
			if (runningJobs <= 0) runningJobs = 1;
			ratio = (double) (idleJobs/(runningJobs));

			if (ratio > 1) {
				
				/* find the number of vms to be launched
				 * f: num of vms
				 * R: running jobs
				 * k: ratio (I/R), I is num of idle jobs
				 *  
				 * f = R * (k - 1) 
				 */
				
				double f = runningJobs * (ratio - 1);

				PrintMsg.print(DMsgType.MSG, "need to launch "+(int)f+" vms");

				MonMessage msg = new MonMessage(MonMsgType.QCHECKER, (int)f);
				
				PrintMsg.print(DMsgType.MSG, "message sent to monitoring manager");

				msgQueue.put(msg);
				
				/* sleep until notification message is arrived */
				this.wait();

				PrintMsg.print(DMsgType.MSG, "queue status checker woke up");
			} else {
				incSleepSec();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	private synchronized void ENGgetQStatus() 
	{

		double f = 2;
		
		MonMessage msg = new MonMessage(MonMsgType.QCHECKER, (int)f);
		
		try {
			
			
			System.out.println("QStatus: sending message, "+msg.toString());
			msgQueue.put(msg);
			/* sleep until notification message is arrived */
			System.out.println("QStatus: going to sleep....");
			this.wait();
			
			System.out.println("QStatus: woke up.");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	}

	
	public void wakeUp() {
		this.notify();
	}
	
	public double getRatio()
	{
		return ratio;
	}

	public void printQStatusChecker()
	{
		System.out.println("----------------------------------------");
		System.out.println("    Q Monitoring Configuration");
		System.out.println("----------------------------------------");
		System.out.println(" Interval : " + getSleepTime());
		System.out.println("    Ratio : " + getRatio());
		System.out.println("----------------------------------------");

	}
	
	
	private int sleepSec = 0;
	private boolean done = false;
	public double ratio = 0.0;
	
	private BlockingQueue <MonMessage> msgQueue;

	//public Double ratio = null;
}
