package vcluster.control.batchsysman;

public class Slot {
	private String domain;
	private String identifier;
	private IdType idType;
	private int activity;
	
	
	
	
	public String getDomain() {
		return domain;
	}




	public void setDomain(String domain) {
		this.domain = domain;
	}




	public String getIdentifier() {
		return identifier;
	}




	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}




	public IdType getIdType() {
		return idType;
	}




	public void setIdType(IdType idType) {
		this.idType = idType;
	}




	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public static enum IdType {PRIVATEIP, PUBLICIP, VMID, VMHOSTNAME,NOTDEFINED };
		
}
