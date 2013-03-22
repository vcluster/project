package vcluster.control;

public class VMMessage {
	public VMMessage(VMMsgType aType, VMExeType aExeType) {
		msgType = aType;
		exeType = aExeType;
		privateData = -1;
	}

	public VMMessage(VMMsgType aType, VMExeType aExeType, int aData) {
		msgType = aType;
		exeType = aExeType;
		privateData = aData;
	}
	
	public VMMsgType getMessageType() {
		return msgType;
	}
	
	public VMExeType getExeType() {
		return exeType;
	}
	
	public int getPrivateData() {
		return privateData;
	}
	
	public String toString() {
		String ret = "";
		switch(msgType) {
		case LAUNCH: ret += "LAUNCH";
		break;
		case TERMINATE: ret += "TERMINATE";
		break;
		case SHUTDOWN: ret += "SHUTDOWN";
		break;
		case NOT_DEFINED: ret += "NOT DEFINED";
		break;
		}
		
		switch(exeType) {
		case OCA: return ret += " Cloud API";
		case REST: return ret += " REST API";
		case NOT_DEFINED: ret += " NOT DEFINED";
		}
		
		return null;
	}
	
	
	/* vm related messages */ 
	public enum VMMsgType {LAUNCH, STOP, SUSPEND, RESUME, TERMINATE, SHUTDOWN, 
		                   DELETE, NOT_DEFINED}; 
	public enum VMExeType {OCA, REST, SHELL, NOT_DEFINED}; 

	private VMMsgType msgType;
	private VMExeType exeType;
	
	/* used in case that calculated data has to be recalculated 
	 * in a calling function */
	private int privateData;

}
