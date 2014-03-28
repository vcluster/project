package vcluster.control.batchsysman;

import java.util.ArrayList;
/**
 * A class representing the pool status of the batch system.
 *
 */
public class PoolStatus {

	
	/**
	 * Constructor,initialize the pool status 	 
	 */
	public PoolStatus(int totalSlot, int ownerSlot,
			int claimedSlot, int unClaimedSlot, int matchedSlot,
			int preemptingSlot, int backfillSlot) {
		super();
		this.totalSlot = totalSlot;
		this.ownerSlot = ownerSlot;
		this.claimedSlot = claimedSlot;
		this.unClaimedSlot = unClaimedSlot;
		this.matchedSlot = matchedSlot;
		this.preemptingSlot = preemptingSlot;
		this.backfillSlot = backfillSlot;
	}
/**
 *Constructor, not initialize anything of the pool status 
 * 
 */
	public PoolStatus() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 *Set the number of unclaimed slots
	 *@param unClaimedSlot, the number of unclaimed slots 
	 */
	public void setUnClaimedSlot(int unClaimedSlot) {
		this.unClaimedSlot = unClaimedSlot;
	}
	/**
	 * Get the number of nuclaimed slots
	 * @return the number of unclaimed slots
	 */
	public int getUnClaimedSlot() {
		return unClaimedSlot;
	}

	
	/**
	 *Set the number of  total  slots 
	 * @param totalSlot ,the number of totalSlot.
	 */
	public void setTotalSlot(int totalSlot) {
		this.totalSlot = totalSlot;
	}

	/**
	 *Get the number of total slots.
	 *@return the number of total slots. 
	 */
	public int getTotalSlot()
	{
		return totalSlot;
	}

	/**
	 *Set the number of owner slots
	 *@param ownerSlot, the number of owner slotsf 
	 * 
	 */
	public void setOwnerSlot(int ownerSlot) {
		this.ownerSlot = ownerSlot;
	}
	
	

	/**
	 *Get the number of owner slots
	 *@return the number of owner slots; 
	 */
	public int getOwnerSlot()
	{
		return ownerSlot;
	}

	/**
	 *Set the number of Claimed slots.
	 *@param the number of claimed slots. 
	 */
	public void setClaimedSlot(int claimedSlot) {
		this.claimedSlot = claimedSlot;
	}

	
	/**
	 *Get the number of Claimed slots.
	 *@return the number of claimed slots. 
	 */
	public int getClaimedSlot()
	{
		return claimedSlot;
	}

	/**
	 * Set the number of Matched slots
	 *@param matchedSlot ,the number of matched slots. 
	 */
	public void setMatchedSlot(int matchedSlot) {
		this.matchedSlot = matchedSlot;
	}

	/**
	 *Get the  the number of Matched slots.
	 *@return the number of Matched slots.
	 * 
	 */
	public int getMatchedSlot()
	{
		return matchedSlot;
	}

	/**
	 *Set the number of preempting slots .
	 *@param preemptingSlot,the number of preempting slots.
	 */
	public void setPreemptingSlot(int preemptingSlot) {
		this.preemptingSlot = preemptingSlot;
	}

	/**
	 *Get the number of preempting slots.
	 *@return the number of preempting slots. 
	 */
	public int getPreemptingSlot()
	{
		return preemptingSlot;
	}

	/**
	 *Set the number of Backfill slots .
	 *@param Backfill,the number of Backfill slots.
	 */
	public void setBackfillSlot(int backfillSlot) {
		this.backfillSlot = backfillSlot;
	}

	/**
	 *Get the number of Backfill slots.
	 *@return the number of Backfill slots. 
	 */
	public int getBackfillSlot()
	{
		return backfillSlot;
	}

	/**
	 *Get a list of all of the slots.
	 *@return  An array list of slots 
	 */
	public ArrayList<Slot> getSlotList() {
		return slotList;
	}

	/**
	 *Set a list of all of the slots.
	 *@param  An array list of slots 
	 */
	public void setSlotList(ArrayList<Slot> slotList) {
		this.slotList = slotList;
	}

	
	/**
	 *A function to output the pool status,for command line console,print the result on screen. 
	 */
	public boolean printPoolStatus()
	{
		System.out.println("----------------------------------------");
		System.out.println("Pool Status");
		System.out.println("----------------------------------------");
		System.out.println(" Total Slots : " + getTotalSlot());
		System.out.println();
		System.out.println("       Owner : " + getOwnerSlot());
		System.out.println("     Claimed : " + getClaimedSlot());
		System.out.println("   Unclaimed : " + getUnClaimedSlot());
		System.out.println("     Matched : " + getMatchedSlot());
		System.out.println("  Preempting : " + getPreemptingSlot());
		System.out.println("    Backfill : " + getBackfillSlot());
		System.out.println("----------------------------------------");
		return true;
	}

	
	private ArrayList<Slot> slotList;
	private int totalSlot = 0;
	private int ownerSlot = 0;
	private int claimedSlot = 0;
	private int unClaimedSlot = 0;
	private int matchedSlot = 0;
	private int preemptingSlot = 0;
	private int backfillSlot = 0;
	
}
