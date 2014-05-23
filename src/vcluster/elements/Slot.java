package vcluster.elements;
/**
 * A class representing a slot of the batch system.
 *
 */
public class Slot {
	private String domain;
	private String identifier;
	private IdType idType;
	private int activity;
	

	/**
	 *Get the domain of the slot, identify where(or which cloud) is the slot from. 
	 * @return the domain of the slot.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 *Set the domain of the slot.
	 *@param domain, the domain of the slot 
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 *Get the identifier of the slot, 
	 *cloud be a string of IP address, or virtual machine's ID, etc.
	 *@return the identifier of the slot.  
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *Set the identifier of the slot.
	 *@return a string of the identifier type of the slot. 
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *Get the identifier type of the slot, 
	 *for example: IP address, virtual machine's ID
	 *@return the identifier type of the slot.  
	 */
	public IdType getIdType() {
		return idType;
	}

	/**
	 *Set the identifier type of the slot.
	 *@return the identifier type of the slot. 
	 */
	public void setIdType(IdType idType) {
		this.idType = idType;
	}


	/**
	 *Get the activity status of a slot
	 *There are three kinds of status for a slot
	 *0 represents the slot is idle
	 *1 represents the slot is busy
	 *3 represents the status is not determined.
	 *@return the activity status of a slot.  
	 */	
	public int getActivity() {
		return activity;
	}

	/**
	 *Set the activity status of a slot
	 *@param activity, only can be one of the number "0, 1, 3" .
	 */
	public void setActivity(int activity) {
		this.activity = activity;
	}
	
	/**
	 *An enum to define the identifier types of a slot. 
	 *Five types is defined, which are "PRIVATEIP, PUBLICIP, VMID, VMHOSTNAME,NOTDEFINED ".
	 */
	public static enum IdType {PRIVATEIP, PUBLICIP, VMID, VMHOSTNAME,NOTDEFINED };
		
}
