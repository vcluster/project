package vcluster.control.cloudman;

import java.util.TreeMap;

import vcluster.control.*;

public class Host {
	private TreeMap<String,VMelement> vmList;
	private int maxVmNum;
	private int currVmNum;
	private String id;
	
	
	
	public Host(int maxVmNum, String id) {
		this.maxVmNum = maxVmNum;
		this.id = id;
		vmList = new TreeMap<String,VMelement>();
	}
	
	public TreeMap<String, VMelement> getVmList() {
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
		return currVmNum;
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
