package vcluster.elements;

import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.executors.BatchExecutor;
import vcluster.executors.PlugmanExecutor;
import vcluster.managers.CloudManager;
import vcluster.managers.PluginManager;
import vcluster.managers.VmManager;
import vcluster.plugInterfaces.CloudInterface;
import vcluster.ui.CmdComb;


/**
 *A class representing a cloud 
 * 
 */
public class Cloud extends Element{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Cloud() {
	
		// TODO Auto-generated constructor stub
	}
	/**
	 *The constructor, by using this constructor some of the member value will be initiated.
	 *@param conf, a List of the cloud configuration value. Such as the cloud name, interface name, etc.
	 * 
	 */
	public Cloud(List<String> conf) {
		this.conf = conf;
		vmList = new TreeMap<String,Vm>();
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
					String hostId = hostStr[0];
					String hostname = hostStr[1];
					String MaxVMNum = hostStr[2];
					String ipmiID = hostStr[3];
					Host host = new Host(Integer.parseInt(MaxVMNum),hostId,hostname,this.cloudName);
					host.setIpmiID(ipmiID);
					//System.out.println(host.getId()+ i + "");
					hostList.put(hostname,host);
				}
			}
		}
		if(hostList==null||hostList.isEmpty()){
			hostList = new TreeMap<String,Host> ();
			Host h = new Host(1000,"0","host1",cloudName);
			hostList.put(h.getName(), h);
		}
		isLoaded = false;
		
	}
		
	/**
	 *This function is for loading a cloud into vcluster. 
	 *Inovke this function the vcluster would connect the corresponding cloud and get the vms'
	 *information.  
	 */
	public String load(){	
		StringBuffer str = new StringBuffer();
		if(cloudName==null||cloudType==null||cloudpluginName==null){
			return null;
		}
		if(!PluginManager.isLoaded(cloudpluginName))PlugmanExecutor.load(new CmdComb("plugman load -c "+cloudpluginName));		
		this.listVMs();
		
		
		CloudManager.setCurrentCloud(this);		
		isLoaded = true;
		String fName = String.format("%-12s", getCloudName());
		String fInterface =String.format("%-20s", getCloudpluginName());
		String fType = String.format("%-12s", getCloudType());
		String fVMs = String.format("%-16s", vmList.size());
		str.append(fName+fInterface+fType+fVMs);
		//HandleXML.setCloudAttribute(cloudName,"isLoaded", "true");
		//System.out.println(str);
		return str.toString();
	}
	
	public int getCurrentVMs() {
		return vmList.size();
	}
	

		
	/**
	 *Get the configurations of the cloud
	 *@return A list of configurations, such as cloud name, interface name connection configurations. 
	 */
	public List<String> getConf() {
		return conf;
	}

	/**
	 *Set the configurations of the cloud
	 *@param conf, a list a the configurations. 
	 */
	public void setConf(List<String> conf) {
		this.conf = conf;
	}

	public void dump(){
		for(String aLine : conf){
			System.out.println(aLine);
		}
	}

	/**
	 * To create a virtual machine.
	 * Through the fucntion creates specified number of virtual machines on the specific host of the cloud.
	 * @param maxCount, the number of virtual machines that you want to create.
	 * @param hostId, the host ID where you want to create vms on.
	 */
	public String createVM(int maxCount,String hostName) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if(!hostName.equalsIgnoreCase("host1")){
			int i = 100;
			for(int j =0;j<conf.size();j++){
				if(conf.get(j).contains("template")){
					i = j;
					break;
				}
			}
			if(i!=100)conf.set(i, "template = templates/"+hostName+".one");
		}

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();
		cp.getCloud(this);
		cp.createVM(maxCount);
		
		if(maxCount==1){
			str.append(maxCount + " virture machine has been created successfully"+System.getProperty("line.separator"));
		}else{
			str.append(maxCount +" virture machines have been created successfully"+System.getProperty("line.separator"));
		}
		//System.out.println(str);
		return str.toString();
	}

	/**
	 *List up the virtual machine that is running on the cloud.
	 * 
	 */
	public boolean listVMs() {
		// TODO Auto-generated method stub

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();
		cp.getCloud(this);
		cp.sync();	
		
		BatchExecutor.mapingActivityToVm();
		return true;
	}
		
	/**
	 * Terminate the given virtual machine on the cloud.
	 * @param id, the given virtual machine's id.
	 */
	public boolean destroyVM(Vm vm) {
		// TODO Auto-generated method stub

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();

		cp.getCloud(this);
		cp.destroyVM(vm.getId());
		 return true;
	}

	/**
	 *Resume a given virtual machine form suspend.
	 *@param id, the given virtual machine's id.
	 *@return boolean. 
	 */
	public boolean startVM(String id) {
		// TODO Auto-generated method stub

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();

		cp.getCloud(this);
		boolean bl = cp.startVM(id);
		
		if(bl){
			System.out.println("Operation failed!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Suspend a given virtual machine from running.
	 * @param id, the virtual machine's id.
	 * 
	 */
	
	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();

		cp.getCloud(this);
		boolean bl = cp.suspendVM(id);
		if(bl){
			System.out.println("Operation failed!");
			return false;
		}
		
		 return true;
	}
	
	/**
	 *Get the type of the cloud :private or public.
	 *@return cloudType.
	 */
	public CloudType getCloudType() {
		
		return cloudType;
	}
	
	/**
	 * Set the type of a cloud
	 * @see class CloudType.
	 * @param cloudType,private or public.
	 */
	public void setCloudType(CloudType type) {
		cloudType = type;
	}
	
	public void setCloudType(String type) {
		if (type.equalsIgnoreCase("private")) 
			cloudType = CloudType.PRIVATE;
		else if (type.equalsIgnoreCase("public")) 
			cloudType = CloudType.PUBLIC;
		else {
			System.out.println("undefined type, "+type+", found");
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

	/**
	 *Get the cloud name .
	 *@return cloudName. 
	 */
	public String getCloudName() {
		return cloudName;
	}

	/**
	 *Set the cloud name.
	 *@param cloudName , the name of the cloud. 
	 */
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	
	/**
	 * Get the name of cloud plugin
	 * @return cloudpluginName, a string of the cloud plugin name.
	 */
	public String getCloudpluginName() {
		return cloudpluginName;
	}

	/**
	 * Set the name of cloud plugin
	 * @param cloudpluginName, The name of cloud plugin
	 */
	public void setCloudpluginName(String cloudpluginName) {
		this.cloudpluginName = cloudpluginName;
	}



	/**
	 * Get the virtual machines list
	 * @return the instances collection of virtual machines
	 *
	 */
	public TreeMap<String,Vm> getVmList() {
		return vmList;
	}

	/**
	 * Set the virtual machines list
	 * @param vmList, a TreeMap collection of virtual machine's instances,the key set is virtual machines' id 
	 */
	public void setVmList(TreeMap<String, Vm> vmList) {
		this.vmList = vmList;
	}



	/**
	 * Judge the cloud is loaded or not.
	 * @return isLoaded, the load status of the cloud.
	 */
	public boolean isLoaded() {
		return isLoaded;
	}


	/**
	 * Set the load status of the cloud
	 * @param isLoaded, boolean type.
	 */
	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * Get the host list of the cloud
	 * @return a TreeMap of host instance list, as key set is the host's id.
	 */
	public TreeMap<String, Host> getHostList() {
		return hostList;
	}
	
	/**
	 * Set the host list
	 * @param hostList. a TreeMap of host instances' list
	 */
	public void setHostList(TreeMap<String, Host> hostList) {
		this.hostList = hostList;
	}
	
	/**
	 * Set the usage priority of the cloud, the smaller number is higher priority.
	 * @param int i, is the value of the priority.
	 */
	public void setPriority(int i) {
		// TODO Auto-generated method stub
		this.priority = i;
	}

	/**
	 * Get the priority of the cloud
	 * @return a number of priotiry.
	 * 
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Migrate a given virtual machine to a specific host.
	 * @param vmId , hostid, The virtual machine's id and the target host id.
	 */
	public String migrate(String vmID,String hostid) {
		// TODO Auto-generated method stub
	/*	if(!vmList.keySet().contains(vmID)||!hostList.keySet().contains(hostid)){
			System.out.println("This virtual machine or host does not exist!");
			return false;
		}*/
		String str="";

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();

		cp.getCloud(null);
		if(cp.migrate(vmID,hostid))
		str = "virtual machine "+vmID+" is being migrated to the host "+hostid+"... ...";
		else str = "Migration failed";
		return str;
	}
	
	/**
	 * Physically turn off the given host.
	 * @param hostID, The given host's id.
	 */
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

		CloudInterface cp = (CloudInterface)PluginManager.pluginList.get(cloudpluginName).getInstance();

		cp.getCloud(null);
		cp.hostoff(host.getIpmiID());
		host.setPowerStat(0);		
		return true;
	}
	
	/**
	 * Physically turn on the given host.
	 * @param the given host's id.
	 */
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
	
	/**
	 * 
	 */
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

	public void addVm(Vm vm){
		vm.setCloudName(this.cloudName);
		if(!vmList.containsKey(vm.getId())){
			vm.setuId(VmManager.generateId());
		}
		else vm.setuId(vmList.get(vm.getId()).getuId());
		vmList.put(vm.getId(), vm);
		
	}
	

	
	public enum CloudType {PRIVATE, PUBLIC, NOT_DEFINED};


	private String cloudName;
	private String cloudpluginName;
	private List<String> conf;
	private CloudType cloudType;
	private TreeMap<String, Vm> vmList;
	private boolean isLoaded;
	private TreeMap<String,Host> hostList;
	private int priority;
	
}
