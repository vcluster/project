package vcluster.control.batchsysman;

import java.util.ArrayList;

public class PoolStatus {
	
	
	
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

	public void setUnClaimedSlot(int unClaimedSlot) {
		this.unClaimedSlot = unClaimedSlot;
	}

	public int getUnClaimedSlot() {
		return unClaimedSlot;
	}

	public void setTotalSlot(int totalSlot) {
		this.totalSlot = totalSlot;
	}

	public int getTotalSlot()
	{
		return totalSlot;
	}

	public void setOwnerSlot(int ownerSlot) {
		this.ownerSlot = ownerSlot;
	}

	public int getOwnerSlot()
	{
		return ownerSlot;
	}

	public void setClaimedSlot(int claimedSlot) {
		this.claimedSlot = claimedSlot;
	}

	public int getClaimedSlot()
	{
		return claimedSlot;
	}

	public void setMatchedSlot(int matchedSlot) {
		this.matchedSlot = matchedSlot;
	}

	public int getMatchedSlot()
	{
		return matchedSlot;
	}

	public void setPreemptingSlot(int preemptingSlot) {
		this.preemptingSlot = preemptingSlot;
	}

	public int getPreemptingSlot()
	{
		return preemptingSlot;
	}

	public void setBackfillSlot(int backfillSlot) {
		this.backfillSlot = backfillSlot;
	}

	public int getBackfillSlot()
	{
		return backfillSlot;
	}

	public ArrayList<Slot> getSlotList() {
		return slotList;
	}

	public void setSlotList(ArrayList<Slot> slotList) {
		this.slotList = slotList;
	}

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
