package vcluster.monitoring;

public class MonMessage {

	public MonMessage(MonMsgType aType) {
		msgType = aType;
		privateData = -1;
	}

	public MonMessage(MonMsgType aType, int aData) {
		msgType = aType;
		privateData = aData;
	}
	
	public MonMsgType getMessageType() {
		return msgType;
	}
	
	public int getPrivateData() {
		return privateData;
	}
	
	public String toString() {
		switch(msgType) {
		case QCHECKER: return "QCHECKER";
		case CLOUD: return "CLOUD";
		case VMMAN: return "VMMAN";
		}
		
		return null;
	}
	
	
	/* monitoring message */ 
	public enum MonMsgType {QCHECKER, CLOUD, VMMAN}; 
	private MonMsgType msgType;
	
	/* just for private data which is used 
	 * if calculated data has to be recalculated in a calling function */
	private int privateData;

}
