package vcluster.control.cloudman;

import java.util.List;

import vcluster.global.Config.CloudType;

public class CloudElement{
	
	protected CloudElement(List<String> configurations) {
		conf = configurations;
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
	
	
	
	public List<String> getConf() {
		return conf;
	}

	public void setConf(List<String> conf) {
		this.conf = conf;
	}

	public void dump(){
		for(String aLine : conf){
			System.out.println(aLine);
		}
	}

	public CloudType getCloudType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String stringCloudType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean createVM() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean listVMs() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

	private List<String> conf;
	private int currentVMs;
	private int cloudType;
	
}
