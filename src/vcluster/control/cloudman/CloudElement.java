package vcluster.control.cloudman;

import vcluster.global.Config.CloudType;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class CloudElement {
	
	protected CloudElement() {
		cloudType = CloudType.NOT_DEFINED;
		endPoint = null;
		maxVMs = -1;
		currentVMs = 0;
		accessKey = null;
		secretKey = null;
		keyName = null;
		instanceType = null;
		imageName = null;
	}
	
	public int getCurrentVMs() {
		return currentVMs;
	}
	
	public void setCurrentVMs(int vms) {
		currentVMs = vms;
	}
	
	protected void incCurrentVMs(int vms) {
		currentVMs += vms;
	}
	
	public int getMaxVMs() {
		return maxVMs;
	}
	
	protected void setMaxVMs(int vms) {
		maxVMs = vms;
	}
	
	public String getEndPoint() {
		return endPoint;
	}
	
	protected void setEndPoint(String point) {
		endPoint = point;
		
		point = point.replaceAll("https://", "");
		point = point.replaceAll("http://", "");
		point = point.replaceAll("/", "");
		shortEndPoint = point.trim();

	}
	
	public String getShortEndPoint() {
		return shortEndPoint;
	}
	
	public CloudType getCloudType() {
		return cloudType;
	}
	
	protected void setCloudType(CloudType type) {
		cloudType = type;
	}
	
	protected void setCloudType(String type) {
		if (type.equalsIgnoreCase("private")) 
			cloudType = CloudType.PRIVATE;
		else if (type.equalsIgnoreCase("public")) 
			cloudType = CloudType.PUBLIC;
		else {
			PrintMsg.print(DMsgType.ERROR, "undefined type, "+type+", found");
			cloudType = CloudType.NOT_DEFINED;
		}
		
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	
	protected void setAccessKey (String key) {
		accessKey = key;
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	
	protected void setSecretKey(String key) {
		secretKey = key;
	}
	
	public String getInstaceType() {
		return instanceType;
	}

	protected void setInstanceType(String type) {
		instanceType = type;
	}
	
	public String getKeyName() {
		return keyName;
	}
	
	protected void setKeyName(String name) {
		keyName = name;
	}

	protected void setImageName(String name) {
		imageName = name;
	}
	
	public String getImageName() {
		return imageName;
	}

	protected void setVersion(String v) {
		this.version = v;
	}
	
	public String getVersion() {
		return version;
	}
	
	protected void setSignatureVersion(String v) {
		signatureVersion = v;
	}
	
	public String getSignatureVersion() {
		return signatureVersion;
	}
	
	protected void setSignatureMethod(String method) {
		signatureMethod = method;
	}
	
	public String getSignatureMethod() {
		return signatureMethod;
	}
	
	public String stringCloudType() {
		
		switch(cloudType) {
		case PRIVATE: return "PRIVATE";
		case PUBLIC: return "PUBLIC";
		case NOT_DEFINED: return "NOT_DEFINED";
		}
		return "NOT_DEFINED";
	}

	public void dump() {
		
		System.out.printf("\tcloud type:\t%s\n", cloudType);
		System.out.printf("\tend point:\t%s\n", endPoint);
		System.out.printf("\tshort point:\t%s\n", shortEndPoint);
		System.out.printf("\tmax vms:\t%d\n", maxVMs);
		System.out.printf("\tcur vms:\t%d\n", currentVMs);
		System.out.printf("\taccess key:\t%s\n", accessKey);
		System.out.printf("\tsecret key:\t%s\n", secretKey);
		System.out.printf("\tkey name:\t%s\n", keyName);
		System.out.printf("\timage name:\t%s\n", imageName);
		System.out.printf("\tinstance type:\t%s\n", instanceType);
		System.out.printf("\tversion:\t%s\n", version);
		System.out.printf("\tsignature version:\t%s\n", signatureVersion);
		System.out.printf("\tsignature method:\t%s\n", signatureMethod);
		
	}

	
	private CloudType cloudType;
	private String endPoint;
	private String shortEndPoint;
	private int maxVMs;
	private int currentVMs;
	private String accessKey;
	private String secretKey;
	private String keyName;
	private String instanceType;
	private String imageName;
	
	private String version;
	private String signatureVersion;
	private String signatureMethod;

}
