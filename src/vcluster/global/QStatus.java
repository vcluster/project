package vcluster.global;

import java.util.StringTokenizer;

import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class QStatus {

	public static boolean extractInfo(String qStatus)
	{
		StringTokenizer st = new StringTokenizer(qStatus, ";, ");
		
		int numJob = 0;
		String token = null;
		
		if (!st.hasMoreTokens()) {
    		PrintMsg.print(DMsgType.ERROR, "no more token.");
			return false;
		}
		
		/* get total jobs */
		token = st.nextToken();

		numJob = Integer.parseInt(token);
		if (!st.hasMoreTokens()) return false;
		if (!st.nextToken().equalsIgnoreCase("jobs")) {
    		PrintMsg.print(DMsgType.ERROR, "token, jobs, expected, but not");
			return false;
		}
		totalJob = numJob;
		
		if (!st.hasMoreTokens()) {
    		PrintMsg.print(DMsgType.ERROR, "no more token");
			return false;
		}

		/* get idle jobs */
		token = st.nextToken();

		numJob = Integer.parseInt(token);

		if (!st.hasMoreTokens()) return false;
		if (!st.nextToken().equalsIgnoreCase("idle")) {
    		PrintMsg.print(DMsgType.ERROR, "token, idle, expected, but not");
			return false;
		}
		idleJob = numJob;


		
		if (!st.hasMoreTokens()) {
    		PrintMsg.print(DMsgType.ERROR, "no more token");
			return false;
		}

		/* get running jobs */
		token = st.nextToken();
		numJob = Integer.parseInt(token);

		if (!st.hasMoreTokens()) return false;
		if (!st.nextToken().equalsIgnoreCase("running")) {
    		PrintMsg.print(DMsgType.ERROR, "token, running, expected, but not");
			return false;
		}
		runningJob = numJob;

		/* get held jobs */
		token = st.nextToken();
		numJob = Integer.parseInt(token);

		if (!st.hasMoreTokens()) return false;
		if (!st.nextToken().equalsIgnoreCase("held")) {
    		PrintMsg.print(DMsgType.ERROR, "token, held, expected, but not");
			return false;
		}
		heldJob = numJob;
		
		
		/* check the number of jobs */
		numJob = idleJob + runningJob + heldJob;
		
		if (totalJob != numJob) {
    		PrintMsg.print(DMsgType.ERROR, "Number of jobs does not mache, total="
    				+totalJob+", sum="+numJob);
			return false;
		}

		return true;
	}
	
	public static int getTotalJob()
	{
		return totalJob;
	}
	
	public static int getIdleJob()
	{
		return idleJob;
	}
	
	public static int getRunningJob()
	{
		return runningJob;
	}
	
	public static int getHeldJob()
	{
		return heldJob;
	}
	
	public static double getRatio()
	{
		return runningJob/(idleJob + 1); 
	}
	
	public static void printQStatus()
	{
		System.out.println("----------------------------------------");
		System.out.println("Queue Status");
		System.out.println("----------------------------------------");
		System.out.println(" Total Jobs : " + getTotalJob());
		System.out.println();
		System.out.println("       Idle : " + getIdleJob());
		System.out.println("    Running : " + getRunningJob());
		System.out.println("       Held : " + getHeldJob());
		System.out.println("----------------------------------------");
	}
	
	
	private static int totalJob = 0;
	private static int idleJob = 0;
	private static int runningJob = 0;
	private static int heldJob = 0;
	
}
