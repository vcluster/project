package vcluster.control.cloudman;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.control.vmman.Vm;
import vcluster.control.vmman.VmManager;
import vcluster.engine.groupexecutor.BatchExecutor;
import vcluster.engine.groupexecutor.PlugmanExecutor;
import vcluster.global.Config.CloudType;
import vcluster.plugins.CloudInterface;
import vcluster.plugins.plugman.PluginManager;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class Cloud{
	
	public Cloud() {
		// TODO Auto-generated constructor stub
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
				
			}else if((aKey.equalsIgnoreCase("Interface"))){
				setCloudpluginName(aValue);
				cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();
			}else if((aKey.equalsIgnoreCase("Name"))){
				//System.out.println("name");
				setCloudName(aValue);
			//	System.out.println(aValue);
			}else if(aKey.equalsIgnoreCase("hosts")){
			//	System.out.println(aValue);
				hostList = new TreeMap<String,Host> ();
				String [] hostlist = aValue.split(",");
				//System.out.println("host");
				for(int i = 0 ; i<hostlist.length;i++){
					String [] hostStr = hostlist[i].split("/");
					String hostname = hostStr[0];
					String MaxVMNum = hostStr[1];
					String ipmiID = hostStr[2];
					Host host = new Host(Integer.parseInt(MaxVMNum),hostname,this.cloudName);
					host.setIpmiID(ipmiID);
					//System.out.println(host.getId()+ i + "");
					hostList.put(hostname,host);
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
		for(Vm vm : getVmList().values()){
			Integer id = new Integer(VmManager.getcurrId());
			VmManager.getVmList().put(id, vm);
		}
		CloudManager.setCurrentCloud(this);		
		isLoaded = true;
		String fName = String.format("%-12s", getCloudName());
		String fInterface =String.format("%-20s", getCloudpluginName());
		String fType = String.format("%-12s", getCloudType());
		String fVMs = String.format("%-16s", vmList.size());
		System.out.println(fName+fInterface+fType+fVMs);
		//HandleXML.setCloudAttribute(cloudName,"isLoaded", "true");
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
		if(!hostId.equalsIgnoreCase("host1")){
			int i = 100;
			for(int j =0;j<conf.size();j++){
				if(conf.get(j).contains("template")){
					i = j;
					break;
				}
			}
			if(i!=100)conf.set(i, "template = templates/"+hostId+".one");
		}
		cp.RegisterCloud(conf);
		ArrayList<Vm> vmlist = cp.createVM(maxCount);
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(Vm vm : vmlist){
			vm.setCloudName(cloudName);
			vm.setHostname(hostId);
			this.vmList.put(vm.getId(), vm);
			Integer uId = VmManager.getcurrId();
			vm.setuId(uId);
			VmManager.getVmList().put(uId, vm);
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
		ArrayList<Vm> cVmList = cp.listVMs();
		vmList = new TreeMap<String,Vm>();
		//int i = 1;
		if(cVmList==null||cVmList.size()==0)return false;
		for(Vm vm : cVmList){
			if(!this.cloudName.equalsIgnoreCase("Gcloud")){
				vm.setHostname("host1");
			}
			vm.setCloudName(getCloudName());
			vmList.put(vm.getId(), vm);			
		}
		
		BatchExecutor.mapingActivityToVm();
		return true;
	}
		
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		Vm vm = vmList.get(id);
		cp.destroyVM(id+":"+vm.getPrivateIP());
		
		 return true;
	}

	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<Vm> vmlist = cp.startVM(id);
		
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(Vm vm : vmlist){
			vmList.get(vm.getId()).setState(vm.getState());
			System.out.println(cloudName+"   "+vm.getId()+"   "+vm.getState());
		}
		return true;
	}

	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		ArrayList<Vm> vmlist = cp.suspendVM(id);
		if(vmlist==null || vmlist.isEmpty()){
			System.out.println("Operation failed!");
			return false;
		}
		for(Vm vm : vmlist){
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



	public TreeMap<String,Vm> getVmList() {
		return vmList;
	}

	public void setVmList(TreeMap<String, Vm> vmList) {
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
	
	public void setPriority(int i) {
		// TODO Auto-generated method stub
		this.priority = i;
	}

	public int getPriority() {
		return priority;
	}

	public boolean migrate(String vmID,String hostid) {
		// TODO Auto-generated method stub
	/*	if(!vmList.keySet().contains(vmID)||!hostList.keySet().contains(hostid)){
			System.out.println("This virtual machine or host does not exist!");
			return false;
		}*/
		
		cp.RegisterCloud(conf);
		cp.migrate(vmID,hostid);
		
		return true;
	}
	public boolean hostoff(String hostID) {
		// TODO Auto-generated method stub
		Host host = hostList.get(hostID);
		if(host.getPowerStat()==0){
			System.out.println("Host current status is power off!!");
			return false;
		}
		if(!host.getVmList().isEmpty()){
			System.out.println("Host still has vms is running,cannot be shutted down!");
			return false;
		}
		cp.RegisterCloud(conf);
		cp.hostoff(host.getIpmiID());
		host.setPowerStat(0);		
		return true;
	}
	public boolean hoston(String hostID) {
		// TODO Auto-generated method stub
		Host host = hostList.get(hostID);
		if(host.getPowerStat()==1){
			System.out.println("Host current status is power on!!");
			return false;
		}
		
		host.setPowerStat(3);		
		return true;
	}
	public String slotNameToVMId(String slotName){
		String vmId="";
		if(cloudName.equals("Gcloud")){
			if(slotName.contains(".")){
				vmId=slotName.replace(".kisti", "").split("-")[1].replace("vm", "").trim();
			}else{
				vmId=slotName.split("-")[1].replace("vm", "");
			}
			
		}else if(cloudName.equalsIgnoreCase("fermicloud")){
			
		}else if(cloudName.equalsIgnoreCase("amazon")){
			
			String ip = slotName.replace(".amaz", "").replaceAll("-", ".");
			System.out.println(ip);
			for(Vm vm : vmList.values()){
				if(vm.getPrivateIP().equalsIgnoreCase(ip)){
					vmId = vm.getId();
				}
			}
		}
		return vmId;
	}
	class HostControlRunner implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}




	private String cloudName;
	private String cloudpluginName;
	private List<String> conf;
	private int currentVMs;
	private CloudType cloudType;
	private CloudInterface cp;
	private TreeMap<String, Vm> vmList;
	private boolean isLoaded;
	private TreeMap<String,Host> hostList;
	private int priority;

	
}
