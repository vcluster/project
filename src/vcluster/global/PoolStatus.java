package vcluster.global;

import java.util.StringTokenizer;

public class PoolStatus {

	public static boolean extractInfo(String poolStatus)
	{
		StringTokenizer st = new StringTokenizer(poolStatus, " \t");
		
		int numSlot = 0;
		String token = null;
		
		if (!st.hasMoreTokens()) {
			System.out.println("[Error] : no more token");
			return false;
		}
		
		/*
		 * 
		 *         Total Owner Claimed Unclaimed Matched Preempting Backfill
		 *
		 *   Total    16     2       0        14       0          0        0
		 */

		/* skip token Total */
		token = st.nextToken();
		// System.out.println("token = "+token);

		if (!st.hasMoreTokens()) return false;

		/* get total */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		totalSlot = numSlot;
		if (!st.hasMoreTokens()) return false;

		/* get owner */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		ownerSlot = numSlot;
		if (!st.hasMoreTokens()) return false;
		

		/* get claimed */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		claimedSlot = numSlot;
		if (!st.hasMoreTokens()) return false;
	

		/* get unclaimed */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		unClaimedSlot = numSlot;
		if (!st.hasMoreTokens()) return false;
		

		/* get matched */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		matchedSlot = numSlot;
		if (!st.hasMoreTokens()) return false;
		

		/* get preempting */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		preemptingSlot = numSlot;
		if (!st.hasMoreTokens()) return false;


		/* get backfill */
		token = st.nextToken();
		//System.out.println("Token = "+token);
		numSlot = Integer.parseInt(token);
		backfillSlot = numSlot;
		if (!st.hasMoreTokens()) return false;

		
		/* check the number of jobs */
		numSlot = ownerSlot + claimedSlot + unClaimedSlot + matchedSlot + preemptingSlot + backfillSlot;
		
		if (totalSlot != numSlot) {
			System.out.println("[Error] : Number of jobs does not mache");
			System.out.println("        : Total = "+totalSlot+", Sum = "+numSlot);
			return false;
		}

		return true;
	}
	
	public static int getTotalSlot()
	{
		return totalSlot;
	}
	
	public static int getOwnerSlot()
	{
		return ownerSlot;
	}
	
	public static int getClaimedSlot()
	{
		return claimedSlot;
	}
	
	public static int getUnclaimedSlot()
	{
		return unClaimedSlot;
	}
	
	public static int getMatchedSlot()
	{
		return matchedSlot;
	}
	
	public static int getPreemptingSlot()
	{
		return preemptingSlot;
	}

	
	public static int getBackfillSlot()
	{
		return backfillSlot;
	}

	public static void printQStatus()
	{
		System.out.println("----------------------------------------");
		System.out.println("Pool Status");
		System.out.println("----------------------------------------");
		System.out.println(" Total Slots : " + getTotalSlot());
		System.out.println();
		System.out.println("       Owner : " + getOwnerSlot());
		System.out.println("     Claimed : " + getClaimedSlot());
		System.out.println("   Unclaimed : " + getUnclaimedSlot());
		System.out.println("     Matched : " + getMatchedSlot());
		System.out.println("  Preempting : " + getPreemptingSlot());
		System.out.println("    Backfill : " + getBackfillSlot());
		System.out.println("----------------------------------------");
	}

	private static int totalSlot = 0;
	private static int ownerSlot = 0;
	private static int claimedSlot = 0;
	private static int unClaimedSlot = 0;
	private static int matchedSlot = 0;
	private static int preemptingSlot = 0;
	private static int backfillSlot = 0;
	
}
