package vcluster.control;

import vcluster.global.Config.VMState;

public class VMelement {
	private String id;
	private VMState state;
	private String name;
	private String memory;
	private String user;
	private String hostname;
	private String time;
	private String ucpu;
	private String group;
	private String cloudName;
	
	public VMelement() {
		this.id = "";
		this.state = VMState.PENDING;
		this.name = "";
		this.memory = "";
		this.user = "";
		this.hostname = "";
		this.time = "";
		this.ucpu = "";
		this.group = "";
		this.cloudName = "";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public VMState getState() {
		return state;
	}
	public void setState(VMState state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUcpu() {
		return ucpu;
	}
	public void setUcpu(String ucpu) {
		this.ucpu = ucpu;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getCloudName() {
		return cloudName;
	}
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}
	public void refreshInfo(){
		
	}
	
	public String stringVMState() {
		
		switch(state) {
		case PENDING: return "PENDING";
		case RUNNING: return "RUNNING";
		case STOP: return "STOP";
		case SUSPEND: return "SUSPEND";
		case PROLOG: return "PROLOG";
		case FAILED: return "FAILED";
		case NOT_DEFINED: return "NOT_DEFINED";
		}
		return "NOT_DEFINED";
	}
	
	
	

}
