package vcluster.control.cloudman;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.control.VMManager;
import vcluster.control.VMelement;
import vcluster.engine.groupexecutor.PlugmanExecutor;
import vcluster.global.Config;
import vcluster.global.Config.CloudType;
import vcluster.plugman.CloudInterface;
import vcluster.plugman.PluginManager;
import vcluster.util.HandleXML;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class Cloud{
	
	public Cloud() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] arg){
		

		
	}

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
			//	System.out.println(aValue);
			}else if(aKey.equalsIgnoreCase("hosts")){
			//	System.out.println(aValue);
				hostList = new TreeMap<String,Host> ();
				String [] hostlist = aValue.split(",");
				for(int i = 0 ; i<hostlist.length;i++){
					String hostname = hostlist[i].split("/")[0];
					String MaxVMNum = hostlist[i].split("/")[1]; 
					hostList.put(hostname, new Host(Integer.parseInt(MaxVMNum),hostname));
				}
			}
		}
		isLoaded = false;
		//HandleXML.addCloudElement(conf);
	}
		
		
	public boolean load(){	
		if(cloudName==null||cloudType==null||cloudpluginName==null){
			return false;
		}
		if(!PluginManager.isLoaded(cloudpluginName))PlugmanExecutor.load("load -c "+cloudpluginName);		
		cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();
		this.listVMs();
		if(getVmList()==null)return false;
		for(VMelement vm : getVmList().values()){
			Integer id = new Integer(VMManager.getcurrId());
			VMManager.getVmList().put(id, vm);
		}
		CloudManager.setCurrentCloud(this);		
		isLoaded = true;
		String fName = String.format("%-12s", getCloudName());
		String fInterface =String.format("%-20s", getCloudpluginName());
		String fType = String.format("%-12s", getCloudType());
		String fVMs = String.format("%-16s", vmList.size());
		System.out.println(fName+fInterface+fType+fVMs);
		HandleXML.setCloudAttribute(cloudName,"isLoaded", "true");
		return true;
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

	public boolean createVM(int maxCount,String hostId) {
		// TODO Auto-generated method stub
		int i = 100;
		for(int j =0;j<conf.size();j++){
			if(conf.get(j).contains("instancetype")){
				i = j;
				break;
			}
		}
		if(i!=100)conf.set(i, "instancetype = m1."+hostId);
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
			if(hostList.size()>1){
				
				String hostname = vm.getHostname();
				//System.out.println(hostname+"    : test");
				Host h = hostList.get(hostname);
				if(h!=null){
					h.getVmList().put(vm.getId(), vm);
				}

				//System.out.println(i++ + "             " + vm.getId() + "  " + vm.getState());
			}else if(hostList.size()==1){
				for(Host host : hostList.values()){
					host.getVmList().putAll(vmList);
				}
			}

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



	public boolean isLoaded() {
		return isLoaded;
	}


	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public TreeMap<String, Host> getHostList() {
		return hostList;
	}
	public void setHostList(TreeMap<String, Host> hostList) {
		this.hostList = hostList;
	}

	private String cloudName;
	private String cloudpluginName;
	private List<String> conf;
	private int currentVMs;
	private CloudType cloudType;
	private CloudInterface cp;
	private TreeMap<String, VMelement> vmList;
	private boolean isLoaded;
	private TreeMap<String,Host> hostList;

	
}
