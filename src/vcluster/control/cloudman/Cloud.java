package vcluster.control.cloudman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.engine.groupexecutor.PlugmanExecutor;
import vcluster.global.Config;
import vcluster.global.Config.CloudType;
import vcluster.plugman.CloudInterface;
import vcluster.plugman.PluginManager;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;
import vcluster.control.*;

public class Cloud{
	
	public Cloud(List<String> conf) {
		this.conf = conf;
		for(String aLine : conf){
			
			StringTokenizer st = new StringTokenizer(aLine, "=");
			
			if (!st.hasMoreTokens()) break;
			
			/* get a keyword */
			String aKey = st.nextToken().trim();
		
			/* get a value */
			if (!st.hasMoreTokens()) break;

			String aValue = st.nextToken().trim();
			
			if (aKey.equalsIgnoreCase("type")){
				setCloudType(aValue);
				break;
								
			}else if((aKey.equalsIgnoreCase("Interface"))){
				setCloudpluginName(aValue);
			}else if((aKey.equalsIgnoreCase("Name"))){

				setCloudName(aValue);
			}
		}
		if(!PluginManager.loadedCloudPlugins.containsKey(this.cloudpluginName))PlugmanExecutor.load("load -c "+cloudpluginName);		
		cp = PluginManager.loadedCloudPlugins.get(this.cloudpluginName);
		this.listVMs();
		if(getVmList()==null)return;
		for(VMelement vm : getVmList().values()){
			Config.vmMan.getVmList().put(new Integer(Config.vmMan.getcurrId()), vm);
		}
		String fName = String.format("%-12s", getCloudName());
		String fInterface =String.format("%-20s", getCloudpluginName());
		String fType = String.format("%-12s", getCloudType());
		String fVMs = String.format("%-16s", vmList.size());
		System.out.println(fName+fInterface+fType+fVMs);
		
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

	public boolean createVM(int maxCount) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<VMelement> vmlist = cp.createVM(maxCount);
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(VMelement vm : vmlist){
			vm.setCloudName(cloudName);
			this.vmList.put(vm.getId(), vm);
			Config.vmMan.getVmList().put(Config.vmMan.getcurrId(), vm);
			System.out.println(cloudName+"   "+vm.getId()+"   "+vm.getState());
		}
		if(maxCount==1){
			System.out.println(maxCount + " virture machine has been created successfully");
		}else{
			System.out.println(maxCount +" virture machines have been created successfully");
		}
		return true;
	}

	public boolean listVMs() {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
	//	HashMap<String,VMelement> vms = new HashMap<String,VMelement>();
		ArrayList<VMelement> vmlist = cp.listVMs();
		vmList = new TreeMap<String,VMelement>();
		//int i = 1;
		if(vmlist==null||vmlist.size()==0)return false;
		for(VMelement vm : vmlist){
			vm.setCloudName(getCloudName());
			vmList.put(vm.getId(), vm);
			//System.out.println(i++ + "             " + vm.getId() + "  " + vm.getState());
		}
		
		return true;
	}
	
	
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<VMelement> vmlist = cp.destroyVM(id);
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(VMelement vm : vmlist){
			vmList.remove(vm.getId());
			System.out.println(cloudName+"   "+vm.getId()+"   "+vm.getState());
		}
		 return true;
	}

	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<VMelement> vmlist = cp.startVM(id);
		
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(VMelement vm : vmlist){
			vmList.get(vm.getId()).setState(vm.getState());
			System.out.println(cloudName+"   "+vm.getId()+"   "+vm.getState());
		}
		return true;
	}

	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<VMelement> vmlist = cp.suspendVM(id);
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(VMelement vm : vmlist){
			vmList.get(vm.getId()).setState(vm.getState());
			System.out.println(cloudName+"   "+vm.getId()+"   "+vm.getState());
		}
		 return true;
	}
	public CloudType getCloudType() {
		
		return cloudType;
	}
	
	public void setCloudType(CloudType type) {
		cloudType = type;
	}
	
	public void setCloudType(String type) {
		if (type.equalsIgnoreCase("private")) 
			cloudType = CloudType.PRIVATE;
		else if (type.equalsIgnoreCase("public")) 
			cloudType = CloudType.PUBLIC;
		else {
			PrintMsg.print(DMsgType.ERROR, "undefined type, "+type+", found");
			cloudType = CloudType.NOT_DEFINED;
		}
		
	}
	
	public String stringCloudType() {
		
		switch(cloudType) {
		case PRIVATE: return "PRIVATE";
		case PUBLIC: return "PUBLIC";
		case NOT_DEFINED: return "NOT_DEFINED";
		}
		return "NOT_DEFINED";
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	
	
	public String getCloudpluginName() {
		return cloudpluginName;
	}

	public void setCloudpluginName(String cloudpluginName) {
		this.cloudpluginName = cloudpluginName;
	}



	public TreeMap<String,VMelement> getVmList() {
		return vmList;
	}

	public void setVmList(TreeMap<String, VMelement> vmList) {
		this.vmList = vmList;
	}



	private String cloudName;
	private String cloudpluginName;
	private List<String> conf;
	private int currentVMs;
	private CloudType cloudType;
	private CloudInterface cp;
	private TreeMap<String, VMelement> vmList;
	
}
