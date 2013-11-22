package vcluster.control.cloudman;

import java.util.TreeMap;

import vcluster.control.*;

public class Host {
	private TreeMap<String,VMelement> vmList;
	private String cloudName;
	private int maxVmNum;
	private int currVmNum;
	private int remainingVmNum;
	private String id;
	
	
	public int getRemainingVmNum() {
		return remainingVmNum;
	}

	public void setRemainingVmNum() {
		this.remainingVmNum = maxVmNum-currVmNum;
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public Host(int maxVmNum, String id,String cloudName) {
		this.maxVmNum = maxVmNum;
		this.id = id;
		this.cloudName = cloudName;
		vmList = new TreeMap<String,VMelement>();
	}
	
	public TreeMap<String, VMelement> getVmList() {
		vmList = new TreeMap<String,VMelement> ();
		for(VMelement vm : CloudManager.getCloudList().get(cloudName).getVmList().values()){
			if(vm.getHostname().equalsIgnoreCase(id)){
				//System.out.println(vm.getHostname()+" : "+ id + "   mark");
				vmList.put(vm.getId(), vm);
			}
		} 
		currVmNum = vmList.size();
		return vmList;
	}
	public void setVmList(TreeMap<String, VMelement> vmList) {
		this.vmList = vmList;
	}
	public int getMaxVmNum() {
		return maxVmNum;
	}
	public void setMaxVmNum(int maxVmNum) {
		this.maxVmNum = maxVmNum;
	}
	public int getCurrVmNum() {
		return getVmList().size();
	}
	public void setCurrVmNum(int currVmNum) {
		this.currVmNum = currVmNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}
